package chat.controllers;

import chat.general.RoomCapacityBalancer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(ConnectToRoomController.class)
public class ConnectToRoomControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomCapacityBalancer checkRoomCapacity;

    @Test
    public void testHandleRequest() throws Exception {
        mockMvc.perform(get("/")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/rooms.jsp"));
    }

    @Test
    public void testConnect() throws Exception {
        when(checkRoomCapacity.takePlaceInRoom("win")).thenReturn(new ModelAndView("chat"));
        mockMvc.perform(get("/connect?chatId=win"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/chat.jsp"));
    }

    @Test
    public void testDisconnect() throws Exception {
        mockMvc.perform(get("/disconnect?chatId=win"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));
    }
}