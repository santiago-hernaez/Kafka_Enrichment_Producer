#! /bin/bash
printf "\e]2;Uterque\a"
echo "-------------------------- CONSUMER DE Uterque --------------------------"
/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic Uterque

