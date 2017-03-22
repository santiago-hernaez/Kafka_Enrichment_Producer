package com.projecto.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

public class Producers extends Thread {
    LinkedBlockingQueue<Map<String, Object>> outQueue;
    KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(getProducerConfig());
    ObjectMapper objectMapper;
    String topic;

    public Producers(String topic,
                     LinkedBlockingQueue<Map<String, Object>> outQueue) {
        this.outQueue = outQueue;
        this.objectMapper = new ObjectMapper();
        this.topic = topic;

    }

    private Properties getProducerConfig() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.ACKS_CONFIG,"1");
        return properties;
    }

    public void lanza(String topic, LinkedBlockingQueue<Map<String,Object>> outQueue) {

         try {
            Map<String, Object> event = outQueue.take();

             if (topic.substring(0,3).equals("Web")) {
                 topic = topic.substring(3);
                 }


            String json = objectMapper.writeValueAsString(event);

            kafkaProducer.send(new ProducerRecord<String, String>(topic, json));

            } catch (InterruptedException e) {
            System.out.println("Apagando el productor ... ");

            } catch (JsonProcessingException e) {
            System.out.println("No se puede convertir a JSON");
          }
        }
    @Override
    public void run() {}
    public void cierra() {kafkaProducer.close();}
    public void shutdown() {
       interrupt();
    }
}
