package chat.controllers;

import chat.entities.ChatData;
import chat.entities.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ListenerController {
    @MessageMapping("/send/win")
    @SendTo("/chat/listen/win")
    public Message win(ChatData chatData) {
        return getMessage(chatData);
    }

    @MessageMapping("/send/linux")
    @SendTo("/chat/listen/linux")
    public Message linux(ChatData chatData) {
        return getMessage(chatData);
    }

    private Message getMessage(ChatData chatData) {
        return Message.builder().content(chatData.getMessage()).userName(chatData.getName()).build();
    }
}
