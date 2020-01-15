#!/bin/bash
#This File is PM2.5/PM10 creation script

today=`date +%Y%m%d`

java -jar -Duser.timezone=GMT+8 /home/ubuntu/tmp/Java/CreatePM10Table.jar > /home/ubuntu/tmp/log/CreationPM10_$today.log 

java -jar -Duser.timezone=GMT+8 /home/ubuntu/tmp/Java/CreatePM25Table.jar > /home/ubuntu/tmp/log/CreationPM25_$today.log 