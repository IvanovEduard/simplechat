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
    <link href="../../css/main.css" rel="stylesheet">
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
<noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being
    enabled. Please enable
    Javascript and reload this page!</h2></noscript>
<div class="myContainer myDarker">
    <div class="inline-group">
        <div class="inline-group">d</div>
        <div class="inline-group">f</div>

    </div>

</div>
<div id="main-content" class="container">
    <div class="row">
        <div class="col-md-6">
            <form class="form-inline">
                <div class="form-group">
                    <label for="message">Please enter your name before messaging</label>
                    <input type="text" id="name" placeholder="Name..." required="">
                    <button id="confirm" class="btn btn-default inline-group" type="submit">Confirm</button>
                </div>

            </form>
        </div>
        <div class="col-md-6">
            <form class="form-inline">
                <div class="form-group">
                    <button id="disconnect" class="btn btn-default" type="submit">Disconnect</button>
                </div>
            </form>
        </div>
    </div>
    <div class="row chatBox">
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
    <div class="row">

            <textarea id="message" class="messageBox inline-group" placeholder="Message..."></textarea>

            <button id="send" class="send-button inline-group" type="submit" disabled="disabled" data-toggle="tooltip" data-placement="right" title="Name is required">Send</button>

    </div>
</div>
</body>
</html>
