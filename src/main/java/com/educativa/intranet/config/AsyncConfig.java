package com.educativa.intranet.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {
    // Esta clase tan pequeña le da el superpoder a Spring Boot de abrir hilos 
    // paralelos o "cintas transportadoras secundarias" usando @Async
}
