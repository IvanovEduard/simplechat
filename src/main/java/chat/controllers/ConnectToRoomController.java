package chat.controllers;


import chat.general.RoomCapacityBalancer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConnectToRoomController {
    private final RoomCapacityBalancer checkRoomCapacity;

    @RequestMapping("/")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        return new ModelAndView("rooms");
    }

    @RequestMapping("/connect")
    public ModelAndView join(@RequestParam String chatId) {
        return checkRoomCapacity.takePlaceInRoom(chatId);
    }

    @RequestMapping("/disconnect")
    public ModelAndView disconnect(@RequestParam String chatId) {
        checkRoomCapacity.releaseRoom(chatId);
        return new ModelAndView("redirect:/");
    }
}
