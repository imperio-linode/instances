package com.bntech.imperio.instances;

import com.bntech.imperio.instances.handler.ErrorHandler;
import com.bntech.imperio.instances.handler.InstanceHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import static com.bntech.imperio.instances.config.Constants.api_ADD;
import static com.bntech.imperio.instances.config.Constants.api_HELLO;
import static com.bntech.imperio.instances.config.Constants.api_ID;
import static com.bntech.imperio.instances.config.Constants.api_UPDATE_ENGINES;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class DataRouter {

    @Bean
    public static RouterFunction<?> doRoute(final InstanceHandler instanceHandler, final ErrorHandler errorHandler) {
        return
                route(GET(api_ID),
                        instanceHandler::instanceDetails
                ).andRoute(POST(api_ADD),
                        instanceHandler::handleDeploy
                ).andRoute(GET(api_HELLO), instanceHandler::hello
                ).andRoute(GET(api_UPDATE_ENGINES), instanceHandler::update);
    }
}
