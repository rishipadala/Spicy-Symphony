package com.spicysymphony.restaurant_booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.spicysymphony.restaurant_booking.repository")
@ComponentScan(basePackages = "com.spicysymphony.restaurant_booking")
@EnableAsync
public class RestaurantBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantBookingApplication.class, args);
	}
}
