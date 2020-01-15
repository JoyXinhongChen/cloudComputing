#!/bin/bash
#This File is PM2.5/PM10 JSON data to store in S3 

today=`date`

java -jar -Duser.timezone=GMT+8 /home/ubuntu/tmp/Java/PM10DataCountToS3.jar > /home/ubuntu/tmp/log/JsonS3PM10_$today.log 

java -jar -Duser.timezone=GMT+8 /home/ubuntu/tmp/Java/PM25DataCountToS3.jar > /home/ubuntu/tmp/log/JsonS3PM25_$today.log 