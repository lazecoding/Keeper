package lazecoding.keeper.component;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.nio.charset.StandardCharsets;

/**
 * UTF-8 SseEmitter
 *
 * @author lazecoding
 */
public class SseEmitterUTF8 extends SseEmitter {

    public SseEmitterUTF8() {
        super();
    }

    public SseEmitterUTF8(Long timeout) {
        super(timeout);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void extendResponse(ServerHttpResponse outputMessage) {
        super.extendResponse(outputMessage);
        // 设置响应类型和字符集
        HttpHeaders headers = outputMessage.getHeaders();
        headers.setContentType(new MediaType("text", "event-stream", StandardCharsets.UTF_8));
    }

}
