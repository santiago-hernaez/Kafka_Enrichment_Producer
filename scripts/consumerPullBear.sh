#! /bin/bash
printf "\e]2;PullBear\a"



echo "-------------------------- CONSUMER DE PullBear --------------------------"


/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic PullBear

