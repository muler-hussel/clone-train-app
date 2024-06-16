package org.clone.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
@ComponentScan("org.clone")
public class MemberApplication {
    private static final Logger LOG = LoggerFactory.getLogger(MemberApplication.class);
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MemberApplication.class);
        Environment evn = app.run(args).getEnvironment();
        LOG.info("Start successfully.");
        LOG.info("Addr: \thttp://127.0.0.1:{}{}/hello", evn.getProperty("server.port"), evn.getProperty("server.servlet.context-path"));

    }

}
