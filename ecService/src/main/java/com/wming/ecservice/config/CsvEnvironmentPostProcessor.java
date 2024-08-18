package com.wming.ecservice.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CsvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> properties = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("env.csv"), StandardCharsets.UTF_8))) {

            properties.putAll(reader.lines()
                    .skip(1)  // 첫 줄은 헤더이므로 건너뜁니다.
                    .map(line -> line.split(","))
                    .collect(Collectors.toMap(parts -> parts[0], parts -> parts[1])));

        } catch (Exception e) {
            System.err.println("Error loading CSV file for environment properties");
            e.printStackTrace();
            throw new RuntimeException("Failed to load properties from env.csv", e);
        }

        environment.getPropertySources().addLast(new MapPropertySource("csvPropertySource", properties));
    }
}