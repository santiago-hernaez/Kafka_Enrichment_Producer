package com.projecto;

import com.projecto.kafka.Consumer;
import com.projecto.kafka.Producers;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class KafkaService {
    public static void main(String[] args) {
        LinkedBlockingQueue<Map<String, Object>> inQueue = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<Map<String, Object>> outQueue = new LinkedBlockingQueue<>();

        String inTopic = "inditex";
        String controlTopic = "enrichInditex";

        Integer partitions = 1;

        final Consumer consumer = new Consumer(inTopic, partitions, inQueue);
        final Processor processor = new Processor(inQueue, outQueue);
        final Producers producer = new Producers(controlTopic, outQueue);

        producer.start();
        processor.start();
        consumer.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                consumer.shutdown();
                processor.shutdown();
                producer.shutdown();
                System.out.println("Apagado!");
            }
        });
    }
}
