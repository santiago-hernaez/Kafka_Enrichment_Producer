package com.projecto.kafka;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


public class Consumer {
    LinkedBlockingQueue<Map<String, Object>> inQueue;
    Integer partitions;
    String topic;
    private ExecutorService executor;
    public static List<ConsumerStream> consumers = new ArrayList<>();
    public Consumer(String topic, Integer partitions, LinkedBlockingQueue<Map<String, Object>> inQueue) {
        this.inQueue = inQueue;
        this.partitions = partitions;
        this.topic = topic;
        this.executor = Executors.newFixedThreadPool(partitions);
    }

    private Properties getKafkaConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG,"Proyecto");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }

    public void start() {

        for (Integer threads = 0; threads<=partitions;threads++) {
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(getKafkaConsumer());
            ConsumerStream consumerRunner = new ConsumerStream(consumer, topic, inQueue);
            consumers.add(consumerRunner);
            executor.submit(consumerRunner);
        }

    }

    public void shutdown() {
        for (ConsumerStream consumerStream : consumers) consumerStream.shutdown();
        if (executor != null) executor.shutdown();
        try {
            if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                System.out.println("Tiempo de espera agotado.");

            }
        } catch (InterruptedException e) {
            System.out.println("Interrupcion durante el apagado!!");
        }
    }
}
