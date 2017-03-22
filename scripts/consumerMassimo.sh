#! /bin/bash
printf "\e]2;Massimo Dutti\a"
echo "-------------------------- CONSUMER DE MassimoDutti --------------------------"
/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic MassimoDutti

