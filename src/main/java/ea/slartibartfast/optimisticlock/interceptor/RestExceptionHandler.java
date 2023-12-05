package ea.slartibartfast.optimisticlock.interceptor;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class RestExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    protected void handleEntityNotFoundException(Exception ex) {
        log.error("EntityNotFoundException handled in Controller", ex);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({OptimisticLockException.class, ObjectOptimisticLockingFailureException.class})
    protected void handleOptimisticLockException(Exception ex) {
        log.error("OptimisticLockException handled in Controller", ex);
    }
}
