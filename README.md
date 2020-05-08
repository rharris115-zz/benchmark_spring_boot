# Services
## Java Spring Boot and Tomcat
The service was implemented to enable asynchronous operation. Other implementation details were left to default settings.
## Python Flask and Werkzeug
The default development service of Flask.
## Python Flask and Gunicorn
The server was run with `(2 * N(core)) + 1` workers as suggested by Gunicorn's documentation. Additionally, two threads per worker were specified.
# End Points
## "/hello"
A basic "Hello World" endpoint.
### Python
```python
def get(self):
    return {'message': 'Hello World!'}
```
### Java
```java
public Map<String, String> greeting() {
    return Map.of("message", "Hello World!");
}
```
## "/sleep/{t}"
### Python
```python
def get(self, t: float):
    sleep(t)
    return {'slept': t}
```
### Java
```java
public Map<String, ?> sleep(final Float t) throws InterruptedException {
    Thread.sleep(Math.round(t * 1000));
    return Map.of("slept", t);
}
```
## "/estimate-pi/{n}"
### Python
```python
def get(self, n: int):
    return {'estimatedPi': sum(4 if random() ** 2 + random() ** 2 < 1 else 0
                               for _ in range(n)) / n}
```
### Java
```java
public Future<Double> estimatePiImpl(final int n) {

    final double estimatedPi = IntStream.generate(() -> {
        double x = rnd.nextDouble();
        double y = rnd.nextDouble();
        return (x * x + y * y) < 1 ? 4 : 0; // Don't need to multiply by 4 later.
    })
            .limit(n)
            .average()
            .orElse(Double.NaN);

    return new AsyncResult<>(estimatedPi);
}
```
## "/estimate-pi-np/{n}"
### Python
```python
def get(self, n: int):
    return {'estimatedPi': 4 * ((np.random.uniform(size=n) ** 2
                                 + np.random.uniform(size=n) ** 2) < 1).mean()}
```

# Summarised Results
## Hello World

| Requests/sec:           | /hello |
| ----------------------- | ------ |
| Spring Boost and Tomcat | 6949.5 |
| Flask and Werkzeug      | 408.3  |
| Flask and Gunicorn      | 767.85 |

## Sleep

| Requests/sec:           | /sleep/0.1 | /sleep/0.5 | /sleep/1.0 |
| ----------------------- | ---------- | ---------- | ---------- |
| Spring Boost and Tomcat | 179.52     | 39.33      | 19.83      |
| Flask and Werkzeug      | 183.24     | 39.18      | 19.7       |
| Flask and Gunicorn      | 37.58      | 7.9        | 3.96       |

### Estimate Pi

| Requests/sec:           | /estimate-pi/100    | /estimate-pi/1000    | /estimate-pi/10000    | /estimate-pi/100000    | /estimate-pi/1000000    |
| ----------------------- | ------------------- | -------------------- | --------------------- | ---------------------- | ----------------------- |
| Spring Boost and Tomcat | 4749.05             | 3304.6               | 527.03                | 51.2                   | 5.04                    |
| Flask and Werkzeug      | 390.09              | 378.25               | 120.45                | 15.46                  | 1.5                     |
| Flask and Gunicorn      | 747.37              | 614.02               | 221.58                | 30.94                  | 3.22                    |
|                         |                     |                      |                       |                        |                         |
| Requests/sec:           | /estimate-pi-np/100 | /estimate-pi-np/1000 | /estimate-pi-np/10000 | /estimate-pi-np/100000 | /estimate-pi-np/1000000 |
| Flask and Werkzeug      | 364.54              | 368.24               | 373.19                | 193.96                 | 20.98                   |
| Flask and Gunicorn      | 710.01              | 689.33               | 566.83                | 208.96                 | 20.64                   |
# Server Machine
```cmd
Hardware Overview:

  Model Name:	Mac mini
  Model Identifier:	Macmini4,1
  Processor Name:	Intel Core 2 Duo
  Processor Speed:	2.4 GHz
  Number of Processors:	1
  Total Number of Cores:	2
  L2 Cache:	3 MB
  Memory:	8 GB
  Boot ROM Version:	76.0.0.0.0
  SMC Version (system):	1.65f2
  Serial Number (system):	C07D9236DD6H
  Hardware UUID:	28058D27-E169-59CF-86BC-A2305236CC80
```
# Client Machine
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
Running 2m test @ http://192.168.0.10:5000/hello
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    33.35ms    7.19ms 113.78ms   94.91%
    Req/Sec   293.86     38.93   404.00     91.14%
  49015 requests in 2.00m, 8.13MB read
Requests/sec:    408.30
Transfer/sec:     69.38KB

Running 2m test @ http://192.168.0.10:5000/sleep/0.1
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   108.26ms    3.63ms 167.01ms   75.44%
    Req/Sec    91.96     12.84   101.00     87.05%
  22007 requests in 2.00m, 4.05MB read
Requests/sec:    183.24
Transfer/sec:     34.51KB

Running 2m test @ http://192.168.0.10:5000/sleep/0.5
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   508.33ms    3.45ms 545.95ms   77.50%
    Req/Sec    27.68     23.73    90.00     71.82%
  4703 requests in 2.00m, 0.86MB read
Requests/sec:     39.18
Transfer/sec:      7.34KB

Running 2m test @ http://192.168.0.10:5000/sleep/1.0
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s     4.02ms   1.03s    73.01%
    Req/Sec    24.30     22.17    90.00     70.18%
  2364 requests in 2.00m, 442.68KB read
Requests/sec:     19.70
Transfer/sec:      3.69KB

Running 2m test @ http://192.168.0.10:5000/estimate-pi/100
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    32.42ms    7.70ms  93.63ms   86.30%
    Req/Sec   254.27     76.90   480.00     84.30%
  46845 requests in 2.00m, 9.01MB read
Requests/sec:    390.09
Transfer/sec:     76.84KB

Running 2m test @ http://192.168.0.10:5000/estimate-pi/1000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    47.10ms    7.65ms 105.20ms   95.52%
    Req/Sec   209.48     24.99   270.00     90.51%
  45419 requests in 2.00m, 8.78MB read
  Socket errors: connect 0, read 1, write 0, timeout 0
Requests/sec:    378.25
Transfer/sec:     74.84KB

Running 2m test @ http://192.168.0.10:5000/estimate-pi/10000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   160.25ms    8.25ms 235.09ms   92.31%
    Req/Sec    61.96     10.98    90.00     62.13%
  14459 requests in 2.00m, 2.80MB read
Requests/sec:    120.45
Transfer/sec:     23.86KB

Running 2m test @ http://192.168.0.10:5000/estimate-pi/100000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.29s   118.57ms   1.70s    75.07%
    Req/Sec     9.23      6.05    40.00     71.28%
  1857 requests in 2.00m, 367.01KB read
Requests/sec:     15.46
Transfer/sec:      3.06KB

Running 2m test @ http://192.168.0.10:5000/estimate-pi/1000000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     0.00us    0.00us   0.00us     nan%
    Req/Sec     1.82      2.95    20.00     90.17%
  180 requests in 2.00m, 35.58KB read
  Socket errors: connect 0, read 0, write 0, timeout 180
Requests/sec:      1.50
Transfer/sec:     303.41B

Running 2m test @ http://192.168.0.10:5000/estimate-pi-np/100
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    43.59ms   67.90ms   1.95s    99.27%
    Req/Sec   249.57     45.13   300.00     92.25%
  43759 requests in 2.00m, 8.44MB read
  Socket errors: connect 0, read 0, write 0, timeout 38
Requests/sec:    364.54
Transfer/sec:     71.97KB

Running 2m test @ http://192.168.0.10:5000/estimate-pi-np/1000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    40.25ms   15.78ms 420.13ms   92.83%
    Req/Sec   235.45     45.47   343.00     87.55%
  44209 requests in 2.00m, 8.54MB read
Requests/sec:    368.24
Transfer/sec:     72.88KB

Running 2m test @ http://192.168.0.10:5000/estimate-pi-np/10000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    45.21ms    8.52ms 140.18ms   91.15%
    Req/Sec   217.46     30.02   320.00     85.53%
  44800 requests in 2.00m, 8.68MB read
Requests/sec:    373.19
Transfer/sec:     74.01KB

Running 2m test @ http://192.168.0.10:5000/estimate-pi-np/100000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   102.38ms   11.68ms 239.82ms   92.44%
    Req/Sec    97.39      7.80   120.00     81.30%
  23286 requests in 2.00m, 4.50MB read
Requests/sec:    193.96
Transfer/sec:     38.38KB

Running 2m test @ http://192.168.0.10:5000/estimate-pi-np/1000000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   948.85ms   44.05ms   1.52s    97.18%
    Req/Sec    11.85      7.37    40.00     65.71%
  2519 requests in 2.00m, 498.48KB read
Requests/sec:     20.98
Transfer/sec:      4.15KB
```
### Flask and Gunicorn
Gunicorn suggests to use (2 * cores) + 1 workers. For our 2 cores, this gives 5.
```cmd
gunicorn --bind '0.0.0.0:8080' --workers=5 --threads=2 'benchmark:app'
```
```cmd
Running 2m test @ http://192.168.0.10:8000/hello
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    28.92ms   20.90ms 308.10ms   92.51%
    Req/Sec   385.66     67.37   540.00     68.36%
  92171 requests in 2.00m, 16.26MB read
Requests/sec:    767.85
Transfer/sec:    138.73KB

Running 2m test @ http://192.168.0.10:8000/sleep/0.1
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   530.81ms  270.72ms 863.54ms   50.02%
    Req/Sec    18.94      8.65    40.00     39.04%
  4512 requests in 2.00m, 0.88MB read
Requests/sec:     37.58
Transfer/sec:      7.48KB

Running 2m test @ http://192.168.0.10:8000/sleep/0.5
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.02s   431.43ms   1.52s    33.33%
    Req/Sec     3.15      1.56    10.00     60.54%
  948 requests in 2.00m, 187.85KB read
  Socket errors: connect 0, read 0, write 0, timeout 936
Requests/sec:      7.90
Transfer/sec:      1.56KB

Running 2m test @ http://192.168.0.10:8000/sleep/1.0
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s     1.34ms   1.01s   100.00%
    Req/Sec     1.41      2.02    19.00     96.36%
  476 requests in 2.00m, 94.25KB read
  Socket errors: connect 0, read 0, write 0, timeout 472
Requests/sec:      3.96
Transfer/sec:     803.79B

Running 2m test @ http://192.168.0.10:8000/estimate-pi/100
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    29.43ms   19.64ms 252.24ms   93.07%
    Req/Sec   375.36     31.12   484.00     70.03%
  89725 requests in 2.00m, 18.21MB read
Requests/sec:    747.37
Transfer/sec:    155.30KB

Running 2m test @ http://192.168.0.10:8000/estimate-pi/1000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    35.18ms   21.05ms 280.86ms   92.60%
    Req/Sec   308.51     26.40   444.00     67.77%
  73739 requests in 2.00m, 15.02MB read
Requests/sec:    614.02
Transfer/sec:    128.12KB

Running 2m test @ http://192.168.0.10:8000/estimate-pi/10000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    90.88ms   27.91ms 382.28ms   89.48%
    Req/Sec   111.17     13.03   151.00     74.97%
  26604 requests in 2.00m, 5.42MB read
Requests/sec:    221.58
Transfer/sec:     46.26KB

Running 2m test @ http://192.168.0.10:8000/estimate-pi/100000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   643.96ms  367.44ms   1.10s    68.61%
    Req/Sec    16.03      8.46    50.00     77.47%
  3715 requests in 2.00m, 774.70KB read
Requests/sec:     30.94
Transfer/sec:      6.45KB

Running 2m test @ http://192.168.0.10:8000/estimate-pi/1000000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.28s    24.92ms   1.31s    75.00%
    Req/Sec     2.20      3.18    29.00     90.83%
  387 requests in 2.00m, 80.68KB read
  Socket errors: connect 0, read 0, write 0, timeout 383
Requests/sec:      3.22
Transfer/sec:     688.04B

Running 2m test @ http://192.168.0.10:8000/estimate-pi-np/100
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    30.75ms   19.93ms 273.93ms   92.91%
    Req/Sec   356.65     32.10   460.00     66.51%
  85252 requests in 2.00m, 17.34MB read
Requests/sec:    710.01
Transfer/sec:    147.91KB

Running 2m test @ http://192.168.0.10:8000/estimate-pi-np/1000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    51.48ms   65.01ms 524.64ms   85.04%
    Req/Sec   347.63    123.30   750.00     74.81%
  82777 requests in 2.00m, 16.91MB read
Requests/sec:    689.33
Transfer/sec:    144.18KB

Running 2m test @ http://192.168.0.10:8000/estimate-pi-np/10000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    48.00ms   50.24ms 450.58ms   84.75%
    Req/Sec   284.70     81.72   464.00     57.79%
  68027 requests in 2.00m, 13.92MB read
Requests/sec:    566.83
Transfer/sec:    118.74KB

Running 2m test @ http://192.168.0.10:8000/estimate-pi-np/100000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    95.68ms   17.93ms 353.99ms   80.08%
    Req/Sec   104.85     11.14   141.00     65.65%
  25079 requests in 2.00m, 5.13MB read
Requests/sec:    208.96
Transfer/sec:     43.79KB

Running 2m test @ http://192.168.0.10:8000/estimate-pi-np/1000000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   965.48ms   45.79ms   1.04s    98.55%
    Req/Sec    10.69      5.08    30.00     65.13%
  2477 requests in 2.00m, 519.12KB read
Requests/sec:     20.64
Transfer/sec:      4.32KB
```
