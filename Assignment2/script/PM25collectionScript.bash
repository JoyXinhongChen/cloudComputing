#!/bin/bash
#This File is PM2.5 PM10 collection script

today=`date +%Y%m%d%H`

java -jar -Duser.timezone=GMT+8 /home/ubuntu/tmp/Java/PM25LoadData.jar > /home/ubuntu/tmp/log/PM25_$today.log

java -jar -Duser.timezone=GMT+8 /home/ubuntu/tmp/Java/PM10LoadData.jar > /home/ubuntu/tmp/log/PM10_$today.log
