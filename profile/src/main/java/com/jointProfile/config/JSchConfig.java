package com.jointProfile.config;

import com.jcraft.jsch.JSch;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JSchConfig {

    @Bean
    public JSch jsch() {
        return new JSch();
    }
}