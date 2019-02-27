var stompClient = null;

// function setConnected(connected) {
    // $("#connect").prop("disabled", connected);
    // $("#disconnect").prop("disabled", !connected);
    // if (connected) {
    //     $("#conversation").show();
    // } else {
    //     $("#conversation").hide();
    // }
    // $("#greetings").html("");
// }

function enableMessaging() {
    var name = $("#name").val();
    if (name && name.length > 0) {
        $("#send").prop("disabled", false);
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
}

function connect(chatId) {
    var socket = new SockJS('/simple-chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        // setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/chat/listen/' + chatId , function (payload) {
            showMessage(JSON.parse(payload.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    // setConnected(false);
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

function showMessage(message) {
    $("#messages").append("<tr><td>" + message.userName + ":" + message.content + "</td></tr>");
}

function join(button) {
    window.location.href = "connect?chatId=" + button.id;
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    // $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendMessage(); });
    $( "#confirm" ).click(function() { enableMessaging(); });
    // $("button").click(function (event) {
    //     var button = event.target;
    //     $.post("join?chatId=" + button.id, function(data, status){
    //         // alert("Data: " + data + "\nStatus: " + status);
    //     });
    // });
});