package chat.general;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomCapacityBalancer {
    private static final short CONNECTION_LIMIT = 2;
    private static ConcurrentHashMap<String, Integer> ROOM_LOAD = new ConcurrentHashMap<>();
    private static final String VIEW_CHAT = "chat";
    private static final String VIEW_ROOMS = "rooms";
    private static final String KEY_CHAT = "chatId";
    private static final long SLEEP_PERIOD = 3;
    private static final int DEFAULT_COUNT = 0;

    public ModelAndView takePlaceInRoom(String chatId) {
        //sleep for case when release and take is invoked at same time
        try {
            Thread.sleep(SLEEP_PERIOD);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Integer count = ROOM_LOAD.getOrDefault(chatId, DEFAULT_COUNT);
        count++;
        if (count <= CONNECTION_LIMIT) {
            ROOM_LOAD.put(chatId, count);
            ModelAndView modelAndView = new ModelAndView(VIEW_CHAT);
            modelAndView.addObject(KEY_CHAT, chatId);
            return modelAndView;
        }
        return new ModelAndView(VIEW_ROOMS);
    }

    public void releaseRoom(String chatId) {
        Integer count = ROOM_LOAD.get(chatId);
        if (count != 0) {
            count--;
        }
        ROOM_LOAD.put(chatId, count);
    }
}
