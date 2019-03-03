var stompClient = null;

function enableMessaging() {
    var name = $("#name").val();
    if (name && name.length > 0) {
        $("#send").prop("disabled", false);
        // $("#conversation").show();
    }
}

function connect(chatId) {
    var socket = new SockJS('/simple-chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/chat/listen/' + chatId, function (payload) {
            showMessage(JSON.parse(payload.body));
        });
    });
}

function showMessage(message) {
    $("#messages").append("<tr><td>" +
        "<div class='user-name'>" + getDateInFormat() + " " + message.userName + ":" + "</div>" +
        "</div>" + message.content + "</td></tr>");
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    window.location.href = "disconnect?chatId=" + window.chatId;
    console.log("Disconnected");
}

function sendMessage() {
    stompClient.send("/app/send/" + window.chatId, {}, JSON.stringify(
        {
            'message': $("#message").val(),
            'name': $("#name").val()
        }
    ));
}

function getDateInFormat() {
    var now = new Date();
    return now.toLocaleTimeString();
}

function join(button) {
    window.location.href = "connect?chatId=" + button.id;
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendMessage();
    });
    $("#confirm").click(function () {
        enableMessaging();
    });
});