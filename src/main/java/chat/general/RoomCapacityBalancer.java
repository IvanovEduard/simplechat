package chat.general;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomCapacityBalancer {
    private static final short CONNECTION_LIMIT = 2;
    private static ConcurrentHashMap<String, Integer> ROOM_LOAD = new ConcurrentHashMap<>();

    public ModelAndView checkRoomCapacity(String chatId) {
        Integer count = ROOM_LOAD.getOrDefault(chatId, 0);
        if (count < CONNECTION_LIMIT) {
            count++;
            ROOM_LOAD.put(chatId, count);
            ModelAndView modelAndView = new ModelAndView("chat");
            modelAndView.addObject("chatId", chatId);
            return modelAndView;
        }
        return new ModelAndView("rooms");
    }

    public void releaseRoom(String chatId) {
        Integer count = ROOM_LOAD.get(chatId);
        if (count != 0) {
            count--;
        }
        ROOM_LOAD.put(chatId, count);
    }
}
