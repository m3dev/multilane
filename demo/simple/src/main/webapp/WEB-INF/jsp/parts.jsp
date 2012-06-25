<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>multilane demo</title>
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

<h1>multilane demo</h1>
<hr/>

<h2>Response time : ${it.spentTime} millis.</h2>
<hr/>

<div>
<ul>
${it.p1}
${it.p2}
${it.p3}
${it.p4}
${it.p5}
${it.p6}
</ul>
</div>

</body>
</html>
