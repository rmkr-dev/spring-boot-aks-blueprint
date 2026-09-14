package dev.rmkr.blueprint;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.classic.PatternLayout;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class LogbackRequestIdPatternTest {

    @Test
    void logbackSpringXmlIsOnClasspath() throws Exception {
        ClassPathResource resource = new ClassPathResource("logback-spring.xml");
        assertTrue(resource.exists());
        String xml = new String(resource.getInputStream().readAllBytes());
        assertTrue(xml.contains("%X{requestId"));
    }

    @Test
    void consoleAppenderPatternIncludesRequestIdMdc() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger root = context.getLogger(Logger.ROOT_LOGGER_NAME);
        boolean found = false;
        for (Iterator<Appender<ILoggingEvent>> it = root.iteratorForAppenders(); it.hasNext(); ) {
            Appender<ILoggingEvent> appender = it.next();
            if (appender instanceof ConsoleAppender<?> console) {
                Encoder<?> encoder = console.getEncoder();
                String pattern = null;
                if (encoder instanceof ch.qos.logback.classic.encoder.PatternLayoutEncoder ple) {
                    pattern = ple.getPattern();
                } else if (encoder instanceof LayoutWrappingEncoder<?> lwe
                        && lwe.getLayout() instanceof PatternLayout layout) {
                    pattern = layout.getPattern();
                }
                if (pattern != null && pattern.contains("%X{requestId")) {
                    found = true;
                    break;
                }
            }
        }
        assertTrue(found, "Expected a CONSOLE appender pattern containing requestId MDC");
    }
}
