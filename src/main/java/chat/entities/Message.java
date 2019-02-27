package chat.entities;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Message {
    private final String content;
    private final String userName;
}
