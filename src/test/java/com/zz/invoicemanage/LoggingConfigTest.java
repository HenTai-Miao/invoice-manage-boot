package com.zz.invoicemanage;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LoggingConfigTest {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingConfigTest.class);
    
    @Test
    public void testLogging() {
        logger.info("This is a test log message");
        logger.warn("This is a warning log message");
        logger.error("This is an error log message");
        
        // Generate multiple log entries to test file rotation
        for (int i = 0; i < 100; i++) {
            logger.info("Log entry number: {}", i);
        }
    }
}