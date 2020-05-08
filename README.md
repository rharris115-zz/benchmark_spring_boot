## Client Machine
```cmd
Hardware Overview:

  Model Name:	MacBook Air
  Model Identifier:	MacBookAir4,2
  Processor Name:	Intel Core i5
  Processor Speed:	1.7 GHz
  Number of Processors:	1
  Total Number of Cores:	2
  L2 Cache (per Core):	256 KB
  L3 Cache:	3 MB
  Memory:	4 GB
  Boot ROM Version:	135.0.0.0.0
  SMC Version (system):	1.73f66
  Serial Number (system):	C02GHE1BDJWT
  Hardware UUID:	EC9919EC-AE69-5738-A4E5-C56CB25EB925
```

## Results
### Java Spring Boot and Tomcat
```cmd
Running 2m test @ http://192.168.0.10:8080/hello
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     4.68ms   11.79ms 260.43ms   96.38%
    Req/Sec     3.49k   316.31     4.58k    80.38%
  834574 requests in 2.00m, 120.33MB read
Requests/sec:   6949.50
Transfer/sec:      1.00MB

Running 2m test @ http://192.168.0.10:8080/sleep/0.1
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   111.28ms    3.04ms 119.98ms   79.89%
    Req/Sec    92.84     17.33   101.00     89.42%
  21546 requests in 2.00m, 4.19MB read
Requests/sec:    179.52
Transfer/sec:     35.78KB

Running 2m test @ http://192.168.0.10:8080/sleep/0.5
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   506.37ms    2.12ms 518.04ms   69.79%
    Req/Sec    19.81      6.27    87.00     94.67%
  4720 requests in 2.00m, 0.92MB read
Requests/sec:     39.33
Transfer/sec:      7.84KB

Running 2m test @ http://192.168.0.10:8080/sleep/1.0
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s     1.82ms   1.01s    71.09%
    Req/Sec    10.59     10.53    89.00     97.57%
  2380 requests in 2.00m, 474.27KB read
Requests/sec:     19.83
Transfer/sec:      3.95KB

Running 2m test @ http://192.168.0.10:8080/estimate-pi/100
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     7.45ms   25.03ms 484.02ms   96.99%
    Req/Sec     2.40k   337.58     3.29k    85.14%
  570037 requests in 2.00m, 90.08MB read
Requests/sec:   4749.05
Transfer/sec:    768.46KB

Running 2m test @ http://192.168.0.10:8080/estimate-pi/1000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    10.44ms   22.09ms 382.79ms   94.51%
    Req/Sec     1.66k   155.04     2.42k    72.14%
  396591 requests in 2.00m, 63.46MB read
Requests/sec:   3304.60
Transfer/sec:    541.43KB

Running 2m test @ http://192.168.0.10:8080/estimate-pi/10000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    69.09ms   93.41ms 536.95ms   86.07%
    Req/Sec   264.72     37.08   420.00     69.49%
  63290 requests in 2.00m, 10.19MB read
Requests/sec:    527.03
Transfer/sec:     86.90KB

Running 2m test @ http://192.168.0.10:8080/estimate-pi/100000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   389.91ms   37.19ms 507.44ms   73.28%
    Req/Sec    25.45     11.18    69.00     61.81%
  6148 requests in 2.00m, 1.00MB read
Requests/sec:     51.20
Transfer/sec:      8.49KB

Running 2m test @ http://192.168.0.10:8080/estimate-pi/1000000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.87s    68.70ms   1.97s    50.00%
    Req/Sec     4.29      4.69    30.00     75.89%
  605 requests in 2.00m, 100.81KB read
  Socket errors: connect 0, read 0, write 0, timeout 597
Requests/sec:      5.04
Transfer/sec:     860.00B
```
### Flask and Werkzeug
```cmd
```
### Flask and Gunicon Workers=2
```cmd
```
