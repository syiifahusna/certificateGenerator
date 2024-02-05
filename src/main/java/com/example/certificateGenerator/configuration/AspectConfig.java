package com.example.certificateGenerator.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = "com.example.certificateGenerator")
@EnableAspectJAutoProxy
public class AspectConfig {
}
