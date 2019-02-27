package chat.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class ConnectToRoomController {
    private static final short CONNECTION_LIMIT = 2;
    private static ConcurrentHashMap<String, Integer> ROOM_LOAD = new ConcurrentHashMap<String, Integer>();

    @RequestMapping("/")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        return new ModelAndView("rooms");
    }

    @RequestMapping("/join")
    public ModelAndView join(@RequestParam String chatId) {
        Integer count = ROOM_LOAD.get(chatId);
        if (count == null || count < CONNECTION_LIMIT ) {
            count = count == null ? 0 : count;
            count++;
            ROOM_LOAD.put(chatId, count);
            ModelAndView modelAndView = new ModelAndView("chat");
            modelAndView.addObject("chatId", chatId);
            return modelAndView;
        }
        return new ModelAndView("rooms");
    }

    @RequestMapping("/outOfRoom")
    public ModelAndView disconnect(@RequestParam String chatId) {
        Integer count = ROOM_LOAD.get(chatId);
        if (count != null && count != 0) {
            count = count - 1;
            ROOM_LOAD.put(chatId, count);
        }
        return new ModelAndView("redirect:/");
    }
}
