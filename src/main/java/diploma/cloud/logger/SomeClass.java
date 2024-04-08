package diploma.cloud.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SomeClass {
    private static final Logger logger = LogManager.getLogger(SomeClass.class);

    public void someMethod() {
        // Логирование различных действий
        logger.info("Выполнено действие X");
        logger.warn("Предупреждение: действие Y");
        logger.error("Произошла ошибка в действии Z");
    }
}
