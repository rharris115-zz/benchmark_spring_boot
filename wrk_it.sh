#!/bin/bash

wrk_path=$1
host=$2
t=$3
d=$4
c=$5

end_points=(/hello
  /sleep/0.1 /sleep/0.5 /sleep/1.0
  /estimate-pi/100 /estimate-pi/1000 /estimate-pi/10000 /estimate-pi/100000 /estimate-pi/1000000
  /estimate-pi-np/100 /estimate-pi-np/1000 /estimate-pi-np/10000 /estimate-pi-np/100000 /estimate-pi-np/1000000)

for end_point in "${end_points[@]}"; do
  $wrk_path/wrk/wrk -t$t -c$c -d$d http://$host$end_point
  echo ''
  sleep 10
done
