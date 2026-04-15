package com.sena.BogotaMetroApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;


@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class BogotaMetroAppApplication {

    public static void main(String[] args) {

        SpringApplication.run(BogotaMetroAppApplication.class, args);
    }

}
