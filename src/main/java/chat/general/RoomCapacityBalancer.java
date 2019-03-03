package chat.general;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Log4j
public class RoomCapacityBalancer {
    private static final short CONNECTION_LIMIT = 2;
    private static ConcurrentHashMap<String, AtomicInteger> ROOM_LOAD = new ConcurrentHashMap<>();
    private static final String VIEW_CHAT = "chat";
    private static final String VIEW_ROOMS = "rooms";
    private static final String KEY_CHAT = "chatId";
    private static final long SLEEP_PERIOD = 8;
    private static final int DEFAULT_COUNT = 0;

    public ModelAndView takePlaceInRoom(String chatId) {
        //sleep for case when release and take methods is invoked at same time
        try {
            Thread.sleep(SLEEP_PERIOD);
        } catch (InterruptedException e) {
            log.error(e);
        }

        synchronized (this) {
            AtomicInteger count = ROOM_LOAD.getOrDefault(chatId, new AtomicInteger(DEFAULT_COUNT));
            if (count.get() < CONNECTION_LIMIT) {
                count.incrementAndGet();
                ROOM_LOAD.put(chatId, count);
                ModelAndView modelAndView = new ModelAndView(VIEW_CHAT);
                modelAndView.addObject(KEY_CHAT, chatId);
                return modelAndView;
            }
            return new ModelAndView(VIEW_ROOMS);
        }
    }

    public void releaseRoom(String chatId) {
        AtomicInteger count = ROOM_LOAD.get(chatId);
        synchronized (this) {
            if (count.get() != DEFAULT_COUNT) {
                count.decrementAndGet();
                ROOM_LOAD.put(chatId, count);
            }
        }
    }
}
