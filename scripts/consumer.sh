#! /bin/bash
printf "\e]2;Inditex\a"
echo "-------------------------- CONSUMER DE inditex --------------------------"
/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic inditex

