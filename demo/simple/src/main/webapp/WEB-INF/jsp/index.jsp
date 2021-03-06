<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>MultiLane demo</title>
    <style>
        body { padding-top: 40px; }
    </style>
    <link rel="stylesheet" media="screen" href='/static/css/bootstrap.min.css'>
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
</head>

<body>

<div class="container">

<h1>MultiLane demo</h1>
<hr/>

<form class="form-horizontal" action="/in-order/" method="get">
  <fieldset>
    <legend>in-order example</legend>
    <div class="control-group">
      <label class="control-label">Timeout millis</label>
      <div class="controls">
        <input type="text" class="input-medium" name="timeout" value="5000">
        <button type="submit" class="btn">Run</button>
      </div>
    </div>
  </fieldset>
</form>

<form class="form-horizontal" action="/multilane/" method="get">
  <fieldset>
    <legend>multilane example</legend>
    <div class="control-group">
      <label class="control-label">Timeout millis</label>
      <div class="controls">
        <input type="text" class="input-medium" name="timeout" value="5000">
        <button type="submit" class="btn">Run</button>
      </div>
    </div>
  </fieldset>
</form>

<hr/>

<pre>
HttpGetToStringMultiLane multiLane = new HttpGetToStringMultiLane();
        
HttpGetToStringAction wait1sec = new HttpGetToStringAction("http://localhost:8080/api/1000", timeout);
HttpGetToStringAction wait2sec = new HttpGetToStringAction("http://localhost:8080/api/2000", timeout);
HttpGetToStringAction wait3sec = new HttpGetToStringAction("http://localhost:8080/api/3000", timeout);
        
String unavailable = "&lt;li&gt;Unavailable&lt;/li&gt;";
        
multiLane.start("p1", wait1sec, unavailable);
multiLane.start("p2", wait1sec, unavailable);
multiLane.start("p3", wait1sec, unavailable);
multiLane.start("p4", wait2sec, unavailable);
multiLane.start("p5", wait2sec, unavailable);
multiLane.start("p6", wait3sec, unavailable);

// Blocking!
Map&lt;String, String&gt; parts = multiLane.collectValues();
        
ViewModel model = new ViewModel();
model.setP1(parts.get("p1"));
model.setP2(parts.get("p2"));
model.setP3(parts.get("p3"));
model.setP4(parts.get("p4"));
model.setP5(parts.get("p5"));
model.setP6(parts.get("p6"));
model.setSpentTime(System.currentTimeMillis() - start);

return Response.ok(new Viewable("/parts.jsp", model)).build();
</pre>

</body>
</html>
