<%--
  Created by IntelliJ IDEA.
  User: ivano
  Date: 24.02.2019
  Time: 22:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Rooms</title>
    <link href="../../css/room.css" rel="stylesheet">
    <script src="../webjars/jquery/jquery.min.js"></script>
    <script src="././webjars/sockjs-client/sockjs.min.js"></script>
    <script src="././webjars/stomp-websocket/stomp.min.js"></script>
    <script src="../../js/app.js"></script>
</head>
<body>
<h1>Chat Rooms</h1>

<div>
    <button id="win" class="btn-group" onclick="join(this)">Windows</button>
    <button id="linux" class="btn-group" onclick="join(this)">Linux</button>
</div>
</body>
</html>
