package com.umograd;

import org.springframework.boot.SpringApplication;

public class TestUmogradBackendApplication {

    public static void main(String[] args) {
        SpringApplication.from(UmogradBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
