#! /bin/bash
printf "\e]2;Bershka\a"
echo "-------------------------- CONSUMER DE Bershka --------------------------"
/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic Bershka

