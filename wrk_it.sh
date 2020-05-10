#!/bin/bash

t=$1
c=$2
d=$3
host=$4

end_points=(
  /hello
  /sleep/0.1 /sleep/0.5 /sleep/1.0
  /estimate-pi/100 /estimate-pi/1000 /estimate-pi/10000 /estimate-pi/100000 /estimate-pi/1000000
  /estimate-pi-np/100 /estimate-pi-np/1000 /estimate-pi-np/10000 /estimate-pi-np/100000 /estimate-pi-np/1000000
)

for end_point in "${end_points[@]}"; do

  url=http://$host$end_point

  echo 'Path:'
  echo $end_point
  echo ''
  echo 'Example Output:'
  echo 'curl '$url
  curl $url
  echo ''
  echo 'Wrk Report:'
  wrk -t$t -c$c -d$d $url
  echo ''
  echo ''
  sleep 10
done
