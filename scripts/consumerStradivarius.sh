#! /bin/bash
printf "\e]2;Stradivarius\a"
echo "-------------------------- CONSUMER DE Stradivarius --------------------------"
/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic Stradivarius

