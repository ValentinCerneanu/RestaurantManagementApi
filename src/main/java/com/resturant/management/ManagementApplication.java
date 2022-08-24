package com.resturant.management;

import com.github.javafaker.Faker;
import com.resturant.management.models.Item;
import com.resturant.management.models.Order;
import com.resturant.management.models.OrderItem;
import com.resturant.management.models.Waiter;
import com.resturant.management.repositories.ItemRepository;
import com.resturant.management.repositories.OrderItemRepository;
import com.resturant.management.repositories.OrderRepository;
import com.resturant.management.repositories.WaiterRepository;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Properties;
import java.util.Set;

@SpringBootApplication
public class ManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagementApplication.class, args);
	}

	@KafkaListener(topics = "Hello-Kafka")
	public void listen(String message){
		System.out.println("Received message in group:" + message);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void checkKafkaServerStatus() {
		boolean isKafkaUp = false;
		Properties properties = new Properties();
		properties.put("bootstrap.servers", "localhost:9092");
		properties.put("connections.max.idle.ms", 10000);
		properties.put("request.timeout.ms", 5000);
		try (AdminClient client = KafkaAdminClient.create(properties))
		{
			ListTopicsResult topics = client.listTopics();
			Set<String> names = topics.names().get();
			if (names.contains("new_order") && names.contains("new_added_items_on_order") && names.contains("changed_order_status"))
			{
				isKafkaUp = true;
			}
		}
		catch (Exception ex)
		{
			// Kafka is not available
			ex.printStackTrace();
		}
	}

	@Bean
	CommandLineRunner commandLineRunner(
			ItemRepository itemRepository,
			OrderRepository orderRepository,
			OrderItemRepository orderItemRepository,
			WaiterRepository waiterRepository)
	{
		return args -> {
			Faker faker = new Faker();

			String firstName = faker.name().firstName();
			String lastName = faker.name().lastName();
			Waiter waiter = new Waiter(firstName, lastName);
			waiterRepository.save(waiter);

			Item item = new Item("Drinks", "Beer", 2.2);
			itemRepository.save(item);

			Order order = new Order(waiter, "active", "8A");
			orderRepository.save(order);

			OrderItem orderItem = new OrderItem(order, item, 2);
			orderItemRepository.save(orderItem);
		};

	}
}
