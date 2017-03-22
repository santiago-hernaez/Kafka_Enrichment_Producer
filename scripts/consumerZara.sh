#! /bin/bash

printf "\e]2;Zara\a"

echo "-------------------------- CONSUMER DE Zara --------------------------"
/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic Zara

