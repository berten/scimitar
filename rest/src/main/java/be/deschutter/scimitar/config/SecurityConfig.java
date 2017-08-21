package be.deschutter.scimitar.config;

import be.deschutter.scimitar.Role;
import be.deschutter.scimitar.RoleEnum;
import be.deschutter.scimitar.ScimitarUser;
import be.deschutter.scimitar.ScimitarUserEao;
import org.apache.catalina.filters.HttpHeaderSecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private ScimitarUserEao scimitarUserEao;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable().httpBasic()
            .authenticationEntryPoint(restAuthenticationEntryPoint()).and()
            .addFilterBefore(filter(), BasicAuthenticationFilter.class)
            .authorizeRequests().anyRequest().hasAnyAuthority("ROLE_ANONYMOUS");
    }

    private Filter filter() {
        return new HttpHeaderSecurityFilter() {
            @Override
            public void doFilter(final ServletRequest servletRequest,
                final ServletResponse servletResponse,
                final FilterChain filterChain)
                throws IOException, ServletException {

                final Enumeration<String> headerNames = ((HttpServletRequest) servletRequest)
                    .getHeaderNames();

                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        System.out.println(
                            "Header: " + ((HttpServletRequest) servletRequest)
                                .getHeader(headerNames.nextElement()));
                    }
                }

                final String loggedInUsername = ((HttpServletRequest) servletRequest)
                    .getHeader("loggedInUsername");
                final Authentication authentication = authenticationManager()
                    .authenticate(
                        new RunAsUserToken(loggedInUsername, loggedInUsername,
                            null, null, null));

                SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

                filterChain.doFilter(servletRequest, servletResponse);
            }
        };
    }

    @Override
    @Transactional
    protected AuthenticationManager authenticationManager() {
        return authentication -> {
            if (authentication.getPrincipal() == null || !(authentication
                .getPrincipal() instanceof String) || ((String) authentication
                .getPrincipal()).isEmpty())
                return null;
            final String principal = (String) authentication.getPrincipal();
            final ScimitarUser user = scimitarUserEao
                .findByUsernameIgnoreCase(principal);

            return new RunAsUserToken(principal, user, "",
                user.getRoles().stream().map(
                    (Function<Role, GrantedAuthority>) role -> new SimpleGrantedAuthority(
                        "ROLE_" + role.getRole())).collect(Collectors.toList()),
                RunAsUserToken.class);

        };
    }

    @Bean
    public AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return (httpServletRequest, httpServletResponse, e) -> httpServletResponse
            .sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    @PostConstruct
    public void initLoginUser() {
        final ScimitarUser s = new ScimitarUser();
        s.setUsername("Berten");
        Arrays.stream(RoleEnum.values()).forEach(s::addRole);
        scimitarUserEao.saveAndFlush(s);
    }
}
