package chat.controllers;

import chat.entities.InputChatData;
import chat.entities.OutputChatData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketListenerControllerTest {
    @LocalServerPort
    private int port;

    private static final String SEND_TO_WIN = "/app/send/windows";
    private static final String SEND_TO_LINUX = "/app/send/linux";
    private static final String SUBSCRIBE_WIN = "/chat/listen/windows";
    private static final String SUBSCRIBE_LINUX = "/chat/listen/linux";
    private static final String MESSAGE_WIN = "nice chat win";
    private static final String NAME_WIN = "Luis Win";
    private static final String MESSAGE_LIN = "nice chat lin";
    private static final String NAME_LIN = "Luis lin";

    private StompSession stompSession;

    @Before
    public void setup() throws InterruptedException, ExecutionException, TimeoutException {
        stompSession = getStompSession();
    }

    @Test
    public void testStompWindowsPart() throws InterruptedException, ExecutionException, TimeoutException {
        testStomp(SUBSCRIBE_WIN, SEND_TO_WIN, MESSAGE_WIN, NAME_WIN);
    }

    @Test
    public void testStompLinuxPart() throws InterruptedException, ExecutionException, TimeoutException {
        testStomp(SUBSCRIBE_LINUX, SEND_TO_LINUX, MESSAGE_LIN, NAME_LIN);
    }

    @Test(expected = TimeoutException.class)
    public void testError() throws InterruptedException, ExecutionException, TimeoutException {
        testStomp("subscribe/some/url", "send/some/url", MESSAGE_LIN, NAME_LIN);
    }

    private void testStomp(String subscribe,
                           String send,
                           String message,
                           String name) throws InterruptedException, ExecutionException, TimeoutException {
        CompletableFuture<OutputChatData> completableFuture = new CompletableFuture<>();
        stompSession.subscribe(subscribe, new OutputChatDataStompFrameHandler(completableFuture));
        stompSession.send(send, InputChatData.builder().message(message).name(name).build());

        OutputChatData gameState = completableFuture.get(3, SECONDS);
        assertThat(gameState.getContent(), is(message));
        assertThat(gameState.getUserName(), is(name));
    }

    private StompSession getStompSession() throws InterruptedException, ExecutionException, TimeoutException {
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(
                Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))
        ));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        return stompClient.connect("ws://localhost:" + port + "/simple-chat", new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);
    }

    private class OutputChatDataStompFrameHandler implements StompFrameHandler {
        private CompletableFuture<OutputChatData> completableFuture;

        OutputChatDataStompFrameHandler(CompletableFuture<OutputChatData> completableFuture) {
            this.completableFuture = completableFuture;
        }

        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return OutputChatData.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            completableFuture.complete((OutputChatData) o);
        }
    }
}