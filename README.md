# Evaluation
Endpoints and services were evaluated with the HTTP benchmarking tool [WRK](https://github.com/wg/wrk/blob/master/README.md).
Given the limitations of the machinery used in this study, all tests were done with 2 requesting threads managing 
either 10 (low), or 40 (high) connections. Each endpoint was tested for 60 seconds.

The requests and responses were over a home LAN with Ethernet cable connections and other traffic minimised.
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
```
# Server Configurations
## Java Spring Boot and Tomcat
The service was implemented to enable asynchronous operation. Other implementation details were left as default.
## Python Flask and Werkzeug
The default development service of Flask was used with default settings and debug disabled.
## Python Flask and Gunicorn
Gunicorn was run with a variety of settings.
- A single `sync` worker.
- `(2 * N(core)) + 1 = 5` `sync` workers with two threads each.
- `(2 * N(core)) + 1 = 5` `gevent` workers.
- `(2 * N(core)) + 1 = 5` `eventlet` workers.
# End Points
## `/hello`
A basic "Hello World" endpoint.
##### Python
```python
def get(self):
    return {'message': 'Hello World!'}
```
##### Java
```java
public Map<String, String> greeting() {
    return Map.of("message", "Hello World!");
}
```
## `/sleep/{t}`
An endpoint that sleeps for a specified number of seconds and returns this number of seconds. This seems to be a reasonable
approximation of I/O bound services.

Three times were chosen, 0.1, 0.5, and 1.0 seconds.

##### Python
```python
def get(self, t: float):
    sleep(t)
    return {'slept': t}
```
##### Java
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
##### Python
```python
def get(self, n: int):
    return {'estimatedPi': sum(4 if random() ** 2 + random() ** 2 < 1 else 0
                               for _ in range(n)) / n}
```
##### Java
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
##### Python
```python
def get(self, n: int):
    return {'estimatedPi': 4 * ((np.random.uniform(size=n) ** 2
                                 + np.random.uniform(size=n) ** 2) < 1).mean()}
```