package com.aries.oam.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
@SpringBootApplication
public class ProxyApplication {
    @Bean
    public JenniferFilter jenniferFilter() {
        return new JenniferFilter();
    }

    public static void main(String[] args) {
        SpringApplication.run(ProxyApplication.class, args);
    }
}

