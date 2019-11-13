package com.yang.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Level;

@Configuration
public class FeignLogConfiguration {

    @Bean
    Logger.Level feignLoggerLever(){
        return Logger.Level.FULL;
    }
}
