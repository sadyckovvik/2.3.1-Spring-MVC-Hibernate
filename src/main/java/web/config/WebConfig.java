package web.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@Configuration
@EnableWebMvc // поддержка Spring MVC
@ComponentScan("web")
public class WebConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    public WebConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Bean
    public SpringResourceTemplateResolver templateResolver() { //сообщает Thymeleaf, где искать шаблоны и как их обрабатывать
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        //Это класс Thymeleaf, который отвечает за разрешение (поиск) шаблонов
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/view/"); //Указывает папку, в которой находятся HTML-шаблоны
        templateResolver.setSuffix(".html"); //расширение файлов шаблонов
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() { //обрабатывает HTML-шаблоны, подставляет данные и возвращает готовый HTML
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true); //позволяет использовать выражения Spring в шаблонах ${user.name}
        return templateEngine;
    }


    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) { //ViewResolver отвечает за преобразование имен представлений (например, "home") в реальные HTML-страницы
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }
}

//Запрос поступает в Spring MVC:
//Например, пользователь запрашивает страницу /home.
//Контроллер возвращает имя представления:
//Контроллер возвращает строку "home".
//ViewResolver находит шаблон:
//ThymeleafViewResolver ищет шаблон /WEB-INF/pages/home.html.
//Thymeleaf обрабатывает шаблон:
//SpringTemplateEngine обрабатывает шаблон, подставляет данные и возвращает готовый HTML.
//HTML отправляется пользователю:
//Пользователь видит готовую страницу.