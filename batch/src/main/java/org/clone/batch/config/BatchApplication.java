package org.clone.batch.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
@ComponentScan("org.clone")
@MapperScan("org.clone.batch.mapper")
public class BatchApplication {
    private static final Logger LOG = LoggerFactory.getLogger(BatchApplication.class);
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BatchApplication.class);
        Environment evn = app.run(args).getEnvironment();
        LOG.info("Start successfully.");
        LOG.info("Addr: \thttp://127.0.0.1:{}{}", evn.getProperty("server.port"), evn.getProperty("server.servlet.context-path"));

    }

}
