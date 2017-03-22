#! /bin/bash
printf "\e]2;Logstash\a"
/opt/logstash-5.2.2/bin/logstash -f /opt/logstash-5.2.2/kafka.json
