package com.projecto.redis;

import redis.clients.jedis.Jedis;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map;


/**
 * Conexión a la redis. Implementa las funciones de nuestro sistema de consulta de tiendas y productos
 * sobre la redis
 *
 */
public class Redis {

    private static Redis instance = null;
    private Jedis jedis;


    /**
     * Constructor privado: Singleton. Crea la conexión con al redis
     */
    private Redis() {
        jedis = new Jedis("localhost");
    }

    /**
     * Singleton
     *
     * @return devuelve la única instancia de la clase
     */
    public static Redis getInstance() {
        if (instance == null)
            instance = new Redis();
        return instance;
    }

    /**
     * Cierra la conexion a la redis
     */
    public void close() {
        if (jedis != null)
            jedis.close();
        jedis = null;
        instance = null;
    }

    public Map<String,Object> enrich(Map<String, Object> inEvent,int prenda){
        //Id de la tienda para buscarlo en el redis.
        int id_Tienda = (int) inEvent.get("id_Tienda");
        String tiendastr = Integer.toString(id_Tienda);
        //Array donde se guardan las prendas del ticket si hay mas de una.
        ArrayList id_prenda = (ArrayList) inEvent.get("Prendas");
        //Formato decimal para aplicar a los precios.
        DecimalFormat df = new DecimalFormat("#.##");

        //Cogemos del Redis los datos de la tienda en el Map tienda.
        Map<String,String> tienda = jedis.hgetAll(tiendastr);
        //Insertamos en el inEvent los datos de la tienda.
        inEvent.put("cadena",tienda.get("cadena"));
        inEvent.put("sexo",tienda.get("sexo"));
        inEvent.put("pais",tienda.get("pais"));
        inEvent.put("region",tienda.get("region"));
        inEvent.put("zona",tienda.get("zona"));


        Double total = 0.00;
        //Recorremos la lista de las prendas compradas y añadimos el total del precio del Ticket si es la primera prenda del ticket.

        if (prenda == 0){

            for (int i = 0 ; i<id_prenda.size(); i++) {
                //Cogemos la prenda y el precio del array
                String prendaprecio = (String) id_prenda.get(i);
                //los separamos
                String[] prepre = prendaprecio.split(":");
                //cogemos el precio
                String precioStr = prepre[1];
                //lo parseamos y lo sumamos al total.
                Double precioint = Double.parseDouble(precioStr);
                total = total + Double.parseDouble(precioStr);
                }
          //Solo ponemos el total y el nº de prendas si es el primer ticket para evitar duplicados en estadisticas.
          total = Double.valueOf(df.format(total));
          //añadimos el numero de prendas compradas, seria el tamaño del array.
          inEvent.put("prendascompradas",id_prenda.size());
          inEvent.put("Total",total);
        }


        //Cogemos los datos de la prenda del Redis.
        String prendaprecio = (String) id_prenda.get(prenda);
        String[] prepre = prendaprecio.split(":");
        Map<String, String> prendaData = jedis.hgetAll(prepre[0]);

        inEvent.put("talla", prendaData.get("talla"));
        inEvent.put("clase",prendaData.get("clase"));
        inEvent.put("modelo",prendaData.get("modelo"));
        inEvent.put("nombre", prendaData.get("nombre"));
        inEvent.put("color",prendaData.get("color"));
        inEvent.put("beneficio",prendaData.get("ben"));
        String precioStr = prepre[1];
        Double precioint = Double.parseDouble(precioStr);
        inEvent.put("precio",Double.valueOf(df.format(precioint)));

        return inEvent;

    }
}

