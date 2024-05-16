package lazecoding.keeper.component;

import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * SSE 消息发送器
 *
 * @author lazecoding
 */
public class SseSender {

    private SseSender() {
    }

    /**
     * 发送信息给本地链接
     **/
    public static boolean sendLocalMessage(SseEmitter emitter, String message) {
        if (ObjectUtils.isEmpty(emitter) || !StringUtils.hasText(message)) {
            return false;
        }
        boolean isSuccess = false;
        try {
            SseEmitter.SseEventBuilder builder = SseEmitter.event().data(message, MediaType.APPLICATION_JSON);
            emitter.send(builder);
            isSuccess = true;
        } catch (Exception e) {
            // 异常
        }
        return isSuccess;
    }

    /**
     * 发送信息给本地用户
     */
    public static void sendLocalMessageToUser(String userId, String message) {
        if (!StringUtils.hasText(userId) || !StringUtils.hasText(message)) {
            return;
        }
        List<SseEmitter> emitters = SseContainer.findUserEmitters(userId);
        if (!CollectionUtils.isEmpty(emitters)) {
            for (SseEmitter emitter : emitters) {
                if (!sendLocalMessage(emitter, message)) {
                    // 失败
                }
            }
        }
    }

    /**
     * 发送信息给本地用户
     */
    public static void sendLocalMessageToUser(List<String> userIds, String message) {
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        for (String userId : userIds) {
            sendLocalMessageToUser(userId, message);
        }
    }

    /**
     * 本地广播
     **/
    public static void sendLocalMessageForBroadcast(String message) {
        Set<Map.Entry<String, SseEmitter>> entries = SseContainer.SSE_EMITTER.entrySet();
        if (CollectionUtils.isEmpty(entries)) {
            return;
        }
        for (Map.Entry<String, SseEmitter> item : entries) {
            SseEmitter emitter = item.getValue();
            if (!sendLocalMessage(emitter, message)) {
                // 失败
            }
        }
    }

}
