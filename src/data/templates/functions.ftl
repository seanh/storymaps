<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>Propp's Functions</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" >
<style type="text/css">
    body {
        background-color: #f2f2f2;
        color: #333333;
    }
    h1, h2 {
        font-family:Georgia;
    }
</style>
</head>
<body>
<h1>Propp's Functions</h1>
<#list functions as function>
    <h2>${function.number}, ${function.name}</h2>
    <p>${function.description}</p>
    <p>${function.instructions}</p>
</#list>
</body>
</html>