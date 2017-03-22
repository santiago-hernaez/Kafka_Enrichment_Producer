#!/bin/bash

echo "**************************ARRANCA ZOOKEEPER**************************"
/opt/zookeeper/bin/zkServer.sh start
echo "**************************ARRANCA KAFKA**************************"
/opt/kafka/bin/kafka-server-start.sh -daemon /opt/kafka/config/server.properties
echo "-"
read -rst 1; timeout=$?
echo "--"
read -rst 1; timeout=$?
echo "---"
read -rst 1; timeout=$?
echo "----"
read -rst 1; timeout=$?
echo "----->OK"
echo "**************************CREAMOS TOPICS**************************"

/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic inditex --partitions 1 --replication-factor 1
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic Zara --partitions 1 --replication-factor 1
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic PullBear --partitions 1 --replication-factor 1
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic MassimoDutti --partitions 1 --replication-factor 1
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic Bershka --partitions 1 --replication-factor 1
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic Stradivarius --partitions 1 --replication-factor 1
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic Uterque --partitions 1 --replication-factor 1
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic Oysho --partitions 1 --replication-factor 1
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic Lefties --partitions 1 --replication-factor 1


echo "**************************ARRANCA TERMINALES Y PRODUCTOR**************************"
gnome-terminal --geometry 200x30+900+0 --tab -e ~/IdeaProjects/projecto/scripts/consumer.sh --tab -e ~/IdeaProjects/projecto/scripts/consumerZara.sh --tab -e ~/IdeaProjects/projecto/scripts/consumerPullBear.sh --tab -e ~/IdeaProjects/projecto/scripts/consumerMassimo.sh --tab -e ~/IdeaProjects/projecto/scripts/consumerBershka.sh --tab -e ~/IdeaProjects/projecto/scripts/consumerStradivarius.sh --tab -e ~/IdeaProjects/projecto/scripts/consumerUterque.sh --tab -e ~/IdeaProjects/projecto/scripts/consumerLefties.sh --tab -e ~/IdeaProjects/projecto/scripts/consumerOysho.sh
# terminator -l Consumer -T CONSUMERS

gnome-terminal --geometry=40x3+9000+9000 -e ~/IdeaProjects/projecto/scripts/redis.sh 
gnome-terminal --geometry=40x3+500+9000 -e ~/IdeaProjects/projecto/scripts/elasticsearch.sh
cont=15
until [ $cont -lt 1 ];
do
     echo Tiempo de arranque de ElasticSearch : $cont
     let cont=cont-1 
     read -rst 1; timeout=$
done
gnome-terminal --geometry=40x3+600+9000 -e ~/IdeaProjects/projecto/scripts/kibana.sh
gnome-terminal --geometry=40x3+700+9000 -e ~/IdeaProjects/projecto/scripts/logstash.sh
gnome-terminal --geometry=40x3+0+9000 -e ~/IdeaProjects/projecto/scripts/javaKafka.sh
java -jar /home/sam/synthetic-producer-master/synthetic-producer-1.4.1-SNAPSHOT-selfcontained.jar -z localhost:2181 -c ~/IdeaProjects/projecto/scripts/inditex.yml -r 30 -t 1

echo "**************************APAGA REDIS y MATA KAFKA**************************"
echo "Mata Kafka"
jps | grep Kafka | cut -d " " -f "1" | xargs kill -KILL
echo "Mata Terminales"
jps | grep ConsoleConsumer | cut -d " " -f "1" | xargs kill -KILL
jps | grep kafka | cut -d " " -f "1" | xargs kill -KILL
jps | grep Elastic | cut -d " " -f "1" | xargs kill -KILL
ps -ef | grep kibana | cut -d " " -f "7" | xargs kill -KILL
ps -ef | grep logstash | cut -d " " -f "7" | xargs kill -KILL
echo "**************************Cierra Redis correctamente**************************"
/opt/redis/src/redis-cli shutdown
echo "**************************Eliminamos Topics**************************"
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic inditex
#/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic enrichInditex
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic Zara
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic PullBear
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic MassimoDutti
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic Bershka
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic Stradivarius
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic Uterque
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic Oysho
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic Lefties

echo "**************************ELIMINAMOS LOGS ANTIGUOS**************************"

rm -r -f /tmp/kafka-logs
rm -r -f /tmp/zookeeper/version-2

read -rst 0.3; timeout=$?


echo "**************************Cierra Zookeeper**************************"
/opt/zookeeper/bin/zkServer.sh stop
echo "-+-+-+-+-+-+-+-+-+-+-+-+-+TODO FINALIZADO OK-+-+-+-+-+-+-+-+-+-+-+-+-+"
