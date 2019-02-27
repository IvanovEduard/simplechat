var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    // $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/simple-chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/chat/join', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect(el) {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    outOfRoom(el.target.value);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/connect", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function join(button) {
    window.location.href = "join?chatId=" + button.id;
}

function outOfRoom(chatId) {
    window.location.href = "outOfRoom?chatId=" + chatId;
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function(el) { disconnect(el); });
    $( "#send" ).click(function() { sendName(); });
    // $("button").click(function (event) {
    //     var button = event.target;
    //     $.post("join?chatId=" + button.id, function(data, status){
    //         // alert("Data: " + data + "\nStatus: " + status);
    //     });
    // });
});