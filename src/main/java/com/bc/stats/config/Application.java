package com.bc.stats.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("com.bc.stats")
@EnableAutoConfiguration
@EnableJpaRepositories("com.bc.stats.domain.repository")
@EntityScan("com.bc")
@EnableTransactionManagement(mode = AdviceMode.PROXY)
public class Application {

   public static void main(String[] args) {
      SpringApplication.run(Application.class, args);
   }
}
