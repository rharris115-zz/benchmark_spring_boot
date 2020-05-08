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
#### Hello World
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:8080/hello
Running 1m test @ http://192.168.0.10:8080/hello
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    34.61ms   10.55ms 363.42ms   79.29%
    Req/Sec     3.64k   292.15     4.63k    78.42%
  435249 requests in 1.00m, 62.75MB read
  Socket errors: connect 149, read 114, write 9, timeout 0
Requests/sec:   7249.20
Transfer/sec:      1.05MB
```
#### Estimate Pi N=100
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:8080/estimate-pi/100
Running 1m test @ http://192.168.0.10:8080/estimate-pi/100
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    53.18ms   16.86ms 407.55ms   81.46%
    Req/Sec     2.37k   464.03     3.55k    75.04%
  283041 requests in 1.00m, 44.94MB read
  Socket errors: connect 149, read 39, write 7, timeout 0
Requests/sec:   4714.85
Transfer/sec:    766.51KB
```
#### Estimate Pi N=1000
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:8080/estimate-pi/1000
Running 1m test @ http://192.168.0.10:8080/estimate-pi/1000
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    67.30ms   18.86ms 485.32ms   78.39%
    Req/Sec     1.87k   190.26     2.47k    71.25%
  224007 requests in 1.00m, 35.85MB read
  Socket errors: connect 149, read 39, write 2, timeout 0
Requests/sec:   3727.66
Transfer/sec:    610.92KB
```
#### Estimate Pi N=10000
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:8080/estimate-pi/10000
Running 1m test @ http://192.168.0.10:8080/estimate-pi/10000
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   430.27ms   61.73ms 996.48ms   96.28%
    Req/Sec   291.85     38.73   444.00     67.78%
  34922 requests in 1.00m, 5.62MB read
  Socket errors: connect 149, read 52, write 12, timeout 0
Requests/sec:    581.03
Transfer/sec:     95.77KB
```
#### Estimate Pi N=100000
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:8080/estimate-pi/100000
Running 1m test @ http://192.168.0.10:8080/estimate-pi/100000
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.05s   564.57ms   1.97s    57.52%
    Req/Sec    30.48     17.13    90.00     57.92%
  3457 requests in 1.00m, 572.70KB read
  Socket errors: connect 149, read 11, write 0, timeout 3344
Requests/sec:     57.56
Transfer/sec:      9.54KB
```
#### Estimate Pi N=1000000
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:8080/estimate-pi/1000000
Running 1m test @ http://192.168.0.10:8080/estimate-pi/1000000
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.39s    91.82ms   1.57s    62.50%
    Req/Sec     7.06      7.35    39.00     83.60%
  344 requests in 1.00m, 57.60KB read
  Socket errors: connect 149, read 35, write 0, timeout 336
Requests/sec:      5.73
Transfer/sec:      0.96KB
```
### Flask and Werkzeug
#### Hello World
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:5000/hello
Running 1m test @ http://192.168.0.10:5000/hello
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   229.08ms   25.31ms 297.84ms   92.87%
    Req/Sec   268.91     74.78   474.00     73.72%
  22481 requests in 1.00m, 3.73MB read
  Socket errors: connect 149, read 3228, write 64, timeout 0
Requests/sec:    374.52
Transfer/sec:     63.64KB
```
#### Estimate Pi N=100
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:5000/estimate-pi/100
Running 1m test @ http://192.168.0.10:5000/estimate-pi/100
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   232.55ms   65.03ms 357.21ms   87.58%
    Req/Sec   238.36     68.11   470.00     83.25%
  23305 requests in 1.00m, 4.48MB read
  Socket errors: connect 149, read 3312, write 96, timeout 0
Requests/sec:    387.76
Transfer/sec:     76.34KB
```
#### Numpy Estimate Pi N=100
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:5000/estimate-pi-np/100
Running 1m test @ http://192.168.0.10:5000/estimate-pi-np/100
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   251.96ms   69.86ms 402.04ms   89.94%
    Req/Sec   228.12     63.30   520.00     82.75%
  23619 requests in 1.00m, 4.56MB read
  Socket errors: connect 149, read 3277, write 200, timeout 0
Requests/sec:    393.03
Transfer/sec:     77.63KB
```
#### Estimate Pi N=1000
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:5000/estimate-pi/1000
Running 1m test @ http://192.168.0.10:5000/estimate-pi/1000
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   321.56ms   44.80ms 383.08ms   90.96%
    Req/Sec   188.53     33.95   280.00     74.41%
  22372 requests in 1.00m, 4.32MB read
  Socket errors: connect 149, read 3202, write 100, timeout 0
Requests/sec:    372.33
Transfer/sec:     73.69KB
```
#### Numpy Estimate Pi N=1000
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:5000/estimate-pi-np/1000
Running 1m test @ http://192.168.0.10:5000/estimate-pi-np/1000
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   273.90ms   34.08ms 397.81ms   92.63%
    Req/Sec   224.69     55.66   480.00     79.42%
  21670 requests in 1.00m, 4.20MB read
  Socket errors: connect 149, read 3288, write 154, timeout 0
Requests/sec:    360.82
Transfer/sec:     71.56KB
```
#### Estimate Pi N=10000
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:5000/estimate-pi/10000
Running 1m test @ http://192.168.0.10:5000/estimate-pi/10000
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.08s    83.90ms   1.27s    98.14%
    Req/Sec    59.24     17.89   121.00     59.56%
  7104 requests in 1.00m, 1.37MB read
  Socket errors: connect 149, read 910, write 75, timeout 0
Requests/sec:    118.28
Transfer/sec:     23.43KB
```
#### Numpy Estimate Pi N=10000
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:5000/estimate-pi-np/10000
Running 1m test @ http://192.168.0.10:5000/estimate-pi-np/10000
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   287.51ms   81.50ms 367.70ms   88.60%
    Req/Sec   201.42     39.48   300.00     76.92%
  20997 requests in 1.00m, 4.07MB read
  Socket errors: connect 149, read 3529, write 106, timeout 0
Requests/sec:    349.46
Transfer/sec:     69.41KB
```
#### Estimate Pi N=100000
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:5000/estimate-pi/100000
Running 1m test @ http://192.168.0.10:5000/estimate-pi/100000
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.59s   394.35ms   2.00s    57.14%
    Req/Sec     9.07      5.98    49.00     72.31%
  905 requests in 1.00m, 178.92KB read
  Socket errors: connect 149, read 316, write 94, timeout 898
Requests/sec:     15.06
Transfer/sec:      2.98KB
```
#### Numpy Estimate Pi N=100000
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:5000/estimate-pi-np/100000
Running 1m test @ http://192.168.0.10:5000/estimate-pi-np/100000
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.32s   177.56ms   2.00s    91.88%
    Req/Sec    93.48     30.03   191.00     66.92%
  11161 requests in 1.00m, 2.14MB read
  Socket errors: connect 149, read 146, write 4, timeout 136
Requests/sec:    185.70
Transfer/sec:     36.54KB
```
#### Estimate Pi N=1000000
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:5000/estimate-pi/1000000
Running 1m test @ http://192.168.0.10:5000/estimate-pi/1000000
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     0.00us    0.00us   0.00us     nan%
    Req/Sec     1.29      2.07    10.00     91.67%
  76 requests in 1.00m, 15.02KB read
  Socket errors: connect 149, read 277, write 58, timeout 76
Requests/sec:      1.27
Transfer/sec:     256.02B
```
#### Numpy Estimate Pi N=1000000
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:5000/estimate-pi-np/1000000
Running 1m test @ http://192.168.0.10:5000/estimate-pi-np/1000000
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.26s   420.79ms   1.68s    66.67%
    Req/Sec    13.49      7.48    40.00     80.82%
  1011 requests in 1.00m, 199.90KB read
  Socket errors: connect 149, read 205, write 40, timeout 1008
Requests/sec:     16.82
Transfer/sec:      3.33KB
```
### Flask and Gunicon Workers=2
#### Hello World
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:8000/hello
Running 1m test @ http://192.168.0.10:8000/hello
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   128.81ms   91.67ms 431.90ms   54.84%
    Req/Sec   306.79    117.36   575.00     68.77%
  29042 requests in 1.00m, 4.99MB read
  Socket errors: connect 299, read 1891, write 109, timeout 0
Requests/sec:    483.56
Transfer/sec:     85.00KB
```
#### Estimate Pi N=100
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:8000/estimate-pi/100
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   157.15ms   91.13ms 401.38ms   59.50%
    Req/Sec   300.94    111.96   770.00     67.25%
  25942 requests in 1.00m, 5.14MB read
  Socket errors: connect 295, read 2136, write 41, timeout 0
Requests/sec:    432.03
Transfer/sec:     87.66KB
```
#### Numpy Estimate Pi N=100
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:8000/estimate-pi-np/100
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   169.14ms  102.28ms 402.58ms   57.65%
    Req/Sec   279.12    109.35   565.00     69.44%
  24293 requests in 1.00m, 4.83MB read
  Socket errors: connect 296, read 2443, write 85, timeout 0
Requests/sec:    404.51
Transfer/sec:     82.29KB
```
#### Estimate Pi N=1000
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:8000/estimate-pi/1000
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   194.08ms  124.70ms 512.73ms   55.68%
    Req/Sec   236.64     95.58   580.00     66.17%
  23971 requests in 1.00m, 4.77MB read
  Socket errors: connect 312, read 2637, write 20, timeout 0
Requests/sec:    398.90
Transfer/sec:     81.27KB
```
#### Numpy Estimate Pi N=1000
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:8000/estimate-pi-np/1000
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   218.76ms   70.56ms 536.34ms   66.64%
    Req/Sec   273.33    110.71   780.00     74.26%
  27349 requests in 1.00m, 5.46MB read
  Socket errors: connect 230, read 2436, write 103, timeout 0
Requests/sec:    455.47
Transfer/sec:     93.05KB
```
#### Estimate Pi N=10000
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:8000/estimate-pi/10000
Running 1m test @ http://192.168.0.10:8000/estimate-pi/10000
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   666.37ms   79.25ms   1.15s    89.92%
    Req/Sec    97.30     28.02   202.00     67.94%
  11586 requests in 1.00m, 2.31MB read
  Socket errors: connect 149, read 3367, write 221, timeout 0
Requests/sec:    192.77
Transfer/sec:     39.30KB
```
#### Numpy Estimate Pi N=10000
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:8000/estimate-pi-np/10000
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/10000
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   260.67ms  101.29ms 590.78ms   69.84%
    Req/Sec   218.76     90.55   646.00     71.80%
  22968 requests in 1.00m, 4.59MB read
  Socket errors: connect 216, read 2654, write 112, timeout 0
Requests/sec:    382.70
Transfer/sec:     78.34KB
```
#### Estimate Pi N=100000
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:8000/estimate-pi/100000
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100000
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.03s   596.49ms   1.99s    54.90%
    Req/Sec    13.23      7.12    40.00     82.24%
  1498 requests in 1.00m, 305.30KB read
  Socket errors: connect 149, read 404, write 102, timeout 1447
Requests/sec:     24.93
Transfer/sec:      5.08KB
```
#### Numpy Estimate Pi N=100000
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:8000/estimate-pi-np/100000
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100000
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   776.23ms   87.18ms   1.03s    80.85%
    Req/Sec    83.44     30.91   188.00     66.30%
  9932 requests in 1.00m, 1.99MB read
  Socket errors: connect 149, read 2435, write 59, timeout 0
Requests/sec:    165.48
Transfer/sec:     33.90KB
```
#### Estimate Pi N=1000000
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:8000/estimate-pi/1000000
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000000
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.09s   474.40ms   1.52s   100.00%
    Req/Sec     1.22      1.44    10.00     91.22%
  150 requests in 1.00m, 30.57KB read
  Socket errors: connect 149, read 399, write 19, timeout 146
Requests/sec:      2.50
Transfer/sec:     521.33B
```
#### Numpy Estimate Pi N=1000000
```cmd
Richards-MacBook-Air:wrk rich$ ./wrk -t2 -c400 -d60s http://192.168.0.10:8000/estimate-pi-np/1000000
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000000
  2 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     0.00us    0.00us   0.00us     nan%
    Req/Sec    10.57      5.58    30.00     59.02%
  506 requests in 1.00m, 103.56KB read
  Socket errors: connect 149, read 583, write 196, timeout 506
Requests/sec:      8.42
Transfer/sec:      1.72KB
```
