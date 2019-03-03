<%--
  Created by IntelliJ IDEA.
  User: ivano
  Date: 25.02.2019
  Time: 23:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Chat</title>
    <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="../../css/room.css" rel="stylesheet" type="text/css">
    <script src="././webjars/jquery/jquery.min.js"></script>
    <script src="././webjars/sockjs-client/sockjs.min.js"></script>
    <script src="././webjars/stomp-websocket/stomp.min.js"></script>
    <script src="../../js/app.js"></script>
    <script>
        var chatId = "${chatId}";
        connect(chatId);
    </script>
</head>
<body>
<div id="main-content" class="container">
    <div>
        <div class="info">
            <div class="name-validation">
                Please enter your name before messaging in ${chatId} room
            </div>
        </div>
        <div class="element-block">
            <form id="nameForm">
                <div class="form-group name-validation">
                    <input type="text" id="name" placeholder="Name..." required>
                    <button id="confirm" class="confirm button-default" type="submit">Confirm</button>
                </div>

            </form>
        </div>
        <div class="element-block align-right">
            <button id="disconnect" class="button-default" type="submit">Disconnect</button>
        </div>
    </div>
    <div class="chatBox">
        <table id="conversation" class="table table-striped">
            <thead>
            <tr>
                <th>Messages</th>
            </tr>
            </thead>
            <tbody id="messages">
            </tbody>
        </table>
    </div>
    <div>
        <textarea id="message" class="messageBox inline-group" placeholder="Message..."></textarea>
        <button id="send" class="send-button inline-group" type="submit" disabled="disabled">Send</button>
    </div>
</div>
</body>
</html>
