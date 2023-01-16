package ru.markush.consumer;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DirectReceiver {
    private static final String EXCHANGE_NAME = "IT_BLOG";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        Scanner scanner = new Scanner(System.in);

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        String queueName = channel.queueDeclare().getQueue();
        System.out.println("New connection to IT-BLOG # : " + queueName);
        System.out.println("-------------------------------------------");
        System.out.println("Select topic: java, php or c++");
        System.out.println("Enter: set_topic {topic}");

        String topic = scanner.nextLine().substring(10);

        scanner.close();

        switch (topic) {
            case "php" -> channel.queueBind(queueName, EXCHANGE_NAME, "php");
            case "java" -> channel.queueBind(queueName, EXCHANGE_NAME, "java");
            case "c++" -> channel.queueBind(queueName, EXCHANGE_NAME, "c++");
        }


        System.out.println("[*} Waiting messages....");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[x] Received '" + message +"'");
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}
