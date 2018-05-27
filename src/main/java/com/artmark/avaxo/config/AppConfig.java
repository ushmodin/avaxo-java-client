package com.artmark.avaxo.config;

import com.artmark.avaxo.command.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Ushmodin N.
 * @since 05.01.2016
 */
@Configuration
@ComponentScan("com.artmark.avaxo")
@EnableScheduling
@PropertySource(value = {"/WEB-INF/config/application.properties", "classpath:/config/application.properties"}, ignoreResourceNotFound = true)
public class AppConfig {
    @Autowired
    private RegistrationService registrationService;

    @Bean(destroyMethod = "shutdown")
    public ExecutorService executor() {
        return Executors.newCachedThreadPool();
    }

    @Bean
    public Settings settings() {
        return new Settings();
    }

    @EventListener
    public void applicationStarted(ContextRefreshedEvent event) {
        executor().execute(registrationService);
    }
}
