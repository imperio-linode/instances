package com.bntech.imperio.instances.exception;

import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

/**
 * Transform Throwable to Mono<Throwable>
 *
 * @throwable Throwable that is to be converted.
 */
public class ThrowableTransformatorImpl implements ThrowableTransformService {
    private final HttpStatus httpStatus;
    private final String message;

    private ThrowableTransformatorImpl(final Throwable throwable) {
        this.httpStatus = getStatus(throwable);
        this.message = throwable.getMessage();
    }

    public static <T extends Throwable> Mono<ThrowableTransformatorImpl> toMono(final Mono<T> throwable) {
        return throwable.flatMap(error -> Mono.just(new ThrowableTransformatorImpl(error)));
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

    private HttpStatus getStatus(final Throwable error) {
        if (error instanceof NotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
