#!/bin/bash

wrk_path=$1
host=$2
t=$3
d=$4
c=$5

$wrk_path/wrk/wrk -t$t -c$c -d$d http://$host/hello
$wrk_path/wrk/wrk -t$t -c$c -d$d http://$host/sleep/0.1
$wrk_path/wrk/wrk -t$t -c$c -d$d http://$host/sleep/0.5
$wrk_path/wrk/wrk -t$t -c$c -d$d http://$host/sleep/1.0
$wrk_path/wrk/wrk -t$t -c$c -d$d http://$host/estimate-pi/100
$wrk_path/wrk/wrk -t$t -c$c -d$d http://$host/estimate-pi/1000
$wrk_path/wrk/wrk -t$t -c$c -d$d http://$host/estimate-pi/10000
$wrk_path/wrk/wrk -t$t -c$c -d$d http://$host/estimate-pi/100000
$wrk_path/wrk/wrk -t$t -c$c -d$d http://$host/estimate-pi/1000000