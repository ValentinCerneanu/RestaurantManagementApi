package com.resturant.management;

import com.github.javafaker.Faker;
import com.resturant.management.models.MenuItem;
import com.resturant.management.models.MenuOrder;
import com.resturant.management.models.OrderItem;
import com.resturant.management.models.Waiter;
import com.resturant.management.repositories.MenuRepository;
import com.resturant.management.repositories.OrderItemRepository;
import com.resturant.management.repositories.OrderRepository;
import com.resturant.management.repositories.WaiterRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
public class ManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagementApplication.class, args);
	}

	@KafkaListener(topics = "Hello-Kafka")
	public void listen(String message){
		System.out.println("Received message in group:" + message);
	}

//	@Bean
//	CommandLineRunner commandLineRunner(
//			MenuRepository menuRepository,
//			OrderRepository orderRepository,
//			OrderItemRepository orderItemRepository,
//			WaiterRepository waiterRepository)
//	{
//		return args -> {
//			Faker faker = new Faker();
//
//			String firstName = faker.name().firstName();
//			String lastName = faker.name().lastName();
//			Waiter waiter = new Waiter(firstName, lastName);
//			waiterRepository.save(waiter);
//
//			MenuItem menuItem = new MenuItem("Drinks", "Beer", 2.2);
//			menuRepository.save(menuItem);
//
//			MenuOrder menuOrder = new MenuOrder(waiter, "active", "8A");
//			orderRepository.save(menuOrder);
//
//			OrderItem orderItem = new OrderItem(menuOrder, menuItem, 2);
//			orderItemRepository.save(orderItem);
//		};
//
//	}
}
