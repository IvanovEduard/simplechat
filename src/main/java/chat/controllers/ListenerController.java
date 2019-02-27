package chat.controllers;

import chat.entities.Message;
import chat.entities.ChatData;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class ListenerController {
    @MessageMapping("/send/win")
    @SendTo("/chat/listen/win")
    public Message win(ChatData chatData) throws Exception {
        Thread.sleep(1000); // simulated delay
        return getMessage(chatData);
    }

    @MessageMapping("/send/linux")
    @SendTo("/chat/listen/linux")
    public Message linux(ChatData chatData) throws Exception {
        Thread.sleep(1000); // simulated delay
        return getMessage(chatData);
    }

    private Message getMessage(ChatData chatData) {
        return Message.builder().content(chatData.getMessage()).userName(chatData.getName()).build();
    }
}
