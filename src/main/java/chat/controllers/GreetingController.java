package chat.controllers;

import chat.entities.Greeting;
import chat.entities.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GreetingController {
    @MessageMapping("/connect")
    @SendTo("/chat/join")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting(HtmlUtils.htmlEscape(message.getName()) + " joined!");
    }
}
