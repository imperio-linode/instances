package com.bntech.imperio.instances.handler;

import com.bntech.imperio.instances.data.object.ErrorResponse;
import com.bntech.imperio.instances.exception.NotFoundException;
import com.bntech.imperio.instances.exception.ThrowableTransformatorImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class ErrorHandler {

    private static final String NOT_FOUND = "not found";
    private static final String ERROR_RAISED = "error raised";

    public Mono<ServerResponse> notFound(final ServerRequest request) {
        return Mono.just(new NotFoundException(NOT_FOUND)).transform(this::getResponse);
    }

    <T extends Throwable> Mono<ServerResponse> getResponse(final Mono<T> monoError) {
        return monoError.transform(ThrowableTransformatorImpl::toMono)
                .flatMap(translation -> ServerResponse
                        .status(translation.getHttpStatus())
                        .body(Mono.just(new ErrorResponse(translation.getMessage())), ErrorResponse.class));
    }

    public Mono<ServerResponse> throwableError(final Throwable error) {
        log.error(ERROR_RAISED, error);
        return Mono.just(error).transform(this::getResponse);
    }
}
