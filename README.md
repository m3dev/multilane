# multilane

'multilane' is a multi-lane expressway to aggregate values in parallel.

# Setup

## Maven

Available on the maven central repository. Add the following dependency:

```xml
<dependencies>
  <dependency>
    <groupId>com.m3</groupId>
    <artifactId>multilane</artifactId>
    <version>1.0.2</version>
  </dependency>
</dependencies>
```

## Example

### Basic MultiLane Usage

Simply aggregate same type values.

```java
HttpGetToStringMultiLane multiLane = new HttpGetToStringMultiLane();

long timeoutMillis = 2500L;
HttpGetToStringAction action1 = new HttpGetToStringAction("http://localhost:8080/api/1s", timeoutMillis);
HttpGetToStringAction action2 = new HttpGetToStringAction("http://localhost:8080/api/2s", timeoutMillis);
HttpGetToStringAction action3 = new HttpGetToStringAction("http://localhost:8080/api/3s", timeoutMillis);

String unavailable = "<li>Unavailable</li>";

multiLane.start("p1", action1, unavailable);
multiLane.start("p2", action1, unavailable);
multiLane.start("p3", action1, unavailable);
multiLane.start("p4", action2, unavailable);
multiLane.start("p5", action2, unavailable);
multiLane.start("p6", action3, unavailable);

// Blocking here!
Map<String, String> parts = multiLane.collectValues();
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

### ActionToBeanMultiLane Usage

Aggregate result values as Java bean fields.

```java
public static class Profile {

  private String name;
  private Integer age;

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public Integer getAge() { return age; }
  public void setAge(Integer age) { this.age = age; }
}

ActionToBeanMultiLane multiLane = new ActionToBeanMultiLane();

multiLane.start("name", new InputAction<String, String>("alice", 1000) {
  public String process(String input) {
    return input.toUpperCase();
  }
});
multiLane.start("age", new SimpleAction<Integer, Integer>(10, 1000) {
  public Integer process(Integer input) {
    return input * 2;
  }
});

Profile profile = multiLane.collectValuesAsBean(Profile.class);
profile.getName(); // "ALICE"
profile.getAge(); // 20
```


## License

Apache License, Version 2.0

http://www.apache.org/licenses/LICENSE-2.0.html

