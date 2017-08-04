package be.deschutter.planetarion.katana;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.definition.UnresolvingLocaleDefinitionsFactory;
import org.apache.tiles.request.Request;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(scanBasePackages = {"be.deschutter"})
public class MvcConfig extends WebMvcConfigurerAdapter {

    private static final Map<String, Definition> tiles = new HashMap<>();
    private static final Attribute TEMPLATE = new Attribute(
        "/WEB-INF/views/layout/layout.jsp");

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }


    @Bean
    public UrlBasedViewResolver setupViewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        resolver.setOrder(1);
        return resolver;
    }

    @Bean
    public UrlBasedViewResolver tilesViewResolver() {
        UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
        viewResolver.setViewClass(TilesView.class);
        viewResolver.setOrder(0);
        return viewResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
            .addResourceLocations("/resources/","resources","/resources","resources/");
    }

    @Bean
    public TilesConfigurer tilesConfigurer() {
        TilesConfigurer tilesConfigurer = new TilesConfigurer();
        tilesConfigurer
            .setDefinitionsFactoryClass(JavaDefinitionsFactory.class);
        tilesConfigurer.setDefinitions(new String[] {});

        addDefinition("admin/dashboard", "Dashboard",
            "/WEB-INF/views/tiles/admin/dashboard.jsp");

        addDefinition("index", "Dashboard",
            "/WEB-INF/views/tiles/index.jsp");

        addDefinition("stats", "Stats",
            "/WEB-INF/views/tiles/stats.jsp");

        return tilesConfigurer;
    }

    private void addDefinition(String name, String title, String body) {
        Map<String, Attribute> attributes = getDefaultAttributes();

        attributes.put("title", new Attribute(title));
        attributes.put("body", new Attribute(body));

        tiles.put(name, new Definition(name, TEMPLATE, attributes));
    }

    private Map<String, Attribute> getDefaultAttributes() {
        Map<String, Attribute> attributes = new HashMap<>();

        attributes
            .put("header", new Attribute("/WEB-INF/views/layout/header.jsp"));
        attributes.put("menu", new Attribute("/WEB-INF/views/layout/menu.jsp"));

        return attributes;
    }

    public static class JavaDefinitionsFactory
        extends UnresolvingLocaleDefinitionsFactory {

        public JavaDefinitionsFactory() {
        }

        @Override
        public Definition getDefinition(String name, Request tilesContext) {
            return tiles.get(name);
        }
    }


    public static void main(String[] args) {
        SpringApplication.run(MvcConfig.class, args);
    }
}