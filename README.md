# multilane

multilane is a multi-lane expressway to aggregate values in parallel.

Note: This library is in an experimental stage now.

# Setup

## Maven

Available on the maven central repository. Add the following dependency:

```xml
<dependencies>
  <dependency>
    <groupId>com.m3</groupId>
    <artifactId>multilane</artifactId>
    <version>0.1.0-SNAPSHOT</version>
  </dependency>
</dependencies>
```

## Example

```java
HttpGetMultiLane multiLane = new HttpGetMultiLane();

long timeoutMillis = 2500L;
HttpGetAction action1 = new HttpGetAction("http://localhost:8080/api/response-in-1second", "UTF-8", timeoutMillis);
HttpGetAction action2 = new HttpGetAction("http://localhost:8080/api/response-in-2seconds", "UTF-8", timeoutMillis);
HttpGetAction action3 = new HttpGetAction("http://localhost:8080/api/response-in-3seconds", "UTF-8", timeoutMillis);

String unavailable = "<li>Unavailable</li>";

multiLane.start("p1", action1, unavailable);
multiLane.start("p2", action1, unavailable);
multiLane.start("p3", action1, unavailable);
multiLane.start("p4", action2, unavailable);
multiLane.start("p5", action2, unavailable);
multiLane.start("p6", action3, unavailable);

// Blocking here!
Map<String, String> parts = multiLane.collectValues();
String p1 = parts.get("p1");
/*
 Map(
   "p1" -> "<li>Response in 1s</li>",
   "p2" -> "<li>Response in 1s</li>",
   "p3" -> "<li>Response in 1s</li>",
   "p4" -> "<li>Response in 2s</li>",
   "p5" -> "<li>Response in 2s</li>",
   "p6" -> "<li>Unavailable</li>"
 )
 */

// or handle Either values if you need to check the errors
Map<String, Either<Throwable, String>> results = multiLane.collect();
```
