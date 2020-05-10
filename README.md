# Evaluation
Endpoints and services were evaluated with the HTTP benchmarking tool [WRK](https://github.com/wg/wrk/blob/master/README.md).
Given the limitations of the machinery used in this evaluation, all tests were done with 2 requesting threads a single 
connection each. Each endpoint was tested for 120 seconds.

The requests and responses were over a home LAN with Ethernet cable connections and other traffic minimised.
# Services
Three service configurations were used for the evaluation. An asynchronous Java service was provided as a basis
of comparison with the two Python services.
## Java Spring Boot and Tomcat
The service was implemented to enable asynchronous operation. Other implementation details were left as default.
## Python Flask and Werkzeug
The default development service of Flask with debug disabled.
## Python Flask and Gunicorn
Gunicorn was run with three different settings.
- A single worker thread.
- `(2 * N(core)) + 1 = 5` workers with a single thread each.
- `(2 * N(core)) + 1 = 5` workers with two threads each.
# End Points
## `/hello`
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
## `/sleep/{t}`
An endpoint that sleeps for a specified number of seconds and returns this number of seconds. This seems to be a reasonable
approximation of I/O bound services.

Three times were chosen, 0.1, 0.5, and 1.0 seconds.

### Python
```python
def get(self, t: float):
    sleep(t)
    return {'slept': t}
```
### Java
```java
public Future<Void> sleep(final float t) throws InterruptedException {
    Thread.sleep(Math.round(t * 1000));
    return new AsyncResult<>(null);
}
```
## `/estimate-pi/{n}`
A service that estimates the constant Pi by sampling uniformly distributed points on the unit square. The proportion that
are within 1 unit of Euclidean distance to the origin will be roughly Pi/4. In Python and Java, no attempt is made to accelerate
this calculation with more native, efficient code.

Five sample counts `n` were chosen, 100, 1000, 10000, 100000, and 1000000.

### Python
```python
def get(self, n: int):
    return {'estimatedPi': sum(4 if random() ** 2 + random() ** 2 < 1 else 0
                               for _ in range(n)) / n}
```
### Java
```java
    public Future<Double> estimatePiImpl(final int n) {

        final double estimatedPi =
                IntStream
                        .generate(() -> (Math.pow(rnd.nextDouble(), 2) + Math.pow(rnd.nextDouble(), 2) < 1 ? 4 : 0))
                        .limit(n)
                        .average()
                        .orElse(Double.NaN);

        return new AsyncResult<>(estimatedPi);
    }
```
## `/estimate-pi-np/{n}`
A special Python endpoint for estimating Pi that uses numpy vector operations. There's no equivalent endpoint implemented in Java.

Again, five sample counts `n` were chosen, 100, 1000, 10000, 100000, and 1000000.

### Python
```python
def get(self, n: int):
    return {'estimatedPi': 4 * ((np.random.uniform(size=n) ** 2
                                 + np.random.uniform(size=n) ** 2) < 1).mean()}
```
# Hardware
## The Server
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

## The Client
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

# Summarised Results
## Hello World


## Sleep


## Estimate Pi


## Raw Results
### Java Spring Boot and Tomcat
#### 10 Connections
```cmd
Path:
/hello

Example Output:
curl http://192.168.0.10:8080/hello
{"message":"Hello World!"}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/hello
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.71ms    7.68ms 191.20ms   98.28%
    Req/Sec     2.56k   458.33     3.40k    74.00%
  306067 requests in 1.00m, 44.13MB read
Requests/sec:   5099.84
Transfer/sec:    752.97KB


Path:
/sleep/0.1

Example Output:
curl http://192.168.0.10:8080/sleep/0.1
{"elapsed":0.103186752,"slept":0.1}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/sleep/0.1
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   130.54ms   25.43ms 214.77ms   70.43%
    Req/Sec    37.97      7.47    50.00     57.92%
  4593 requests in 1.00m, 0.89MB read
Requests/sec:     76.43
Transfer/sec:     15.23KB


Path:
/sleep/0.5

Example Output:
curl http://192.168.0.10:8080/sleep/0.5
{"elapsed":0.50641457,"slept":0.5}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/sleep/0.5
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   631.12ms  186.21ms   1.02s    75.21%
    Req/Sec     9.63      7.61    40.00     75.07%
  944 requests in 1.00m, 187.99KB read
Requests/sec:     15.72
Transfer/sec:      3.13KB


Path:
/sleep/1.0

Example Output:
curl http://192.168.0.10:8080/sleep/1.0
{"elapsed":1.005425587,"slept":1.0}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/sleep/1.0
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.21s   379.59ms   2.00s    79.11%
    Req/Sec     7.56     10.11    39.00     80.13%
  472 requests in 1.00m, 93.97KB read
  Socket errors: connect 0, read 0, write 0, timeout 22
Requests/sec:      7.86
Transfer/sec:      1.56KB


Path:
/estimate-pi/100

Example Output:
curl http://192.168.0.10:8080/estimate-pi/100
{"estimatedPi":3.4,"elapsed":9.3136E-5}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/estimate-pi/100
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     3.88ms    9.95ms 360.64ms   95.08%
    Req/Sec     2.34k   220.72     3.13k    78.25%
  278939 requests in 1.00m, 43.95MB read
Requests/sec:   4648.24
Transfer/sec:    749.89KB


Path:
/estimate-pi/1000

Example Output:
curl http://192.168.0.10:8080/estimate-pi/1000
{"estimatedPi":3.164,"elapsed":2.25823E-4}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/estimate-pi/1000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     6.23ms   14.02ms 224.10ms   93.44%
    Req/Sec     1.68k   164.20     2.44k    70.33%
  200657 requests in 1.00m, 32.01MB read
Requests/sec:   3342.78
Transfer/sec:    546.03KB


Path:
/estimate-pi/10000

Example Output:
curl http://192.168.0.10:8080/estimate-pi/10000
{"estimatedPi":3.128,"elapsed":0.001454232}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/estimate-pi/10000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    67.09ms  100.91ms 516.49ms   82.90%
    Req/Sec   263.57     79.73   530.00     69.48%
  31518 requests in 1.00m, 5.07MB read
Requests/sec:    524.59
Transfer/sec:     86.49KB


Path:
/estimate-pi/100000

Example Output:
curl http://192.168.0.10:8080/estimate-pi/100000
{"estimatedPi":3.12864,"elapsed":0.011292373}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/estimate-pi/100000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   185.72ms   37.64ms 346.60ms   70.58%
    Req/Sec    26.61      9.71    59.00     68.55%
  3226 requests in 1.00m, 534.97KB read
Requests/sec:     53.73
Transfer/sec:      8.91KB


Path:
/estimate-pi/1000000

Example Output:
curl http://192.168.0.10:8080/estimate-pi/1000000
{"estimatedPi":3.140552,"elapsed":0.09829767}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/estimate-pi/1000000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.64s   101.74ms   1.93s    70.00%
    Req/Sec     5.47      5.26    29.00     74.71%
  318 requests in 1.00m, 53.00KB read
  Socket errors: connect 0, read 0, write 0, timeout 78
Requests/sec:      5.30
Transfer/sec:      0.88KB
```
#### 20 Connections
```cmd
Path:
/hello

Example Output:
curl http://192.168.0.10:8080/hello
{"message":"Hello World!"}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/hello
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     3.91ms    9.00ms 219.95ms   96.90%
    Req/Sec     3.74k   265.39     4.54k    78.33%
  446640 requests in 1.00m, 64.40MB read
Requests/sec:   7442.94
Transfer/sec:      1.07MB


Path:
/sleep/0.1

Example Output:
curl http://192.168.0.10:8080/sleep/0.1
{"elapsed":0.110675504,"slept":0.1}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/sleep/0.1
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   258.91ms   52.20ms 330.75ms   48.77%
    Req/Sec    38.51     11.72    70.00     80.79%
  4624 requests in 1.00m, 0.90MB read
Requests/sec:     77.02
Transfer/sec:     15.35KB


Path:
/sleep/0.5

Example Output:
curl http://192.168.0.10:8080/sleep/0.5
{"elapsed":0.508564991,"slept":0.5}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/sleep/0.5
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.26s   261.93ms   1.53s    64.64%
    Req/Sec     7.12      2.53    29.00     89.83%
  936 requests in 1.00m, 186.36KB read
Requests/sec:     15.58
Transfer/sec:      3.10KB


Path:
/sleep/1.0

Example Output:
curl http://192.168.0.10:8080/sleep/1.0
{"elapsed":1.001844342,"slept":1.0}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/sleep/1.0
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s   183.15us   1.01s    50.00%
    Req/Sec     3.57      3.09    29.00     94.31%
  472 requests in 1.00m, 93.98KB read
  Socket errors: connect 0, read 0, write 0, timeout 464
Requests/sec:      7.85
Transfer/sec:      1.56KB


Path:
/estimate-pi/100

Example Output:
curl http://192.168.0.10:8080/estimate-pi/100
{"estimatedPi":3.24,"elapsed":3.06944E-4}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/estimate-pi/100
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     7.88ms   20.32ms 379.99ms   94.36%
    Req/Sec     2.70k   202.42     3.61k    74.08%
  322146 requests in 1.00m, 50.82MB read
Requests/sec:   5367.73
Transfer/sec:    867.02KB


Path:
/estimate-pi/1000

Example Output:
curl http://192.168.0.10:8080/estimate-pi/1000
{"estimatedPi":3.132,"elapsed":2.20582E-4}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/estimate-pi/1000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     9.38ms   19.11ms 267.46ms   94.58%
    Req/Sec     1.77k   156.40     2.42k    71.33%
  211772 requests in 1.00m, 33.88MB read
Requests/sec:   3526.12
Transfer/sec:    577.67KB


Path:
/estimate-pi/10000

Example Output:
curl http://192.168.0.10:8080/estimate-pi/10000
{"estimatedPi":3.1364,"elapsed":0.001436012}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/estimate-pi/10000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    68.83ms   92.09ms 533.29ms   86.02%
    Req/Sec   261.65     36.05   370.00     68.00%
  31295 requests in 1.00m, 5.04MB read
Requests/sec:    520.70
Transfer/sec:     85.85KB


Path:
/estimate-pi/100000

Example Output:
curl http://192.168.0.10:8080/estimate-pi/100000
{"estimatedPi":3.14012,"elapsed":0.010938061}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/estimate-pi/100000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   409.65ms   37.47ms 516.95ms   72.92%
    Req/Sec    24.17     10.14    59.00     67.13%
  2921 requests in 1.00m, 484.23KB read
Requests/sec:     48.60
Transfer/sec:      8.06KB


Path:
/estimate-pi/1000000

Example Output:
curl http://192.168.0.10:8080/estimate-pi/1000000
{"estimatedPi":3.1409,"elapsed":0.096358125}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/estimate-pi/1000000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.67s    43.64ms   1.73s    62.50%
    Req/Sec     5.63      6.63    39.00     91.28%
  287 requests in 1.00m, 47.82KB read
  Socket errors: connect 0, read 0, write 0, timeout 279
Requests/sec:      4.78
Transfer/sec:     814.97B
```
#### 40 Connections
```cmd
Path:
/hello

Example Output:
curl http://192.168.0.10:8080/hello
{"message":"Hello World!"}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/hello
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     6.56ms    8.66ms 264.98ms   97.77%
    Req/Sec     3.51k   209.70     3.91k    82.42%
  418953 requests in 1.00m, 60.41MB read
Requests/sec:   6980.95
Transfer/sec:      1.01MB


Path:
/sleep/0.1

Example Output:
curl http://192.168.0.10:8080/sleep/0.1
{"elapsed":0.10827681,"slept":0.1}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/sleep/0.1
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   515.68ms   24.24ms 543.72ms   98.92%
    Req/Sec    38.49      9.64    60.00     61.72%
  4640 requests in 1.00m, 0.90MB read
Requests/sec:     77.25
Transfer/sec:     15.39KB


Path:
/sleep/0.5

Example Output:
curl http://192.168.0.10:8080/sleep/0.5
{"elapsed":0.504281381,"slept":0.5}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/sleep/0.5
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.02s   422.36ms   1.52s    33.33%
    Req/Sec     7.39      5.32    48.00     95.04%
  946 requests in 1.00m, 188.33KB read
  Socket errors: connect 0, read 0, write 0, timeout 922
Requests/sec:     15.75
Transfer/sec:      3.14KB


Path:
/sleep/1.0

Example Output:
curl http://192.168.0.10:8080/sleep/1.0
{"elapsed":1.009193184,"slept":1.0}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/sleep/1.0
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s   396.22us   1.01s    62.50%
    Req/Sec     3.51      3.78    38.00     98.33%
  472 requests in 1.00m, 93.97KB read
  Socket errors: connect 0, read 0, write 0, timeout 464
Requests/sec:      7.86
Transfer/sec:      1.57KB


Path:
/estimate-pi/100

Example Output:
curl http://192.168.0.10:8080/estimate-pi/100
{"estimatedPi":3.2,"elapsed":6.9582E-5}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/estimate-pi/100
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     8.93ms    8.87ms 350.61ms   97.31%
    Req/Sec     2.48k   111.58     2.86k    76.92%
  296721 requests in 1.00m, 46.99MB read
Requests/sec:   4944.06
Transfer/sec:    801.75KB


Path:
/estimate-pi/1000

Example Output:
curl http://192.168.0.10:8080/estimate-pi/1000
{"estimatedPi":3.116,"elapsed":1.95275E-4}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/estimate-pi/1000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    14.03ms   16.67ms 391.13ms   96.94%
    Req/Sec     1.66k   130.73     2.05k    74.75%
  198758 requests in 1.00m, 31.80MB read
Requests/sec:   3309.59
Transfer/sec:    542.30KB


Path:
/estimate-pi/10000

Example Output:
curl http://192.168.0.10:8080/estimate-pi/10000
{"estimatedPi":3.1656,"elapsed":0.001053255}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/estimate-pi/10000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    89.62ms   78.06ms 575.23ms   90.94%
    Req/Sec   270.27     24.23   340.00     71.32%
  32303 requests in 1.00m, 5.20MB read
Requests/sec:    538.11
Transfer/sec:     88.72KB


Path:
/estimate-pi/100000

Example Output:
curl http://192.168.0.10:8080/estimate-pi/100000
{"estimatedPi":3.13612,"elapsed":0.010399417}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/estimate-pi/100000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   739.98ms   55.61ms 879.07ms   83.92%
    Req/Sec    26.84     11.68    70.00     63.76%
  3228 requests in 1.00m, 534.75KB read
Requests/sec:     53.71
Transfer/sec:      8.90KB


Path:
/estimate-pi/1000000

Example Output:
curl http://192.168.0.10:8080/estimate-pi/1000000
{"estimatedPi":3.14052,"elapsed":0.097941041}

Wrk Report:
Running 1m test @ http://192.168.0.10:8080/estimate-pi/1000000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.49s   129.64ms   1.65s    62.50%
    Req/Sec     5.29      4.59    20.00     79.78%
  318 requests in 1.00m, 52.99KB read
  Socket errors: connect 0, read 0, write 0, timeout 310
Requests/sec:      5.30
Transfer/sec:      0.88KB
```
### Flask and Werkzeug
#### 10 Connections
```cmd
Path:
/hello

Example Output:
curl http://192.168.0.10:5000/hello
{"message": "Hello World!"}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/hello
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    16.82ms    9.48ms 319.54ms   98.33%
    Req/Sec   295.84     35.06   333.00     93.33%
  23907 requests in 1.00m, 3.97MB read
Requests/sec:    397.78
Transfer/sec:     67.59KB


Path:
/sleep/0.1

Example Output:
curl http://192.168.0.10:5000/sleep/0.1
{"slept": 0.1, "elapsed": 0.10369205474853516}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/sleep/0.1
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   108.21ms    2.05ms 143.90ms   80.81%
    Req/Sec    45.62      8.08    50.00     88.96%
  5486 requests in 1.00m, 1.01MB read
Requests/sec:     91.33
Transfer/sec:     17.20KB


Path:
/sleep/0.5

Example Output:
curl http://192.168.0.10:5000/sleep/0.5
{"slept": 0.5, "elapsed": 0.5028591156005859}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/sleep/0.5
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   507.14ms    2.40ms 520.57ms   69.66%
    Req/Sec    16.43     14.68    40.00     78.38%
  1180 requests in 1.00m, 221.10KB read
Requests/sec:     19.64
Transfer/sec:      3.68KB


Path:
/sleep/1.0

Example Output:
curl http://192.168.0.10:5000/sleep/1.0
{"slept": 1.0, "elapsed": 1.0020029544830322}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/sleep/1.0
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s     2.43ms   1.02s    69.32%
    Req/Sec    11.83     13.25    40.00     79.33%
  590 requests in 1.00m, 110.47KB read
Requests/sec:      9.82
Transfer/sec:      1.84KB


Path:
/estimate-pi/100

Example Output:
curl http://192.168.0.10:5000/estimate-pi/100
{"estimatedPi": 3.32, "elapsed": 0.00011110305786132812}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi/100
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    17.37ms    2.86ms  85.83ms   94.70%
    Req/Sec   264.24     56.95   320.00     91.92%
  27348 requests in 1.00m, 5.26MB read
Requests/sec:    455.22
Transfer/sec:     89.69KB


Path:
/estimate-pi/1000

Example Output:
curl http://192.168.0.10:5000/estimate-pi/1000
{"estimatedPi": 3.216, "elapsed": 0.0008389949798583984}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi/1000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    23.72ms    5.88ms 117.44ms   94.08%
    Req/Sec   207.44     25.74   250.00     88.16%
  23311 requests in 1.00m, 4.50MB read
Requests/sec:    388.31
Transfer/sec:     76.84KB


Path:
/estimate-pi/10000

Example Output:
curl http://192.168.0.10:5000/estimate-pi/10000
{"estimatedPi": 3.148, "elapsed": 0.007201194763183594}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi/10000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    80.34ms    3.96ms 138.33ms   87.75%
    Req/Sec    61.70      7.20    80.00     56.22%
  7402 requests in 1.00m, 1.43MB read
Requests/sec:    123.27
Transfer/sec:     24.41KB


Path:
/estimate-pi/100000

Example Output:
curl http://192.168.0.10:5000/estimate-pi/100000
{"estimatedPi": 3.14328, "elapsed": 0.0626990795135498}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi/100000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   645.78ms  108.01ms   1.01s    68.94%
    Req/Sec     8.71      5.31    39.00     77.47%
  924 requests in 1.00m, 182.54KB read
Requests/sec:     15.39
Transfer/sec:      3.04KB


Path:
/estimate-pi/1000000

Example Output:
curl http://192.168.0.10:5000/estimate-pi/1000000
{"estimatedPi": 3.141164, "elapsed": 0.6425909996032715}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi/1000000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     0.00us    0.00us   0.00us     nan%
    Req/Sec     1.51      2.69    10.00     91.01%
  90 requests in 1.00m, 17.73KB read
  Socket errors: connect 0, read 0, write 0, timeout 90
Requests/sec:      1.50
Transfer/sec:     302.30B


Path:
/estimate-pi-np/100

Example Output:
curl http://192.168.0.10:5000/estimate-pi-np/100
{"estimatedPi": 3.04, "elapsed": 0.0004591941833496094}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi-np/100
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    18.92ms    1.93ms  48.64ms   93.37%
    Req/Sec   257.71     21.03   360.00     94.21%
  20894 requests in 1.00m, 4.03MB read
Requests/sec:    347.67
Transfer/sec:     68.65KB


Path:
/estimate-pi-np/1000

Example Output:
curl http://192.168.0.10:5000/estimate-pi-np/1000
{"estimatedPi": 3.112, "elapsed": 0.00035691261291503906}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi-np/1000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.32ms    8.42ms 287.70ms   97.90%
    Req/Sec   243.66     24.33   280.00     94.14%
  22836 requests in 1.00m, 4.41MB read
Requests/sec:    380.01
Transfer/sec:     75.22KB


Path:
/estimate-pi-np/10000

Example Output:
curl http://192.168.0.10:5000/estimate-pi-np/10000
{"estimatedPi": 3.1356, "elapsed": 0.0011410713195800781}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi-np/10000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    22.66ms    7.92ms 216.92ms   96.17%
    Req/Sec   219.05     25.60   260.00     90.44%
  25401 requests in 1.00m, 4.92MB read
Requests/sec:    422.87
Transfer/sec:     83.87KB


Path:
/estimate-pi-np/100000

Example Output:
curl http://192.168.0.10:5000/estimate-pi-np/100000
{"estimatedPi": 3.13948, "elapsed": 0.009479045867919922}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi-np/100000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    51.12ms    7.33ms 257.77ms   91.14%
    Req/Sec    96.78      9.60   141.00     81.48%
  11579 requests in 1.00m, 2.24MB read
Requests/sec:    192.65
Transfer/sec:     38.18KB


Path:
/estimate-pi-np/1000000

Example Output:
curl http://192.168.0.10:5000/estimate-pi-np/1000000
{"estimatedPi": 3.144296, "elapsed": 0.0846099853515625}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi-np/1000000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   475.80ms   17.74ms 702.70ms   92.03%
    Req/Sec    12.01      7.24    40.00     67.97%
  1254 requests in 1.00m, 248.79KB read
Requests/sec:     20.87
Transfer/sec:      4.14KB
```
#### 20 Connections
```cmd
Path:
/hello

Example Output:
curl http://192.168.0.10:5000/hello
{"message": "Hello World!"}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/hello
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    33.87ms    6.94ms 105.27ms   96.53%
    Req/Sec   291.81     35.28   343.00     93.28%
  27332 requests in 1.00m, 4.54MB read
Requests/sec:    455.22
Transfer/sec:     77.35KB


Path:
/sleep/0.1

Example Output:
curl http://192.168.0.10:5000/sleep/0.1
{"slept": 0.1, "elapsed": 0.10280299186706543}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/sleep/0.1
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   110.92ms    6.28ms 267.76ms   85.41%
    Req/Sec    89.55     14.24   101.00     87.30%
  10695 requests in 1.00m, 1.97MB read
Requests/sec:    177.95
Transfer/sec:     33.51KB


Path:
/sleep/0.5

Example Output:
curl http://192.168.0.10:5000/sleep/0.5
{"slept": 0.5, "elapsed": 0.5046379566192627}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/sleep/0.5
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   509.17ms    4.20ms 535.04ms   78.16%
    Req/Sec    28.92     21.44    88.00     68.62%
  2344 requests in 1.00m, 439.18KB read
Requests/sec:     39.01
Transfer/sec:      7.31KB


Path:
/sleep/1.0

Example Output:
curl http://192.168.0.10:5000/sleep/1.0
{"slept": 1.0, "elapsed": 1.0016119480133057}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/sleep/1.0
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s     5.12ms   1.04s    69.07%
    Req/Sec    30.02     31.37    90.00     77.56%
  1180 requests in 1.00m, 221.00KB read
Requests/sec:     19.65
Transfer/sec:      3.68KB


Path:
/estimate-pi/100

Example Output:
curl http://192.168.0.10:5000/estimate-pi/100
{"estimatedPi": 3.24, "elapsed": 9.703636169433594e-05}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi/100
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    36.08ms    6.06ms  97.83ms   97.06%
    Req/Sec   273.42     31.21   310.00     94.04%
  25660 requests in 1.00m, 4.94MB read
Requests/sec:    427.01
Transfer/sec:     84.13KB


Path:
/estimate-pi/1000

Example Output:
curl http://192.168.0.10:5000/estimate-pi/1000
{"estimatedPi": 3.18, "elapsed": 0.0008318424224853516}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi/1000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    48.37ms   18.28ms 555.85ms   97.30%
    Req/Sec   207.84     27.99   242.00     93.55%
  24770 requests in 1.00m, 4.79MB read
Requests/sec:    412.45
Transfer/sec:     81.61KB


Path:
/estimate-pi/10000

Example Output:
curl http://192.168.0.10:5000/estimate-pi/10000
{"estimatedPi": 3.132, "elapsed": 0.007162809371948242}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi/10000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   164.06ms   12.81ms 311.84ms   92.60%
    Req/Sec    60.62     12.65    90.00     74.85%
  7275 requests in 1.00m, 1.41MB read
Requests/sec:    121.12
Transfer/sec:     23.99KB


Path:
/estimate-pi/100000

Example Output:
curl http://192.168.0.10:5000/estimate-pi/100000
{"estimatedPi": 3.14828, "elapsed": 0.06320595741271973}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi/100000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.28s   131.42ms   1.65s    79.94%
    Req/Sec     8.82      5.39    30.00     77.07%
  927 requests in 1.00m, 183.16KB read
Requests/sec:     15.43
Transfer/sec:      3.05KB


Path:
/estimate-pi/1000000

Example Output:
curl http://192.168.0.10:5000/estimate-pi/1000000
{"estimatedPi": 3.142332, "elapsed": 0.6309471130371094}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi/1000000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     0.00us    0.00us   0.00us     nan%
    Req/Sec     2.03      3.78    19.00     88.61%
  83 requests in 1.00m, 16.43KB read
  Socket errors: connect 0, read 0, write 0, timeout 83
Requests/sec:      1.38
Transfer/sec:     280.29B


Path:
/estimate-pi-np/100

Example Output:
curl http://192.168.0.10:5000/estimate-pi-np/100
{"estimatedPi": 3.0, "elapsed": 0.0003170967102050781}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi-np/100
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    38.56ms    4.57ms  84.01ms   97.21%
    Req/Sec   256.08     24.72   304.00     86.22%
  23948 requests in 1.00m, 4.62MB read
Requests/sec:    399.09
Transfer/sec:     78.78KB


Path:
/estimate-pi-np/1000

Example Output:
curl http://192.168.0.10:5000/estimate-pi-np/1000
{"estimatedPi": 3.224, "elapsed": 0.000370025634765625}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi-np/1000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    40.33ms    5.93ms 206.78ms   95.20%
    Req/Sec   243.90     28.98   360.00     90.67%
  25863 requests in 1.00m, 5.00MB read
Requests/sec:    430.34
Transfer/sec:     85.19KB


Path:
/estimate-pi-np/10000

Example Output:
curl http://192.168.0.10:5000/estimate-pi-np/10000
{"estimatedPi": 3.168, "elapsed": 0.0009300708770751953}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi-np/10000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    44.49ms    6.79ms 269.93ms   91.56%
    Req/Sec   221.29     24.26   272.00     88.69%
  20724 requests in 1.00m, 4.01MB read
Requests/sec:    345.13
Transfer/sec:     68.43KB


Path:
/estimate-pi-np/100000

Example Output:
curl http://192.168.0.10:5000/estimate-pi-np/100000
{"estimatedPi": 3.142, "elapsed": 0.008493185043334961}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi-np/100000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   101.12ms   10.71ms 249.30ms   93.81%
    Req/Sec    98.60      8.13   131.00     70.44%
  11793 requests in 1.00m, 2.28MB read
Requests/sec:    196.25
Transfer/sec:     38.83KB


Path:
/estimate-pi-np/1000000

Example Output:
curl http://192.168.0.10:5000/estimate-pi-np/1000000
{"estimatedPi": 3.143252, "elapsed": 0.08569908142089844}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi-np/1000000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   983.13ms   28.51ms   1.32s    95.79%
    Req/Sec    14.74      8.17    30.00     71.10%
  1211 requests in 1.00m, 239.64KB read
Requests/sec:     20.18
Transfer/sec:      3.99KB
```
#### 40 Connections
```cmd
Path:
/hello

Example Output:
curl http://192.168.0.10:5000/hello
{"message": "Hello World!"}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/hello
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    67.17ms    9.95ms 134.87ms   95.21%
    Req/Sec   295.07     36.88   390.00     91.26%
  27616 requests in 1.00m, 4.58MB read
Requests/sec:    460.12
Transfer/sec:     78.19KB


Path:
/sleep/0.1

Example Output:
curl http://192.168.0.10:5000/sleep/0.1
{"slept": 0.1, "elapsed": 0.10373902320861816}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/sleep/0.1
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   120.24ms   11.64ms 297.91ms   69.57%
    Req/Sec   165.40     39.34   202.00     84.00%
  19714 requests in 1.00m, 3.63MB read
Requests/sec:    328.16
Transfer/sec:     61.80KB


Path:
/sleep/0.5

Example Output:
curl http://192.168.0.10:5000/sleep/0.5
{"slept": 0.5, "elapsed": 0.5071320533752441}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/sleep/0.5
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   510.70ms    6.10ms 553.53ms   84.42%
    Req/Sec    44.54     36.95   188.00     83.18%
  4685 requests in 1.00m, 0.86MB read
Requests/sec:     77.95
Transfer/sec:     14.61KB


Path:
/sleep/1.0

Example Output:
curl http://192.168.0.10:5000/sleep/1.0
{"slept": 1.0, "elapsed": 1.000643014907837}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/sleep/1.0
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s     8.55ms   1.05s    84.03%
    Req/Sec    42.46     35.45   191.00     64.62%
  2360 requests in 1.00m, 441.90KB read
Requests/sec:     39.27
Transfer/sec:      7.35KB


Path:
/estimate-pi/100

Example Output:
curl http://192.168.0.10:5000/estimate-pi/100
{"estimatedPi": 3.04, "elapsed": 0.00011110305786132812}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi/100
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    68.27ms   10.63ms  95.15ms   92.25%
    Req/Sec   267.15     54.27   346.00     91.68%
  27525 requests in 1.00m, 5.30MB read
Requests/sec:    458.35
Transfer/sec:     90.30KB


Path:
/estimate-pi/1000

Example Output:
curl http://192.168.0.10:5000/estimate-pi/1000
{"estimatedPi": 3.12, "elapsed": 0.0008609294891357422}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi/1000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    95.91ms   13.25ms 246.51ms   92.73%
    Req/Sec   208.40     25.53   250.00     91.57%
  24901 requests in 1.00m, 4.81MB read
Requests/sec:    414.71
Transfer/sec:     82.05KB


Path:
/estimate-pi/10000

Example Output:
curl http://192.168.0.10:5000/estimate-pi/10000
{"estimatedPi": 3.1552, "elapsed": 0.007124900817871094}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi/10000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   323.47ms   15.17ms 369.62ms   94.16%
    Req/Sec    61.58     17.91   101.00     58.15%
  7394 requests in 1.00m, 1.43MB read
Requests/sec:    123.04
Transfer/sec:     24.37KB


Path:
/estimate-pi/100000

Example Output:
curl http://192.168.0.10:5000/estimate-pi/100000
{"estimatedPi": 3.13644, "elapsed": 0.06353998184204102}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi/100000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.11s   553.84ms   2.00s    58.62%
    Req/Sec     8.59      5.00    30.00     77.01%
  921 requests in 1.00m, 182.16KB read
  Socket errors: connect 0, read 0, write 0, timeout 892
Requests/sec:     15.34
Transfer/sec:      3.03KB


Path:
/estimate-pi/1000000

Example Output:
curl http://192.168.0.10:5000/estimate-pi/1000000
{"estimatedPi": 3.1421, "elapsed": 0.6444780826568604}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi/1000000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     0.00us    0.00us   0.00us     nan%
    Req/Sec     1.59      2.62    10.00     91.55%
  73 requests in 1.00m, 14.45KB read
  Socket errors: connect 0, read 0, write 0, timeout 73
Requests/sec:      1.22
Transfer/sec:     246.29B


Path:
/estimate-pi-np/100

Example Output:
curl http://192.168.0.10:5000/estimate-pi-np/100
{"estimatedPi": 3.08, "elapsed": 0.000186920166015625}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi-np/100
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    86.44ms   68.47ms   1.88s    99.12%
    Req/Sec   238.98     44.87   292.00     93.10%
  24566 requests in 1.00m, 4.74MB read
  Socket errors: connect 0, read 0, write 0, timeout 36
Requests/sec:    408.77
Transfer/sec:     80.70KB


Path:
/estimate-pi-np/1000

Example Output:
curl http://192.168.0.10:5000/estimate-pi-np/1000
{"estimatedPi": 3.1, "elapsed": 0.000354766845703125}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi-np/1000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    86.27ms   22.31ms 498.36ms   94.25%
    Req/Sec   232.70     29.40   300.00     91.77%
  26237 requests in 1.00m, 5.07MB read
Requests/sec:    436.69
Transfer/sec:     86.43KB


Path:
/estimate-pi-np/10000

Example Output:
curl http://192.168.0.10:5000/estimate-pi-np/10000
{"estimatedPi": 3.1228, "elapsed": 0.0008788108825683594}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi-np/10000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    95.41ms   18.38ms 405.96ms   93.19%
    Req/Sec   209.64     24.19   282.00     87.38%
  22279 requests in 1.00m, 4.31MB read
Requests/sec:    370.81
Transfer/sec:     73.50KB


Path:
/estimate-pi-np/100000

Example Output:
curl http://192.168.0.10:5000/estimate-pi-np/100000
{"estimatedPi": 3.13364, "elapsed": 0.008026123046875}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi-np/100000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   222.01ms   45.69ms 523.15ms   83.89%
    Req/Sec    89.90     19.24   151.00     59.82%
  10768 requests in 1.00m, 2.08MB read
Requests/sec:    179.28
Transfer/sec:     35.44KB


Path:
/estimate-pi-np/1000000

Example Output:
curl http://192.168.0.10:5000/estimate-pi-np/1000000
{"estimatedPi": 3.139512, "elapsed": 0.08353924751281738}


Wrk Report:
Running 1m test @ http://192.168.0.10:5000/estimate-pi-np/1000000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.60s   268.74ms   1.97s    70.00%
    Req/Sec    13.54      7.60    50.00     80.86%
  1083 requests in 1.00m, 213.91KB read
  Socket errors: connect 0, read 0, write 0, timeout 1063
Requests/sec:     18.03
Transfer/sec:      3.56KB
```
### Flask and Gunicorn One 'sync' Worker
#### 10 Connections
```cmd
Path:
/hello

Example Output:
curl http://192.168.0.10:8000/hello
{"message": "Hello World!"}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/hello
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    22.50ms   11.00ms 130.88ms   95.25%
    Req/Sec   227.18     36.77   282.00     90.81%
  24195 requests in 1.00m, 4.15MB read
Requests/sec:    402.96
Transfer/sec:     70.83KB


Path:
/sleep/0.1

Example Output:
curl http://192.168.0.10:8000/sleep/0.1
{"slept": 0.1, "elapsed": 0.10200715065002441}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/0.1
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.04s    73.72ms   1.17s    98.08%
    Req/Sec     6.42      3.43    10.00     69.35%
  572 requests in 1.00m, 111.07KB read
Requests/sec:      9.52
Transfer/sec:      1.85KB


Path:
/sleep/0.5

Example Output:
curl http://192.168.0.10:8000/sleep/0.5
{"slept": 0.5, "elapsed": 0.5019841194152832}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/0.5
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s   505.31ms   1.52s    66.67%
    Req/Sec     0.43      0.53     2.00     58.47%
  118 requests in 1.00m, 22.80KB read
  Socket errors: connect 0, read 0, write 0, timeout 115
Requests/sec:      1.97
Transfer/sec:     388.86B


Path:
/sleep/1.0

Example Output:
curl http://192.168.0.10:8000/sleep/1.0
{"slept": 1.0, "elapsed": 1.0019888877868652}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/1.0
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s     0.00us   1.01s   100.00%
    Req/Sec     0.05      0.22     1.00     94.92%
  59 requests in 1.00m, 11.39KB read
  Socket errors: connect 0, read 0, write 0, timeout 58
Requests/sec:      0.98
Transfer/sec:     194.07B


Path:
/estimate-pi/100

Example Output:
curl http://192.168.0.10:8000/estimate-pi/100
{"estimatedPi": 3.04, "elapsed": 9.584426879882812e-05}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    25.05ms   19.11ms 290.28ms   95.75%
    Req/Sec   218.46     35.01   272.00     93.05%
  25893 requests in 1.00m, 5.13MB read
Requests/sec:    431.24
Transfer/sec:     87.50KB


Path:
/estimate-pi/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000
{"estimatedPi": 3.036, "elapsed": 0.0007810592651367188}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    27.72ms    5.90ms  95.13ms   93.82%
    Req/Sec   172.58     29.20   222.00     91.44%
  18989 requests in 1.00m, 3.78MB read
Requests/sec:    316.28
Transfer/sec:     64.42KB


Path:
/estimate-pi/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/10000
{"estimatedPi": 3.16, "elapsed": 0.006896257400512695}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/10000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    85.01ms    5.94ms 221.09ms   96.36%
    Req/Sec    58.26      5.29    80.00     74.98%
  6997 requests in 1.00m, 1.39MB read
Requests/sec:    116.48
Transfer/sec:     23.76KB


Path:
/estimate-pi/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/100000
{"estimatedPi": 3.14256, "elapsed": 0.062039852142333984}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   636.54ms   35.40ms 658.36ms   99.04%
    Req/Sec     8.12      3.77    20.00     64.61%
  937 requests in 1.00m, 191.12KB read
Requests/sec:     15.60
Transfer/sec:      3.18KB


Path:
/estimate-pi/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000000
{"estimatedPi": 3.139744, "elapsed": 0.6138138771057129}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.24s   616.36ms   1.85s    66.67%
    Req/Sec     0.80      0.40     1.00     80.41%
  97 requests in 1.00m, 19.76KB read
  Socket errors: connect 0, read 0, write 0, timeout 94
Requests/sec:      1.62
Transfer/sec:     336.95B


Path:
/estimate-pi-np/100

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100
{"estimatedPi": 3.0, "elapsed": 0.000392913818359375}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    22.39ms    3.02ms  50.60ms   93.10%
    Req/Sec   212.16     30.15   333.00     94.67%
  17097 requests in 1.00m, 3.40MB read
Requests/sec:    284.78
Transfer/sec:     57.92KB


Path:
/estimate-pi-np/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000
{"estimatedPi": 3.052, "elapsed": 0.00024700164794921875}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    25.92ms   15.78ms 249.03ms   95.56%
    Req/Sec   199.40     32.47   404.00     90.37%
  23440 requests in 1.00m, 4.68MB read
Requests/sec:    390.18
Transfer/sec:     79.73KB


Path:
/estimate-pi-np/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/10000
{"estimatedPi": 3.1184, "elapsed": 0.0012249946594238281}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/10000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    33.47ms   15.20ms 288.71ms   94.99%
    Req/Sec   152.48     18.90   202.00     89.59%
  18169 requests in 1.00m, 3.63MB read
Requests/sec:    302.59
Transfer/sec:     61.95KB


Path:
/estimate-pi-np/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100000
{"estimatedPi": 3.1526, "elapsed": 0.01111912727355957}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   103.93ms    4.32ms 141.27ms   95.03%
    Req/Sec    47.67      4.11    69.00     82.04%
  5729 requests in 1.00m, 1.15MB read
Requests/sec:     95.42
Transfer/sec:     19.54KB


Path:
/estimate-pi-np/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000000
{"estimatedPi": 3.13896, "elapsed": 0.10230088233947754}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   926.18ms   62.24ms 970.76ms   98.60%
    Req/Sec     6.23      3.55    20.00     76.79%
  642 requests in 1.00m, 131.43KB read
Requests/sec:     10.70
Transfer/sec:      2.19KB
```
#### 20 Connections
```cmd
Path:
/hello

Example Output:
curl http://192.168.0.10:8000/hello
{"message": "Hello World!"}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/hello
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    42.61ms   10.30ms 231.30ms   97.16%
    Req/Sec   233.73     26.04   323.00     88.42%
  21832 requests in 1.00m, 3.75MB read
Requests/sec:    363.54
Transfer/sec:     63.90KB


Path:
/sleep/0.1

Example Output:
curl http://192.168.0.10:8000/sleep/0.1
{"slept": 0.1, "elapsed": 0.10191679000854492}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/0.1
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.09s   607.86ms   1.99s    60.00%
    Req/Sec     5.21      2.70    10.00     60.84%
  572 requests in 1.00m, 111.09KB read
  Socket errors: connect 0, read 0, write 0, timeout 552
Requests/sec:      9.52
Transfer/sec:      1.85KB


Path:
/sleep/0.5

Example Output:
curl http://192.168.0.10:8000/sleep/0.5
{"slept": 0.5, "elapsed": 0.500633716583252}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/0.5
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s   504.67ms   1.51s    66.67%
    Req/Sec     0.27      0.45     1.00     72.88%
  118 requests in 1.00m, 22.80KB read
  Socket errors: connect 0, read 0, write 0, timeout 115
Requests/sec:      1.96
Transfer/sec:     388.66B


Path:
/sleep/1.0

Example Output:
curl http://192.168.0.10:8000/sleep/1.0
{"slept": 1.0, "elapsed": 1.0019891262054443}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/1.0
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.00s     0.00us   1.00s   100.00%
    Req/Sec     0.03      0.18     1.00     96.61%
  59 requests in 1.00m, 11.40KB read
  Socket errors: connect 0, read 0, write 0, timeout 58
Requests/sec:      0.98
Transfer/sec:     194.31B


Path:
/estimate-pi/100

Example Output:
curl http://192.168.0.10:8000/estimate-pi/100
{"estimatedPi": 3.28, "elapsed": 8.511543273925781e-05}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    47.69ms   25.01ms 449.36ms   94.71%
    Req/Sec   218.56     34.05   303.00     93.56%
  25556 requests in 1.00m, 5.06MB read
Requests/sec:    425.63
Transfer/sec:     86.37KB


Path:
/estimate-pi/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000
{"estimatedPi": 3.204, "elapsed": 0.0007610321044921875}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    56.18ms   13.21ms 264.98ms   92.88%
    Req/Sec   178.20     21.84   282.00     90.71%
  21285 requests in 1.00m, 4.23MB read
Requests/sec:    354.41
Transfer/sec:     72.21KB


Path:
/estimate-pi/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/10000
{"estimatedPi": 3.1508, "elapsed": 0.006943225860595703}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/10000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   169.70ms    8.81ms 251.76ms   95.27%
    Req/Sec    58.59      6.54   100.00     67.76%
  7038 requests in 1.00m, 1.40MB read
Requests/sec:    117.13
Transfer/sec:     23.88KB


Path:
/estimate-pi/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/100000
{"estimatedPi": 3.13572, "elapsed": 0.06049370765686035}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.26s   103.09ms   1.29s    98.09%
    Req/Sec     8.44      4.12    20.00     62.48%
  940 requests in 1.00m, 191.76KB read
Requests/sec:     15.64
Transfer/sec:      3.19KB


Path:
/estimate-pi/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000000
{"estimatedPi": 3.144928, "elapsed": 0.6094579696655273}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.22s   615.39ms   1.84s    66.67%
    Req/Sec     0.50      0.50     1.00    100.00%
  98 requests in 1.00m, 19.96KB read
  Socket errors: connect 0, read 0, write 0, timeout 95
Requests/sec:      1.63
Transfer/sec:     340.33B


Path:
/estimate-pi-np/100

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100
{"estimatedPi": 3.28, "elapsed": 0.0001652240753173828}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    46.18ms    4.84ms 174.06ms   98.20%
    Req/Sec   212.90     22.68   240.00     97.47%
  16799 requests in 1.00m, 3.34MB read
Requests/sec:    279.84
Transfer/sec:     56.91KB


Path:
/estimate-pi-np/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000
{"estimatedPi": 3.22, "elapsed": 0.0002827644348144531}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    42.00ms   15.50ms  73.89ms   86.57%
    Req/Sec   217.07     43.76   373.00     91.00%
  18795 requests in 1.00m, 3.75MB read
Requests/sec:    313.18
Transfer/sec:     63.99KB


Path:
/estimate-pi-np/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/10000
{"estimatedPi": 3.1344, "elapsed": 0.0011229515075683594}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/10000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    63.04ms    9.44ms 199.73ms   94.17%
    Req/Sec   157.89     17.93   260.00     91.56%
  18889 requests in 1.00m, 3.78MB read
Requests/sec:    314.38
Transfer/sec:     64.37KB


Path:
/estimate-pi-np/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100000
{"estimatedPi": 3.14072, "elapsed": 0.008383035659790039}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   207.94ms    7.61ms 252.13ms   96.92%
    Req/Sec    47.77      7.76    70.00     45.62%
  5744 requests in 1.00m, 1.15MB read
Requests/sec:     95.62
Transfer/sec:     19.59KB


Path:
/estimate-pi-np/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000000
{"estimatedPi": 3.142612, "elapsed": 0.09188199043273926}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.86s   189.65ms   1.92s    97.21%
    Req/Sec     6.07      3.07    20.00     84.59%
  632 requests in 1.00m, 129.39KB read
  Socket errors: connect 0, read 0, write 0, timeout 22
Requests/sec:     10.52
Transfer/sec:      2.15KB
```
#### 40 Connections
```cmd
Path:
/hello

Example Output:
curl http://192.168.0.10:8000/hello
{"message": "Hello World!"}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/hello
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    68.31ms   33.27ms 212.59ms   77.03%
    Req/Sec   226.06     37.75   323.00     88.76%
  21128 requests in 1.00m, 3.63MB read
  Socket errors: connect 9, read 0, write 0, timeout 0
Requests/sec:    351.63
Transfer/sec:     61.81KB


Path:
/sleep/0.1

Example Output:
curl http://192.168.0.10:8000/sleep/0.1
{"slept": 0.1, "elapsed": 0.10054707527160645}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/0.1
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.05s   589.03ms   1.99s    57.89%
    Req/Sec     4.81      2.30    10.00     76.75%
  572 requests in 1.00m, 111.08KB read
  Socket errors: connect 0, read 0, write 0, timeout 553
Requests/sec:      9.52
Transfer/sec:      1.85KB


Path:
/sleep/0.5

Example Output:
curl http://192.168.0.10:8000/sleep/0.5
{"slept": 0.5, "elapsed": 0.5014121532440186}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/0.5
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s   504.81ms   1.51s    66.67%
    Req/Sec     0.31      0.48     2.00     70.34%
  118 requests in 1.00m, 22.81KB read
  Socket errors: connect 0, read 0, write 0, timeout 115
Requests/sec:      1.96
Transfer/sec:     388.84B


Path:
/sleep/1.0

Example Output:
curl http://192.168.0.10:8000/sleep/1.0
{"slept": 1.0, "elapsed": 1.0019640922546387}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/1.0
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s     0.00us   1.01s   100.00%
    Req/Sec     0.03      0.18     1.00     96.61%
  59 requests in 1.00m, 11.39KB read
  Socket errors: connect 0, read 0, write 0, timeout 58
Requests/sec:      0.98
Transfer/sec:     194.10B


Path:
/estimate-pi/100

Example Output:
curl http://192.168.0.10:8000/estimate-pi/100
{"estimatedPi": 3.08, "elapsed": 8.487701416015625e-05}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    90.55ms   17.15ms 260.67ms   92.42%
    Req/Sec   217.29     29.89   410.00     94.39%
  23337 requests in 1.00m, 4.62MB read
Requests/sec:    388.48
Transfer/sec:     78.83KB


Path:
/estimate-pi/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000
{"estimatedPi": 3.116, "elapsed": 0.0007770061492919922}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   112.99ms   14.85ms 215.16ms   92.16%
    Req/Sec   176.57     25.39   353.00     91.07%
  21133 requests in 1.00m, 4.20MB read
Requests/sec:    351.89
Transfer/sec:     71.69KB


Path:
/estimate-pi/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/10000
{"estimatedPi": 3.1296, "elapsed": 0.006902933120727539}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/10000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   342.54ms   15.90ms 423.59ms   97.08%
    Req/Sec    58.09      8.61   101.00     61.45%
  6980 requests in 1.00m, 1.39MB read
Requests/sec:    116.15
Transfer/sec:     23.69KB


Path:
/estimate-pi/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/100000
{"estimatedPi": 3.1414, "elapsed": 0.06265091896057129}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.03s   582.31ms   1.99s    61.29%
    Req/Sec     7.88      2.73    20.00     70.80%
  935 requests in 1.00m, 190.77KB read
  Socket errors: connect 0, read 0, write 0, timeout 904
Requests/sec:     15.56
Transfer/sec:      3.18KB


Path:
/estimate-pi/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000000
{"estimatedPi": 3.142128, "elapsed": 0.6074819564819336}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.22s   612.80ms   1.84s    66.67%
    Req/Sec     0.21      0.41     1.00     79.38%
  97 requests in 1.00m, 19.75KB read
  Socket errors: connect 0, read 0, write 0, timeout 94
Requests/sec:      1.62
Transfer/sec:     336.99B


Path:
/estimate-pi-np/100

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100
{"estimatedPi": 3.0, "elapsed": 0.000164031982421875}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    95.17ms   17.46ms 228.25ms   91.84%
    Req/Sec   205.86     26.04   390.00     91.94%
  23506 requests in 1.00m, 4.67MB read
Requests/sec:    391.09
Transfer/sec:     79.56KB


Path:
/estimate-pi-np/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000
{"estimatedPi": 3.156, "elapsed": 0.00028514862060546875}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    99.56ms   17.46ms 233.40ms   90.77%
    Req/Sec   200.54     32.84   380.00     91.56%
  23963 requests in 1.00m, 4.78MB read
Requests/sec:    398.72
Transfer/sec:     81.45KB


Path:
/estimate-pi-np/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/10000
{"estimatedPi": 3.1444, "elapsed": 0.0011222362518310547}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/10000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   130.42ms   11.91ms 231.56ms   89.57%
    Req/Sec   153.14     18.92   282.00     88.80%
  18319 requests in 1.00m, 3.66MB read
Requests/sec:    304.98
Transfer/sec:     62.43KB


Path:
/estimate-pi-np/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100000
{"estimatedPi": 3.14708, "elapsed": 0.008931636810302734}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   423.69ms   21.27ms 491.99ms   96.91%
    Req/Sec    46.90      8.59    70.00     72.51%
  5639 requests in 1.00m, 1.13MB read
Requests/sec:     93.90
Transfer/sec:     19.23KB


Path:
/estimate-pi-np/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000000
{"estimatedPi": 3.141444, "elapsed": 0.09261584281921387}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.04s   589.15ms   1.99s    61.90%
    Req/Sec     5.78      2.89    20.00     58.27%
  634 requests in 1.00m, 129.75KB read
  Socket errors: connect 0, read 0, write 0, timeout 613
Requests/sec:     10.55
Transfer/sec:      2.16KB
```
### Flask and Gunicorn 5 'sync' Workers
#### 10 Connections
```cmd
Path:
/hello

Example Output:
curl http://192.168.0.10:8000/hello
{"message": "Hello World!"}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/hello
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    12.48ms   21.60ms 276.60ms   92.64%
    Req/Sec   395.00     98.72   787.00     76.65%
  28729 requests in 1.00m, 4.93MB read
  Socket errors: connect 5, read 4, write 0, timeout 0
Requests/sec:    478.64
Transfer/sec:     84.14KB


Path:
/sleep/0.1

Example Output:
curl http://192.168.0.10:8000/sleep/0.1
{"slept": 0.1, "elapsed": 0.1007223129272461}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/0.1
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   207.99ms   12.16ms 319.09ms   95.85%
    Req/Sec    23.61      5.48    30.00     83.80%
  2870 requests in 1.00m, 557.35KB read
Requests/sec:     47.81
Transfer/sec:      9.28KB


Path:
/sleep/0.5

Example Output:
curl http://192.168.0.10:8000/sleep/0.5
{"slept": 0.5, "elapsed": 0.500999927520752}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/0.5
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.00s    45.90ms   1.01s    99.16%
    Req/Sec     4.53      2.76    20.00     92.94%
  595 requests in 1.00m, 114.98KB read
Requests/sec:      9.91
Transfer/sec:      1.91KB


Path:
/sleep/1.0

Example Output:
curl http://192.168.0.10:8000/sleep/1.0
{"slept": 1.0, "elapsed": 1.0009710788726807}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/1.0
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.38s   513.88ms   2.00s    62.50%
    Req/Sec     2.15      2.90    19.00     95.93%
  295 requests in 1.00m, 56.97KB read
  Socket errors: connect 0, read 0, write 0, timeout 287
Requests/sec:      4.91
Transfer/sec:      0.95KB


Path:
/estimate-pi/100

Example Output:
curl http://192.168.0.10:8000/estimate-pi/100
{"estimatedPi": 3.04, "elapsed": 9.775161743164062e-05}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    16.96ms   21.35ms 251.98ms   90.24%
    Req/Sec   367.66     75.91   520.00     79.10%
  29484 requests in 1.00m, 5.84MB read
Requests/sec:    491.13
Transfer/sec:     99.65KB


Path:
/estimate-pi/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000
{"estimatedPi": 3.236, "elapsed": 0.0007932186126708984}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    18.37ms   22.82ms 417.35ms   91.38%
    Req/Sec   301.72     57.68   414.00     78.61%
  20103 requests in 1.00m, 4.00MB read
Requests/sec:    334.95
Transfer/sec:     68.22KB


Path:
/estimate-pi/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/10000
{"estimatedPi": 3.1332, "elapsed": 0.006877899169921875}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/10000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    47.54ms   16.87ms 212.45ms   73.54%
    Req/Sec   104.72     16.96   141.00     83.74%
  12535 requests in 1.00m, 2.49MB read
Requests/sec:    208.60
Transfer/sec:     42.46KB


Path:
/estimate-pi/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/100000
{"estimatedPi": 3.14872, "elapsed": 0.06316113471984863}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   342.81ms   16.11ms 388.30ms   78.53%
    Req/Sec    14.57      6.99    40.00     86.11%
  1742 requests in 1.00m, 354.67KB read
Requests/sec:     29.01
Transfer/sec:      5.91KB


Path:
/estimate-pi/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000000
{"estimatedPi": 3.13828, "elapsed": 0.631831169128418}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.64s    26.94ms   1.69s    60.00%
    Req/Sec     5.72      6.94    40.00     90.62%
  180 requests in 1.00m, 36.66KB read
  Socket errors: connect 0, read 0, write 0, timeout 175
Requests/sec:      3.00
Transfer/sec:     625.38B


Path:
/estimate-pi-np/100

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100
{"estimatedPi": 3.04, "elapsed": 0.000408172607421875}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    14.37ms   17.61ms 185.89ms   90.98%
    Req/Sec   349.66     90.84   707.00     83.73%
  23209 requests in 1.00m, 4.61MB read
  Socket errors: connect 0, read 5, write 0, timeout 0
Requests/sec:    386.29
Transfer/sec:     78.58KB


Path:
/estimate-pi-np/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000
{"estimatedPi": 3.136, "elapsed": 0.0002219676971435547}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    16.38ms   21.13ms 324.00ms   90.01%
    Req/Sec   333.07     90.12   600.00     78.22%
  25425 requests in 1.00m, 5.07MB read
Requests/sec:    423.27
Transfer/sec:     86.47KB


Path:
/estimate-pi-np/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/10000
{"estimatedPi": 3.1708, "elapsed": 0.0010077953338623047}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/10000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    18.44ms   16.88ms 230.74ms   88.51%
    Req/Sec   281.55     54.40   393.00     78.69%
  18986 requests in 1.00m, 3.79MB read
Requests/sec:    316.33
Transfer/sec:     64.74KB


Path:
/estimate-pi-np/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100000
{"estimatedPi": 3.14508, "elapsed": 0.008901119232177734}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    48.02ms   19.46ms 307.38ms   82.09%
    Req/Sec   104.21     17.03   151.00     84.13%
  12461 requests in 1.00m, 2.49MB read
Requests/sec:    207.40
Transfer/sec:     42.44KB


Path:
/estimate-pi-np/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000000
{"estimatedPi": 3.14128, "elapsed": 0.08508896827697754}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   413.82ms   19.59ms 567.18ms   79.35%
    Req/Sec    12.64      7.67    40.00     68.35%
  1443 requests in 1.00m, 295.15KB read
Requests/sec:     24.03
Transfer/sec:      4.92KB
```
#### 20 Connections
```cmd
Path:
/hello

Example Output:
curl http://192.168.0.10:8000/hello
{"message": "Hello World!"}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/hello
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    25.89ms   17.91ms 261.82ms   93.38%
    Req/Sec   394.56     54.49   520.00     82.06%
  26838 requests in 1.00m, 4.61MB read
  Socket errors: connect 0, read 20, write 0, timeout 0
Requests/sec:    446.98
Transfer/sec:     78.57KB


Path:
/sleep/0.1

Example Output:
curl http://192.168.0.10:8000/sleep/0.1
{"slept": 0.1, "elapsed": 0.10067415237426758}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/0.1
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   419.67ms   29.31ms 735.02ms   97.12%
    Req/Sec    23.46      8.35    40.00     76.73%
  2848 requests in 1.00m, 553.05KB read
Requests/sec:     47.39
Transfer/sec:      9.20KB


Path:
/sleep/0.5

Example Output:
curl http://192.168.0.10:8000/sleep/0.5
{"slept": 0.5, "elapsed": 0.5019879341125488}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/0.5
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s   427.32ms   1.52s    33.33%
    Req/Sec     4.58      3.72    29.00     94.00%
  590 requests in 1.00m, 114.01KB read
  Socket errors: connect 0, read 0, write 0, timeout 575
Requests/sec:      9.82
Transfer/sec:      1.90KB


Path:
/sleep/1.0

Example Output:
curl http://192.168.0.10:8000/sleep/1.0
{"slept": 1.0, "elapsed": 1.0012199878692627}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/1.0
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s     2.14ms   1.01s    60.00%
    Req/Sec     1.83      1.90    20.00     98.33%
  295 requests in 1.00m, 56.96KB read
  Socket errors: connect 0, read 0, write 0, timeout 290
Requests/sec:      4.91
Transfer/sec:      0.95KB


Path:
/estimate-pi/100

Example Output:
curl http://192.168.0.10:8000/estimate-pi/100
{"estimatedPi": 3.28, "elapsed": 9.703636169433594e-05}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    23.83ms   16.36ms 199.62ms   92.93%
    Req/Sec   362.14     47.87   460.00     86.11%
  24757 requests in 1.00m, 4.90MB read
  Socket errors: connect 8, read 12, write 0, timeout 0
Requests/sec:    412.05
Transfer/sec:     83.60KB


Path:
/estimate-pi/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000
{"estimatedPi": 3.156, "elapsed": 0.0007519721984863281}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    30.32ms   19.70ms 299.64ms   79.77%
    Req/Sec   288.21     46.11   373.00     91.87%
  16384 requests in 1.00m, 3.26MB read
Requests/sec:    272.93
Transfer/sec:     55.58KB


Path:
/estimate-pi/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/10000
{"estimatedPi": 3.1088, "elapsed": 0.0069599151611328125}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/10000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    96.97ms   19.15ms 345.21ms   82.78%
    Req/Sec   103.07     11.82   141.00     71.15%
  12317 requests in 1.00m, 2.45MB read
Requests/sec:    204.95
Transfer/sec:     41.72KB


Path:
/estimate-pi/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/100000
{"estimatedPi": 3.14772, "elapsed": 0.06517505645751953}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   701.71ms   38.55ms 783.59ms   98.18%
    Req/Sec    14.48      7.51    40.00     82.98%
  1699 requests in 1.00m, 345.83KB read
Requests/sec:     28.28
Transfer/sec:      5.76KB


Path:
/estimate-pi/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000000
{"estimatedPi": 3.1423, "elapsed": 0.6568071842193604}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.69s    36.99ms   1.74s    60.00%
    Req/Sec     2.78      3.39    10.00     81.07%
  177 requests in 1.00m, 36.03KB read
  Socket errors: connect 0, read 0, write 0, timeout 172
Requests/sec:      2.95
Transfer/sec:     613.95B


Path:
/estimate-pi-np/100

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100
{"estimatedPi": 3.04, "elapsed": 0.0001957416534423828}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    26.67ms   19.61ms 275.85ms   87.12%
    Req/Sec   338.58     64.10   656.00     88.05%
  26969 requests in 1.00m, 5.36MB read
Requests/sec:    449.40
Transfer/sec:     91.41KB


Path:
/estimate-pi-np/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000
{"estimatedPi": 3.148, "elapsed": 0.0002551078796386719}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    29.31ms   16.51ms 212.43ms   92.78%
    Req/Sec   335.18     46.06   414.00     89.18%
  18947 requests in 1.00m, 3.78MB read
Requests/sec:    315.36
Transfer/sec:     64.42KB


Path:
/estimate-pi-np/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/10000
{"estimatedPi": 3.1396, "elapsed": 0.0009281635284423828}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/10000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    28.42ms   20.57ms 241.24ms   84.63%
    Req/Sec   265.53     62.76   555.00     87.44%
  22357 requests in 1.00m, 4.47MB read
Requests/sec:    372.46
Transfer/sec:     76.22KB


Path:
/estimate-pi-np/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100000
{"estimatedPi": 3.1476, "elapsed": 0.006615877151489258}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    98.29ms   23.16ms 463.83ms   88.84%
    Req/Sec   102.21     13.23   180.00     81.97%
  12185 requests in 1.00m, 2.43MB read
Requests/sec:    202.76
Transfer/sec:     41.48KB


Path:
/estimate-pi-np/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000000
{"estimatedPi": 3.14452, "elapsed": 0.07028579711914062}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   855.48ms   52.26ms 915.01ms   97.06%
    Req/Sec    11.78      6.23    30.00     57.57%
  1395 requests in 1.00m, 285.32KB read
Requests/sec:     23.22
Transfer/sec:      4.75KB
```
#### 40 Connections
```cmd
Path:
/hello

Example Output:
curl http://192.168.0.10:8000/hello
{"message": "Hello World!"}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/hello
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    37.14ms   22.69ms 268.65ms   79.36%
    Req/Sec   377.40     92.68   680.00     75.03%
  30208 requests in 1.00m, 5.19MB read
  Socket errors: connect 17, read 20, write 0, timeout 0
Requests/sec:    502.70
Transfer/sec:     88.37KB


Path:
/sleep/0.1

Example Output:
curl http://192.168.0.10:8000/sleep/0.1
{"slept": 0.1, "elapsed": 0.10200691223144531}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/0.1
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   835.72ms   55.71ms   1.02s    95.69%
    Req/Sec    23.44      6.21    49.00     84.53%
  2853 requests in 1.00m, 554.02KB read
Requests/sec:     47.48
Transfer/sec:      9.22KB


Path:
/sleep/0.5

Example Output:
curl http://192.168.0.10:8000/sleep/0.5
{"slept": 0.5, "elapsed": 0.501054048538208}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/0.5
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s   427.13ms   1.52s    33.33%
    Req/Sec     4.37      2.37    19.00     88.76%
  590 requests in 1.00m, 114.01KB read
  Socket errors: connect 0, read 0, write 0, timeout 575
Requests/sec:      9.83
Transfer/sec:      1.90KB


Path:
/sleep/1.0

Example Output:
curl http://192.168.0.10:8000/sleep/1.0
{"slept": 1.0, "elapsed": 1.0019619464874268}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/1.0
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s     1.90ms   1.01s    40.00%
    Req/Sec     1.86      2.06    19.00     96.69%
  295 requests in 1.00m, 56.97KB read
  Socket errors: connect 0, read 0, write 0, timeout 290
Requests/sec:      4.91
Transfer/sec:      0.95KB


Path:
/estimate-pi/100

Example Output:
curl http://192.168.0.10:8000/estimate-pi/100
{"estimatedPi": 2.96, "elapsed": 8.487701416015625e-05}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    43.42ms   24.39ms 282.44ms   76.81%
    Req/Sec   357.08    102.49   720.00     81.35%
  28582 requests in 1.00m, 5.66MB read
  Socket errors: connect 8, read 28, write 0, timeout 0
Requests/sec:    475.85
Transfer/sec:     96.53KB


Path:
/estimate-pi/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000
{"estimatedPi": 3.08, "elapsed": 0.0007958412170410156}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    57.63ms   21.42ms 220.01ms   82.25%
    Req/Sec   279.45     69.75   505.00     86.78%
  17717 requests in 1.00m, 3.52MB read
Requests/sec:    294.97
Transfer/sec:     60.08KB


Path:
/estimate-pi/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/10000
{"estimatedPi": 3.1516, "elapsed": 0.00718998908996582}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/10000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   194.51ms   22.60ms 365.50ms   88.14%
    Req/Sec   102.46     18.29   166.00     72.36%
  12262 requests in 1.00m, 2.44MB read
Requests/sec:    204.16
Transfer/sec:     41.57KB


Path:
/estimate-pi/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/100000
{"estimatedPi": 3.15044, "elapsed": 0.06509995460510254}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.40s   115.80ms   1.49s    97.99%
    Req/Sec    14.21      6.44    40.00     90.36%
  1693 requests in 1.00m, 344.66KB read
Requests/sec:     28.19
Transfer/sec:      5.74KB


Path:
/estimate-pi/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000000
{"estimatedPi": 3.141436, "elapsed": 0.6637051105499268}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.67s    21.88ms   1.69s    60.00%
    Req/Sec     2.77      3.85    30.00     85.71%
  175 requests in 1.00m, 35.63KB read
  Socket errors: connect 0, read 0, write 0, timeout 170
Requests/sec:      2.91
Transfer/sec:     607.08B


Path:
/estimate-pi-np/100

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100
{"estimatedPi": 3.24, "elapsed": 0.00015687942504882812}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    54.26ms   22.64ms 252.29ms   80.41%
    Req/Sec   329.38     81.54   510.00     84.06%
  22767 requests in 1.00m, 4.52MB read
  Socket errors: connect 0, read 19, write 0, timeout 0
Requests/sec:    379.24
Transfer/sec:     77.15KB


Path:
/estimate-pi-np/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000
{"estimatedPi": 3.224, "elapsed": 0.0002658367156982422}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    45.01ms   23.60ms 228.62ms   80.66%
    Req/Sec   332.53     79.27   670.00     87.06%
  25495 requests in 1.00m, 5.09MB read
Requests/sec:    424.35
Transfer/sec:     86.68KB


Path:
/estimate-pi-np/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/10000
{"estimatedPi": 3.1428, "elapsed": 0.0008759498596191406}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/10000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    33.47ms   33.73ms 740.07ms   92.99%
    Req/Sec   266.70     81.35   550.00     74.35%
  23669 requests in 1.00m, 4.73MB read
  Socket errors: connect 0, read 10, write 0, timeout 0
Requests/sec:    393.97
Transfer/sec:     80.62KB


Path:
/estimate-pi-np/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100000
{"estimatedPi": 3.14748, "elapsed": 0.006256818771362305}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   195.80ms   22.18ms 415.27ms   87.07%
    Req/Sec   101.91     17.19   156.00     67.34%
  12203 requests in 1.00m, 2.44MB read
Requests/sec:    203.06
Transfer/sec:     41.55KB


Path:
/estimate-pi-np/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000000
{"estimatedPi": 3.143916, "elapsed": 0.07279419898986816}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.71s   155.28ms   1.84s    97.47%
    Req/Sec    11.86      6.23    39.00     66.97%
  1382 requests in 1.00m, 282.67KB read
Requests/sec:     22.99
Transfer/sec:      4.70KB
```
### Flask and Gunicorn 5 Workers Two Threads Each
#### 10 Connections
```cmd
Path:
/hello

Example Output:
curl http://192.168.0.10:8000/hello
{"message": "Hello World!"}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/hello
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    17.63ms   22.47ms 229.96ms   90.84%
    Req/Sec   412.65     56.27   550.00     66.69%
  49293 requests in 1.00m, 8.70MB read
Requests/sec:    821.39
Transfer/sec:    148.40KB


Path:
/sleep/0.1

Example Output:
curl http://192.168.0.10:8000/sleep/0.1
{"slept": 0.1, "elapsed": 0.10499715805053711}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/0.1
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   265.56ms   50.75ms 326.91ms   54.65%
    Req/Sec    18.77      8.24    40.00     42.59%
  2256 requests in 1.00m, 449.13KB read
Requests/sec:     37.57
Transfer/sec:      7.48KB


Path:
/sleep/0.5

Example Output:
curl http://192.168.0.10:8000/sleep/0.5
{"slept": 0.5, "elapsed": 0.5022578239440918}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/0.5
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.26s   258.78ms   1.52s    77.97%
    Req/Sec     3.13      1.61     9.00     53.53%
  472 requests in 1.00m, 93.51KB read
Requests/sec:      7.86
Transfer/sec:      1.56KB


Path:
/sleep/1.0

Example Output:
curl http://192.168.0.10:8000/sleep/1.0
{"slept": 1.0, "elapsed": 1.00044584274292}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/1.0
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s     1.19ms   1.01s    50.00%
    Req/Sec     1.30      1.92    19.00     99.05%
  236 requests in 1.00m, 46.73KB read
  Socket errors: connect 0, read 0, write 0, timeout 232
Requests/sec:      3.93
Transfer/sec:     796.30B


Path:
/estimate-pi/100

Example Output:
curl http://192.168.0.10:8000/estimate-pi/100
{"estimatedPi": 3.04, "elapsed": 9.489059448242188e-05}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    27.77ms   41.83ms 389.22ms   86.62%
    Req/Sec   397.65    111.36   747.00     71.06%
  47552 requests in 1.00m, 9.65MB read
Requests/sec:    791.22
Transfer/sec:    164.41KB


Path:
/estimate-pi/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000
{"estimatedPi": 3.116, "elapsed": 0.0007779598236083984}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    21.62ms   25.17ms 264.21ms   90.28%
    Req/Sec   320.49     39.39   424.00     66.19%
  38302 requests in 1.00m, 7.80MB read
Requests/sec:    637.94
Transfer/sec:    133.11KB


Path:
/estimate-pi/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/10000
{"estimatedPi": 3.156, "elapsed": 0.006803035736083984}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/10000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    50.56ms   41.63ms 320.12ms   66.76%
    Req/Sec   113.17     33.16   222.00     65.99%
  13537 requests in 1.00m, 2.76MB read
Requests/sec:    225.54
Transfer/sec:     47.07KB


Path:
/estimate-pi/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/100000
{"estimatedPi": 3.13884, "elapsed": 0.06312298774719238}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   321.74ms   40.60ms 544.96ms   63.19%
    Req/Sec    15.61      7.44    40.00     83.05%
  1861 requests in 1.00m, 388.10KB read
Requests/sec:     30.98
Transfer/sec:      6.46KB


Path:
/estimate-pi/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000000
{"estimatedPi": 3.142652, "elapsed": 0.6118710041046143}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   635.97ms   83.15ms   1.20s    97.96%
    Req/Sec     2.45      3.12    19.00     86.26%
  193 requests in 1.00m, 40.22KB read
  Socket errors: connect 0, read 0, write 0, timeout 95
Requests/sec:      3.22
Transfer/sec:     686.26B


Path:
/estimate-pi-np/100

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100
{"estimatedPi": 3.0, "elapsed": 0.00044989585876464844}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    19.15ms   23.55ms 250.18ms   90.45%
    Req/Sec   373.12     51.53   500.00     66.36%
  44599 requests in 1.00m, 9.07MB read
Requests/sec:    742.94
Transfer/sec:    154.78KB


Path:
/estimate-pi-np/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000
{"estimatedPi": 3.132, "elapsed": 0.0003139972686767578}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    18.82ms   22.53ms 271.79ms   91.05%
    Req/Sec   366.55     50.88   494.00     64.52%
  43814 requests in 1.00m, 8.95MB read
Requests/sec:    729.85
Transfer/sec:    152.68KB


Path:
/estimate-pi-np/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/10000
{"estimatedPi": 3.156, "elapsed": 0.001032114028930664}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/10000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    25.36ms    1.84ms  55.69ms   88.05%
    Req/Sec   197.92      7.71   212.00     71.74%
  23656 requests in 1.00m, 4.83MB read
Requests/sec:    394.05
Transfer/sec:     82.47KB


Path:
/estimate-pi-np/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100000
{"estimatedPi": 3.1408, "elapsed": 0.009665966033935547}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    52.23ms   42.06ms 304.94ms   69.98%
    Req/Sec   106.84     26.61   198.00     65.18%
  12797 requests in 1.00m, 2.62MB read
Requests/sec:    213.00
Transfer/sec:     44.62KB


Path:
/estimate-pi-np/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000000
{"estimatedPi": 3.14138, "elapsed": 0.08600711822509766}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   400.24ms   49.90ms 586.85ms   65.37%
    Req/Sec    12.40      5.55    30.00     65.44%
  1493 requests in 1.00m, 312.71KB read
Requests/sec:     24.88
Transfer/sec:      5.21KB
```
#### 20 Connections
```cmd
Path:
/hello

Example Output:
curl http://192.168.0.10:8000/hello
{"message": "Hello World!"}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/hello
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    27.15ms   19.18ms 280.34ms   92.72%
    Req/Sec   409.03     36.82   494.00     69.12%
  48858 requests in 1.00m, 8.62MB read
Requests/sec:    814.18
Transfer/sec:    147.10KB


Path:
/sleep/0.1

Example Output:
curl http://192.168.0.10:8000/sleep/0.1
{"slept": 0.1, "elapsed": 0.10080218315124512}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/0.1
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   530.10ms   77.09ms 646.70ms   49.71%
    Req/Sec    19.31      9.50    40.00     43.58%
  2255 requests in 1.00m, 448.90KB read
Requests/sec:     37.56
Transfer/sec:      7.48KB


Path:
/sleep/0.5

Example Output:
curl http://192.168.0.10:8000/sleep/0.5
{"slept": 0.5, "elapsed": 0.5008029937744141}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/0.5
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   552.24ms  194.03ms   1.52s    93.85%
    Req/Sec     4.54      3.10    28.00     75.30%
  590 requests in 1.00m, 116.89KB read
  Socket errors: connect 0, read 0, write 0, timeout 460
Requests/sec:      9.82
Transfer/sec:      1.95KB


Path:
/sleep/1.0

Example Output:
curl http://192.168.0.10:8000/sleep/1.0
{"slept": 1.0, "elapsed": 1.0013377666473389}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/1.0
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s     1.34ms   1.01s   100.00%
    Req/Sec     1.60      2.69    19.00     96.52%
  236 requests in 1.00m, 46.74KB read
  Socket errors: connect 0, read 0, write 0, timeout 232
Requests/sec:      3.93
Transfer/sec:     796.56B


Path:
/estimate-pi/100

Example Output:
curl http://192.168.0.10:8000/estimate-pi/100
{"estimatedPi": 3.36, "elapsed": 0.0001220703125}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    43.23ms   53.38ms 503.14ms   84.46%
    Req/Sec   393.86    167.95   810.00     62.77%
  47089 requests in 1.00m, 9.56MB read
Requests/sec:    783.63
Transfer/sec:    162.83KB


Path:
/estimate-pi/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000
{"estimatedPi": 3.1, "elapsed": 0.0008802413940429688}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    34.42ms   21.55ms 279.20ms   92.51%
    Req/Sec   316.94     28.91   390.00     70.20%
  37898 requests in 1.00m, 7.72MB read
Requests/sec:    631.05
Transfer/sec:    131.67KB


Path:
/estimate-pi/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/10000
{"estimatedPi": 3.146, "elapsed": 0.006883859634399414}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/10000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    91.12ms   49.23ms 375.91ms   82.18%
    Req/Sec   113.11     16.21   171.00     70.13%
  13547 requests in 1.00m, 2.76MB read
Requests/sec:    225.55
Transfer/sec:     47.07KB


Path:
/estimate-pi/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/100000
{"estimatedPi": 3.14788, "elapsed": 0.06204485893249512}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   642.21ms   81.63ms 848.46ms   63.14%
    Req/Sec    15.82      7.58    40.00     81.91%
  1861 requests in 1.00m, 388.11KB read
Requests/sec:     30.98
Transfer/sec:      6.46KB


Path:
/estimate-pi/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000000
{"estimatedPi": 3.14256, "elapsed": 0.6094150543212891}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.25s    63.49ms   1.34s    75.00%
    Req/Sec     2.51      3.04    10.00     84.36%
  190 requests in 1.00m, 39.62KB read
  Socket errors: connect 0, read 0, write 0, timeout 186
Requests/sec:      3.16
Transfer/sec:     674.99B


Path:
/estimate-pi-np/100

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100
{"estimatedPi": 3.28, "elapsed": 0.0002129077911376953}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    29.52ms   20.00ms 273.39ms   92.85%
    Req/Sec   373.69     35.03   484.00     69.45%
  44685 requests in 1.00m, 9.09MB read
Requests/sec:    744.10
Transfer/sec:    154.99KB


Path:
/estimate-pi-np/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000
{"estimatedPi": 3.22, "elapsed": 0.0002677440643310547}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    47.62ms   17.70ms 560.11ms   98.64%
    Req/Sec   214.88     17.38   353.00     88.54%
  25676 requests in 1.00m, 5.23MB read
Requests/sec:    427.32
Transfer/sec:     89.11KB


Path:
/estimate-pi-np/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/10000
{"estimatedPi": 3.1252, "elapsed": 0.0008337497711181641}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/10000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    54.67ms   64.76ms 542.83ms   84.68%
    Req/Sec   293.83    102.34   630.00     72.86%
  34959 requests in 1.00m, 7.15MB read
Requests/sec:    582.16
Transfer/sec:    121.98KB


Path:
/estimate-pi-np/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100000
{"estimatedPi": 3.13724, "elapsed": 0.006084918975830078}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    93.56ms   19.21ms 254.06ms   71.66%
    Req/Sec   107.17     12.54   140.00     63.03%
  12829 requests in 1.00m, 2.63MB read
Requests/sec:    213.49
Transfer/sec:     44.74KB


Path:
/estimate-pi-np/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000000
{"estimatedPi": 3.141736, "elapsed": 0.06939697265625}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   797.92ms  100.49ms   1.13s    68.54%
    Req/Sec    13.55      7.60    40.00     79.69%
  1494 requests in 1.00m, 312.88KB read
Requests/sec:     24.88
Transfer/sec:      5.21KB
```
#### 40 Connections
```cmd
Path:
/hello

Example Output:
curl http://192.168.0.10:8000/hello
{"message": "Hello World!"}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/hello
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    50.68ms   17.46ms 292.00ms   92.38%
    Req/Sec   404.75     35.52   474.00     79.07%
  48393 requests in 1.00m, 8.54MB read
Requests/sec:    805.27
Transfer/sec:    145.49KB


Path:
/sleep/0.1

Example Output:
curl http://192.168.0.10:8000/sleep/0.1
{"slept": 0.1, "elapsed": 0.10453295707702637}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/0.1
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.07s   753.74ms   1.86s    55.32%
    Req/Sec    18.37      7.97    39.00     36.94%
  2220 requests in 1.00m, 441.89KB read
Requests/sec:     36.96
Transfer/sec:      7.36KB


Path:
/sleep/0.5

Example Output:
curl http://192.168.0.10:8000/sleep/0.5
{"slept": 0.5, "elapsed": 0.5044090747833252}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/0.5
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.02s   434.90ms   1.54s    33.33%
    Req/Sec     3.55      2.88    19.00     89.12%
  472 requests in 1.00m, 93.50KB read
  Socket errors: connect 0, read 0, write 0, timeout 460
Requests/sec:      7.86
Transfer/sec:      1.56KB


Path:
/sleep/1.0

Example Output:
curl http://192.168.0.10:8000/sleep/1.0
{"slept": 1.0, "elapsed": 1.0066790580749512}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/1.0
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s     1.39ms   1.01s   100.00%
    Req/Sec     1.09      0.68     3.00     58.47%
  236 requests in 1.00m, 46.73KB read
  Socket errors: connect 0, read 0, write 0, timeout 232
Requests/sec:      3.93
Transfer/sec:     796.17B


Path:
/estimate-pi/100

Example Output:
curl http://192.168.0.10:8000/estimate-pi/100
{"estimatedPi": 3.16, "elapsed": 9.322166442871094e-05}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    55.54ms   17.59ms 251.87ms   94.60%
    Req/Sec   367.35     27.47   444.00     72.45%
  43893 requests in 1.00m, 8.91MB read
Requests/sec:    731.29
Transfer/sec:    151.96KB


Path:
/estimate-pi/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000
{"estimatedPi": 3.092, "elapsed": 0.0007998943328857422}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    67.59ms   19.35ms 309.01ms   94.21%
    Req/Sec   300.94     22.66   363.00     74.62%
  35971 requests in 1.00m, 7.33MB read
Requests/sec:    599.21
Transfer/sec:    125.03KB


Path:
/estimate-pi/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/10000
{"estimatedPi": 3.1464, "elapsed": 0.007200002670288086}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/10000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   186.58ms  104.34ms 566.36ms   59.68%
    Req/Sec   108.07     21.14   171.00     68.68%
  12939 requests in 1.00m, 2.64MB read
Requests/sec:    215.42
Transfer/sec:     44.95KB


Path:
/estimate-pi/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/100000
{"estimatedPi": 3.146, "elapsed": 0.06305408477783203}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.32s   109.27ms   1.49s    96.94%
    Req/Sec    15.23      7.43    40.00     84.03%
  1797 requests in 1.00m, 374.71KB read
Requests/sec:     29.92
Transfer/sec:      6.24KB


Path:
/estimate-pi/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000000
{"estimatedPi": 3.137988, "elapsed": 0.6056699752807617}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.42s    71.13ms   1.68s    89.13%
    Req/Sec     4.12      5.52    29.00     89.54%
  186 requests in 1.00m, 38.77KB read
  Socket errors: connect 0, read 0, write 0, timeout 140
Requests/sec:      3.10
Transfer/sec:     660.69B


Path:
/estimate-pi-np/100

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100
{"estimatedPi": 2.84, "elapsed": 0.00022077560424804688}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    60.25ms   22.62ms 429.26ms   93.28%
    Req/Sec   341.61     36.09   404.00     85.14%
  40836 requests in 1.00m, 8.31MB read
Requests/sec:    680.09
Transfer/sec:    141.64KB


Path:
/estimate-pi-np/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000
{"estimatedPi": 3.084, "elapsed": 0.0003390312194824219}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    59.79ms   16.63ms 242.05ms   94.41%
    Req/Sec   339.77     20.94   404.00     75.46%
  40600 requests in 1.00m, 8.29MB read
Requests/sec:    676.39
Transfer/sec:    141.46KB


Path:
/estimate-pi-np/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/10000
{"estimatedPi": 3.1516, "elapsed": 0.0007991790771484375}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/10000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    76.24ms   55.84ms 432.62ms   61.38%
    Req/Sec   279.64     99.61   520.00     64.77%
  33441 requests in 1.00m, 6.84MB read
Requests/sec:    556.45
Transfer/sec:    116.59KB


Path:
/estimate-pi-np/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100000
{"estimatedPi": 3.14536, "elapsed": 0.006063938140869141}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   195.52ms   21.36ms 351.90ms   73.46%
    Req/Sec   102.45     14.77   141.00     69.88%
  12270 requests in 1.00m, 2.51MB read
Requests/sec:    204.18
Transfer/sec:     42.79KB


Path:
/estimate-pi-np/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000000
{"estimatedPi": 3.142772, "elapsed": 0.07151389122009277}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000000
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.64s   175.79ms   1.94s    92.30%
    Req/Sec    13.23      7.77    40.00     76.68%
  1442 requests in 1.00m, 302.01KB read
Requests/sec:     24.00
Transfer/sec:      5.03KB
```
### Flask and Gunicorn 5 'gevent' Workers
#### 10 Connections
```cmd
{"estimatedPi": 3.16, "elapsed": 9.608268737792969e-05}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    75.61ms  123.81ms   1.06s    86.34%
    Req/Sec   472.77    196.37     0.98k    69.89%
  55602 requests in 1.00m, 11.28MB read
Requests/sec:    925.70
Transfer/sec:    192.36KB


Path:
/estimate-pi/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000
{"estimatedPi": 3.164, "elapsed": 0.0007679462432861328}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    90.89ms  148.86ms   1.33s    86.27%
    Req/Sec   370.03     92.89   770.00     84.22%
  43912 requests in 1.00m, 8.95MB read
Requests/sec:    730.79
Transfer/sec:    152.48KB


Path:
/estimate-pi/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/10000
{"estimatedPi": 3.1204, "elapsed": 0.006829738616943359}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/10000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    95.23ms  137.66ms   1.18s    86.95%
    Req/Sec   119.32     51.95   252.00     69.57%
  13999 requests in 1.00m, 2.85MB read
Requests/sec:    232.99
Transfer/sec:     48.65KB


Path:
/estimate-pi/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/100000
{"estimatedPi": 3.14256, "elapsed": 0.06122636795043945}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   340.07ms  411.46ms   2.00s    82.55%
    Req/Sec    18.08      9.78    40.00     71.38%
  1845 requests in 1.00m, 385.34KB read
  Socket errors: connect 0, read 0, write 0, timeout 60
Requests/sec:     30.71
Transfer/sec:      6.41KB


Path:
/estimate-pi/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000000
{"estimatedPi": 3.143708, "elapsed": 0.6141960620880127}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s   249.19ms   1.91s    92.50%
    Req/Sec     1.81      1.92    10.00     86.89%
  191 requests in 1.00m, 39.86KB read
  Socket errors: connect 0, read 0, write 0, timeout 71
Requests/sec:      3.18
Transfer/sec:     679.21B


Path:
/estimate-pi-np/100

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100
{"estimatedPi": 2.96, "elapsed": 0.0004143714904785156}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   124.05ms  245.84ms   1.96s    88.10%
    Req/Sec   428.22    147.71   740.00     59.72%
  51203 requests in 1.00m, 10.42MB read
  Socket errors: connect 0, read 0, write 0, timeout 3
Requests/sec:    851.97
Transfer/sec:    177.47KB


Path:
/estimate-pi-np/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000
{"estimatedPi": 3.252, "elapsed": 0.0002701282501220703}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    75.08ms  125.14ms   1.13s    86.69%
    Req/Sec   436.22    161.15     0.89k    72.07%
  51145 requests in 1.00m, 10.45MB read
Requests/sec:    851.20
Transfer/sec:    178.08KB


Path:
/estimate-pi-np/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/10000
{"estimatedPi": 3.1264, "elapsed": 0.0009789466857910156}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/10000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   183.57ms  344.71ms   1.98s    85.81%
    Req/Sec   324.31    109.20   575.00     58.98%
  38756 requests in 1.00m, 7.93MB read
  Socket errors: connect 0, read 0, write 0, timeout 21
Requests/sec:    645.42
Transfer/sec:    135.22KB


Path:
/estimate-pi-np/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100000
{"estimatedPi": 3.14048, "elapsed": 0.009144783020019531}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   113.43ms  169.21ms   1.60s    86.86%
    Req/Sec   117.74     52.19   242.00     70.04%
  13511 requests in 1.00m, 2.77MB read
Requests/sec:    225.00
Transfer/sec:     47.20KB


Path:
/estimate-pi-np/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000000
{"estimatedPi": 3.141284, "elapsed": 0.09952521324157715}


Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000000
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   266.20ms  337.95ms   2.00s    87.67%
    Req/Sec    12.74      6.39    30.00     74.61%
  1399 requests in 1.00m, 293.10KB read
  Socket errors: connect 0, read 0, write 0, timeout 82
Requests/sec:     23.30
Transfer/sec:      4.88KB
```
#### 20 Connections
```cmd
Path:
/hello

Example Output:
curl http://192.168.0.10:8000/hello
{"message": "Hello World!"}

Wrk Report:
Running 1m test @ http://192.168.0.10:8000/hello
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   201.34ms  304.81ms   1.99s    84.57%
    Req/Sec   473.56    160.59     0.95k    73.86%
  56016 requests in 1.00m, 9.88MB read
  Socket errors: connect 0, read 0, write 0, timeout 5
Requests/sec:    932.04
Transfer/sec:    168.39KB


Path:
/sleep/0.1

Example Output:
curl http://192.168.0.10:8000/sleep/0.1
{"slept": 0.1, "elapsed": 0.10204195976257324}

Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/0.1
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   104.70ms    1.96ms 137.73ms   79.14%
    Req/Sec    95.83      6.96   101.00     90.86%
  11452 requests in 1.00m, 2.23MB read
Requests/sec:    190.78
Transfer/sec:     37.98KB


Path:
/sleep/0.5

Example Output:
curl http://192.168.0.10:8000/sleep/0.5
{"slept": 0.5, "elapsed": 0.5002977848052979}

Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/0.5
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   504.98ms    2.55ms 535.11ms   85.38%
    Req/Sec    25.70     19.96    90.00     78.07%
  2360 requests in 1.00m, 467.54KB read
Requests/sec:     39.31
Transfer/sec:      7.79KB


Path:
/sleep/1.0

Example Output:
curl http://192.168.0.10:8000/sleep/1.0
{"slept": 1.0, "elapsed": 1.001143217086792}

Wrk Report:
Running 1m test @ http://192.168.0.10:8000/sleep/1.0
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.01s     2.71ms   1.03s    88.90%
    Req/Sec    15.54     18.61    89.00     88.08%
  1180 requests in 1.00m, 233.67KB read
Requests/sec:     19.66
Transfer/sec:      3.89KB


Path:
/estimate-pi/100

Example Output:
curl http://192.168.0.10:8000/estimate-pi/100
{"estimatedPi": 3.0, "elapsed": 0.0001049041748046875}

Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   193.94ms  285.22ms   1.97s    84.23%
    Req/Sec   463.58    217.53     0.92k    66.37%
  52984 requests in 1.00m, 10.75MB read
  Socket errors: connect 0, read 0, write 0, timeout 3
Requests/sec:    882.67
Transfer/sec:    183.41KB


Path:
/estimate-pi/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000
{"estimatedPi": 3.136, "elapsed": 0.0007860660552978516}

Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   224.38ms  328.34ms   1.94s    83.79%
    Req/Sec   360.78    168.53   720.00     66.76%
  40352 requests in 1.00m, 8.22MB read
  Socket errors: connect 0, read 0, write 0, timeout 16
Requests/sec:    671.82
Transfer/sec:    140.15KB


Path:
/estimate-pi/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/10000
{"estimatedPi": 3.1392, "elapsed": 0.006886959075927734}

Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/10000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   210.77ms  274.01ms   1.90s    83.71%
    Req/Sec   113.85     52.92   240.00     60.84%
  13428 requests in 1.00m, 2.74MB read
  Socket errors: connect 0, read 0, write 0, timeout 1
Requests/sec:    223.64
Transfer/sec:     46.69KB


Path:
/estimate-pi/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/100000
{"estimatedPi": 3.1436, "elapsed": 0.06171011924743652}

Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/100000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   246.43ms  322.35ms   1.92s    89.74%
    Req/Sec    15.14      7.13    30.00     83.27%
  1793 requests in 1.00m, 373.87KB read
  Socket errors: connect 0, read 0, write 0, timeout 153
Requests/sec:     29.87
Transfer/sec:      6.23KB


Path:
/estimate-pi/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi/1000000
{"estimatedPi": 3.142524, "elapsed": 0.6143379211425781}

Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi/1000000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   657.83ms  133.03ms   1.93s    98.28%
    Req/Sec     2.18      2.67    10.00     88.76%
  187 requests in 1.00m, 39.01KB read
  Socket errors: connect 0, read 0, write 0, timeout 71
Requests/sec:      3.11
Transfer/sec:     664.89B


Path:
/estimate-pi-np/100

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100
{"estimatedPi": 3.08, "elapsed": 0.00025916099548339844}

Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   221.24ms  349.41ms   1.97s    84.52%
    Req/Sec   412.91    158.01   830.00     72.87%
  48615 requests in 1.00m, 9.89MB read
  Socket errors: connect 0, read 0, write 0, timeout 39
Requests/sec:    809.33
Transfer/sec:    168.59KB


Path:
/estimate-pi-np/1000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000
{"estimatedPi": 3.132, "elapsed": 0.0003046989440917969}

Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   238.26ms  375.61ms   2.00s    83.98%
    Req/Sec   396.20    142.94   818.00     68.35%
  47070 requests in 1.00m, 9.61MB read
  Socket errors: connect 0, read 0, write 0, timeout 47
Requests/sec:    783.76
Transfer/sec:    163.94KB


Path:
/estimate-pi-np/10000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/10000
{"estimatedPi": 3.1224, "elapsed": 0.0008268356323242188}

Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/10000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   246.52ms  405.14ms   2.00s    83.77%
    Req/Sec   331.22    151.19   650.00     73.31%
  37528 requests in 1.00m, 7.68MB read
  Socket errors: connect 0, read 0, write 0, timeout 101
Requests/sec:    624.85
Transfer/sec:    130.93KB


Path:
/estimate-pi-np/100000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/100000
{"estimatedPi": 3.14524, "elapsed": 0.009732723236083984}

Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/100000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   276.99ms  412.27ms   2.00s    83.04%
    Req/Sec   100.75     33.89   217.00     75.11%
  11981 requests in 1.00m, 2.45MB read
  Socket errors: connect 0, read 0, write 0, timeout 90
Requests/sec:    199.33
Transfer/sec:     41.78KB


Path:
/estimate-pi-np/1000000

Example Output:
curl http://192.168.0.10:8000/estimate-pi-np/1000000
{"estimatedPi": 3.141936, "elapsed": 0.07200908660888672}

Wrk Report:
Running 1m test @ http://192.168.0.10:8000/estimate-pi-np/1000000
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   241.99ms  270.86ms   2.00s    94.08%
    Req/Sec    11.53      5.60    30.00     67.03%
  1356 requests in 1.00m, 283.92KB read
  Socket errors: connect 0, read 0, write 0, timeout 146
Requests/sec:     22.56
Transfer/sec:      4.72KB
```
#### 40 Connections
```cmd

```