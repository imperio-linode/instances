package com.bntech.imperio.instances.config;


import com.bntech.imperio.instances.DataRouter;
import com.bntech.imperio.instances.handler.ErrorHandler;
import com.bntech.imperio.instances.handler.InstanceHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.server.RouterFunction;

import static com.bntech.imperio.instances.config.Constants.APP_PROPERTIES;

@Configuration
@PropertySource(APP_PROPERTIES)
public class AppConfig {

    @Bean
    ErrorHandler errorHandler() {
        return new ErrorHandler();
    }

    @Bean
    RouterFunction<?> mainRouterFunction(final InstanceHandler instanceHandler, final ErrorHandler errorHandler) {
        return DataRouter.doRoute(instanceHandler, errorHandler);
    }
}
