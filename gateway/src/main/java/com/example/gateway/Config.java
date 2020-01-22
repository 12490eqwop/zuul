package com.example.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix="zuul.route.service1")

public class Config {

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }

    private List<String> url = new ArrayList<>();

    @Override
    public String toString() {
        return "Config{" +
                "url=" + url +
                '}';
    }

//    public List<String> host = new ArrayList<>();
//
//    public List<String> getHost() {
//        return host;
//    }
//
//    public void setHost(List<String> host) {
//        this.host = host;
//    }
//
//    @Override
//    public String toString() {
//        return "Config{" +
//                "host=" + host +
//                '}';
//    }
}
