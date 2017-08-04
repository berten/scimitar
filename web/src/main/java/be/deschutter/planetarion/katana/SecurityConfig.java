package be.deschutter.planetarion.katana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationMgr)
        throws Exception {
        authenticationMgr.inMemoryAuthentication().withUser("berten")
            .password("berten").authorities("ROLE_USER", "ROLE_ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/index")
            .access("hasRole('ROLE_USER')").antMatchers("/admin")
            .access("hasRole('ROLE_ADMIN')").and().formLogin()
            .loginPage("/login").defaultSuccessUrl("/index").and().rememberMe();
    }
}
