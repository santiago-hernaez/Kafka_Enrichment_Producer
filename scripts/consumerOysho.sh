#! /bin/bash
printf "\e]2;Oysho\a"
echo "-------------------------- CONSUMER DE Oysho --------------------------"
/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic Oysho

