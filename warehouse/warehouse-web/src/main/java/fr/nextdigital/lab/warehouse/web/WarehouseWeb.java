package fr.nextdigital.lab.warehouse.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.hateoas.config.EnableHypermediaSupport;

import fr.nextdigital.lab.event.EventAutoConfig;

@SpringBootApplication(exclude = EventAutoConfig.class)
@EnableDiscoveryClient
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class WarehouseWeb {

    public static void main(String[] args) {
        SpringApplication.run(WarehouseWeb.class, args);
    }
}
