package com.projecto;

import com.projecto.kafka.Producers;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import com.projecto.redis.IO;
import com.projecto.redis.Redis;
import redis.clients.jedis.Jedis;

public class Processor extends Thread {
    LinkedBlockingQueue<Map<String, Object>> inQueue;
    LinkedBlockingQueue<Map<String, Object>> outQueue;

    private Redis redis;

    public Processor(LinkedBlockingQueue<Map<String, Object>> inQueue,
                     LinkedBlockingQueue<Map<String, Object>> outQueue) {
        this.inQueue = inQueue;
        this.outQueue = outQueue;
    }

    public String convertTime(long time) {
        long second = (time / 1000) % 60;
        long minutes = (time / (1000 * 60)) % 60;
        long hours = (time / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hours, minutes, second);

    }


    @Override

    public void run() {
        Producers productor = new Producers("", outQueue);
        System.out.println ("Iniciando captura de Tickets...");
        while (!isInterrupted()) {
            try {
                redis = Redis.getInstance();
                Map<String, Object> inEvent = inQueue.take();


                //"id_Tienda":1-7031, "metodoPago":"Efectivo"/"Tarjeta"/"TarjetaRegalo"/"App"/"Affinity", "fecha":"timestamp"
                //"Prendas":[idPrenda,...], "total":0.99-149.99

                ArrayList id_prenda = (ArrayList) inEvent.get("Prendas");
                int prenda = id_prenda.size();

                //recorremos el array de prendas y creamos un log por cada prenda del ticket.
                for (int i = 0 ; i<prenda; i++) {
                    inEvent = redis.enrich(inEvent, i);

                    // Juntamos los datos y tirarlos al OutQueue
                    inEvent.put("CONTROL", "Ok");

                    String cadena = (String) inEvent.get("cadena");

                    outQueue.put(inEvent);

                    productor.lanza(cadena, outQueue);
                    }

                    inEvent.remove("Total");
                    inEvent.remove("prendascompradas");


            } catch (InterruptedException e) {
                System.out.println("Apagando el procesador ... ");
                productor.cierra();

            }

        }
    }




    public void shutdown() {
        interrupt();
    }


}

