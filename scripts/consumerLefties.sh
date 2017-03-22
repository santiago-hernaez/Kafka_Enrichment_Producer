#! /bin/bash
printf "\e]2;Lefties\a"
echo "-------------------------- CONSUMER DE Lefties --------------------------"
/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic Lefties

