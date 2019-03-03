package chat.controllers;

import chat.entities.InputChatData;
import chat.entities.OutputChatData;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketListenerController {
    @MessageMapping("/send/windows")
    @SendTo("/chat/listen/windows")
    public OutputChatData windows(InputChatData inputChatData) {
        return getMessage(inputChatData);
    }

    @MessageMapping("/send/linux")
    @SendTo("/chat/listen/linux")
    public OutputChatData linux(InputChatData inputChatData) {
        return getMessage(inputChatData);
    }

    private OutputChatData getMessage(InputChatData inputChatData) {
        return OutputChatData.builder()
                .content(inputChatData.getMessage())
                .userName(inputChatData.getName())
                .build();
    }
}
