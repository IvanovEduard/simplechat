package chat.general;

import com.google.common.collect.Sets;
import lombok.extern.log4j.Log4j;
import org.junit.After;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Log4j
public class RoomCapacityBalancerTest {

    private RoomCapacityBalancer roomCapacityBalancer = new RoomCapacityBalancer();
    private static final String WIN_ID = "win";
    private static final String LINUX_ID = "linux";
    private static final String VIEW_CHAT = "chat";
    private static final String VIEW_ROOMS = "rooms";

    @After
    public void finalizer() throws NoSuchFieldException, IllegalAccessException {
        Class<? extends RoomCapacityBalancer> aClass = roomCapacityBalancer.getClass();
        Field roomLoad = aClass.getDeclaredField("ROOM_LOAD");
        roomLoad.setAccessible(true);
        if (roomLoad.getType() == ConcurrentHashMap.class) {
            roomLoad.set(aClass, new ConcurrentHashMap<>());
        }
    }

    @Test
    public void testTakePlaceInRoom() {
        ModelAndView modelAndViewLinux1 = roomCapacityBalancer.takePlaceInRoom(LINUX_ID);
        ModelAndView modelAndViewLinux2 = roomCapacityBalancer.takePlaceInRoom(LINUX_ID);
        ModelAndView modelAndViewWindows1 = roomCapacityBalancer.takePlaceInRoom(WIN_ID);
        ModelAndView modelAndViewWindows2 = roomCapacityBalancer.takePlaceInRoom(WIN_ID);

        assertThat(modelAndViewLinux1.getViewName(), is(VIEW_CHAT));
        assertThat(modelAndViewLinux2.getViewName(), is(VIEW_CHAT));
        assertThat(modelAndViewWindows1.getViewName(), is(VIEW_CHAT));
        assertThat(modelAndViewWindows2.getViewName(), is(VIEW_CHAT));
    }

    @Test
    public void testTakePlaceInRoomWhenNoFreePlace() {
        ModelAndView modelAndViewChat1 = roomCapacityBalancer.takePlaceInRoom(WIN_ID);
        ModelAndView modelAndViewChat2 = roomCapacityBalancer.takePlaceInRoom(WIN_ID);
        ModelAndView modelAndViewRooms = roomCapacityBalancer.takePlaceInRoom(WIN_ID);

        assertThat(modelAndViewChat1.getViewName(), is(VIEW_CHAT));
        assertThat(modelAndViewChat2.getViewName(), is(VIEW_CHAT));
        assertThat(modelAndViewRooms.getViewName(), is(VIEW_ROOMS));
    }

    @Test
    public void testTakePlaceInRoomThreadSafe() throws ExecutionException, InterruptedException {
        roomCapacityBalancer.takePlaceInRoom(WIN_ID);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<ModelAndView> submit1 = executorService
                .submit(new RoomCapacityBalancerTaken(countDownLatch, roomCapacityBalancer, WIN_ID));
        Future<ModelAndView> submit2 = executorService
                .submit(new RoomCapacityBalancerTaken(countDownLatch, roomCapacityBalancer, WIN_ID));
        countDownLatch.countDown();
        executorService.shutdown();

        Set<String> actual = Sets.newHashSet(submit1.get().getViewName(), submit2.get().getViewName());
        assertThat(actual, is(Sets.newHashSet(VIEW_CHAT, VIEW_ROOMS)));
    }

    @Test
    public void testReleaseRoom() {
        ModelAndView modelAndViewLinux1 = roomCapacityBalancer.takePlaceInRoom(LINUX_ID);
        ModelAndView modelAndViewLinux2 = roomCapacityBalancer.takePlaceInRoom(LINUX_ID);
        roomCapacityBalancer.releaseRoom(LINUX_ID);
        ModelAndView modelAndViewLinux3 = roomCapacityBalancer.takePlaceInRoom(LINUX_ID);

        assertThat(modelAndViewLinux1.getViewName(), is(VIEW_CHAT));
        assertThat(modelAndViewLinux2.getViewName(), is(VIEW_CHAT));
        assertThat(modelAndViewLinux3.getViewName(), is(VIEW_CHAT));
    }

    @Test
    public void testReleaseRoomThreadSafe() throws ExecutionException, InterruptedException {
        roomCapacityBalancer.takePlaceInRoom(WIN_ID);
        roomCapacityBalancer.takePlaceInRoom(WIN_ID);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<ModelAndView> submit = executorService
                .submit(new RoomCapacityBalancerTaken(countDownLatch, roomCapacityBalancer, WIN_ID));
        executorService
                .submit(new RoomCapacityBalancerRelease(countDownLatch, roomCapacityBalancer, WIN_ID));
        countDownLatch.countDown();
        executorService.shutdown();

        assertThat(submit.get().getViewName(), is(VIEW_CHAT));
    }

    class RoomCapacityBalancerTaken implements Callable<ModelAndView> {

        private CountDownLatch countDownLatch;
        private RoomCapacityBalancer roomCapacityBalancer;
        private String chatId;

        private RoomCapacityBalancerTaken(CountDownLatch countDownLatch,
                                          RoomCapacityBalancer roomCapacityBalancer,
                                          String chatId) {
            this.countDownLatch = countDownLatch;
            this.roomCapacityBalancer = roomCapacityBalancer;
            this.chatId = chatId;
        }


        @Override
        public ModelAndView call() throws Exception {
            countDownLatch.await();
            log.info(Thread.currentThread().getName());
            return roomCapacityBalancer.takePlaceInRoom(chatId);
        }
    }

    class RoomCapacityBalancerRelease implements Runnable {

        private CountDownLatch countDownLatch;
        private RoomCapacityBalancer roomCapacityBalancer;
        private String chatId;

        private RoomCapacityBalancerRelease(CountDownLatch countDownLatch,
                                            RoomCapacityBalancer roomCapacityBalancer,
                                            String chatId) {
            this.countDownLatch = countDownLatch;
            this.roomCapacityBalancer = roomCapacityBalancer;
            this.chatId = chatId;
        }

        @Override
        public void run() {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                log.error(e);
            }
            log.info(Thread.currentThread().getName());
            roomCapacityBalancer.releaseRoom(chatId);
        }
    }
}