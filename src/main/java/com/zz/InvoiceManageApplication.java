package com.zz;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 韦笑迎
 * ===============================
 * Date：2025/12/10
 * Time：17:02
 * ================================
 */
@SpringBootApplication
@MapperScan("com.zz.modules.**.mapper")
public class InvoiceManageApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(InvoiceManageApplication.class);
    
    public static void main(String[] args) {
        SpringApplication.run(InvoiceManageApplication.class, args);
        logger.info("Invoice Management Application started successfully");
    }
}