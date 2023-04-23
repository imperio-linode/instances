package com.bntech.imperio.instances;

import com.bntech.imperio.instances.handler.ErrorHandler;
import com.bntech.imperio.instances.handler.InstanceHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import static com.bntech.imperio.instances.config.Constants.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class DataRouter {

    @Bean
    public static RouterFunction<?> doRoute(final InstanceHandler instanceHandler, final ErrorHandler errorHandler) {
        return route(GET(api_ID),
                instanceHandler::instanceDetails
        ).andRoute(POST(api_CREATE_ENGINE),
                instanceHandler::instanceDeploy
        ).andRoute(GET(api_HELLO),
                instanceHandler::hello
        ).andRoute(GET(api_UPDATE_DB),
                instanceHandler::update
        ).andRoute(DELETE(api_ID),
                instanceHandler::instanceDelete);
    }
}
