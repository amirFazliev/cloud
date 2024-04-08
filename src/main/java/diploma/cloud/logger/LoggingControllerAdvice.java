package diploma.cloud.logger;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.apache.log4j.Logger;

@RestControllerAdvice
public class LoggingControllerAdvice {

    private static final Logger logger = Logger.getLogger(LoggingControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    public void logException(Exception ex, WebRequest request) {
        logger.error("Exception occurred: " + ex.getMessage());
    }

    // Логирование входящих запросов
    @RequestMapping("*")
    public void logRequest(HttpServletRequest request) {
        logger.info("Incoming Request: " + request.getMethod() + " " + request.getRequestURI());
    }

    // Логирование исходящих ответов
    @AfterReturning(pointcut = "execution(* diploma.cloud.controller.*.*(..))", returning = "result")
    public void logResponse(JoinPoint joinPoint, Object result) {
        logger.info("Outgoing Response: " + result.toString());
    }
}
