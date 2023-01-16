package ru.markush.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DirectSenderApp {
    public static final String EXCHANGE_NAME = "IT_BLOG";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();
             Scanner task = new Scanner(System.in)) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);


            System.out.println("Enter some theme like php, java or c++. And message like: php some message");

            String setTopic = task.nextLine().toLowerCase();
            int i = setTopic.indexOf(' ');
            String topic = setTopic.substring(0,i);
            switch (topic) {
                case "php" ->
                        channel.basicPublish(EXCHANGE_NAME, "php", null, "php some message".getBytes(StandardCharsets.UTF_8));
                case "java" ->
                        channel.basicPublish(EXCHANGE_NAME, "java", null, "java some message".getBytes(StandardCharsets.UTF_8));
                case "c++" ->
                        channel.basicPublish(EXCHANGE_NAME, "c++", null, "c++ some message".getBytes(StandardCharsets.UTF_8));
                default ->
                        channel.basicPublish(EXCHANGE_NAME, "NONe", null, "none message".getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
