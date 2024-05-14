package lazecoding.keeper.component;

import lazecoding.keeper.util.UUIDUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * SSE 容器
 * @author lazecoding
 */
public class SseContainer {

    private SseContainer() {
    }

    /**
     * sseId 和 SseEmitter 的映射。
     */
    public final static Map<String, SseEmitter> SSE_EMITTER = new ConcurrentHashMap<>();

    /**
     * sseId 和 userId 的映射。
     */
    public final static Map<String, String> SSE_USER = new ConcurrentHashMap<>();

    /**
     * userId 和 一组 sseId 的映射，一个用户可能多端登录。
     */
    public final static Map<String, CopyOnWriteArraySet<String>> USER_SSE = new ConcurrentHashMap<>();


    /**
     * 注册  SseEmitter
     *
     * @param userId 用户 Id
     * @return sseId
     */
    public static SseEmitter registerEmitter(String userId) {
        if (!StringUtils.hasText(userId)) {
            return null;
        }
        String sseId = System.currentTimeMillis() + "-" + UUIDUtil.getUUID();
        SseEmitter emitter = new SseEmitter(0L);

        SSE_EMITTER.put(sseId, emitter);
        SSE_USER.put(sseId, userId);
        // 该 userId 绑定了哪些 sseId
        CopyOnWriteArraySet<String> sseIdSet = USER_SSE.putIfAbsent(userId, new CopyOnWriteArraySet<>());
        if (sseIdSet == null) {
            sseIdSet = USER_SSE.get(userId);
        }
        sseIdSet.add(sseId);

        // Remove the emitter when the client disconnects
        emitter.onCompletion(() -> SseContainer.unregisterEmitter(sseId));
        emitter.onTimeout(() -> SseContainer.unregisterEmitter(sseId));
        emitter.onError((throwable) -> SseContainer.unregisterEmitter(sseId));
        return emitter;
    }

    /**
     * 注销 SseEmitter
     *
     * @param sseId SEE 链接 Id
     */
    public static void unregisterEmitter(String sseId) {
        if (!StringUtils.hasText(sseId)) {
            return;
        }
        SSE_EMITTER.remove(sseId);
        if (SSE_USER.containsKey(sseId)) {
            // 删除正向关系
            String userId = SSE_USER.get(sseId);
            SSE_USER.remove(sseId);
            // 删除反向关系
            CopyOnWriteArraySet<String> sseIdSet = USER_SSE.get(userId);
            if (!CollectionUtils.isEmpty(sseIdSet)) {
                sseIdSet.remove(sseId);
            }
        }
    }

    /**
     * 获取用户注册的的 SseEmitter
     *
     * @param userId 用户 Id
     * @return List<SseEmitter>
     */
    public static List<SseEmitter> findUserEmitters(String userId) {
        CopyOnWriteArraySet<String> sseIdSet = USER_SSE.get(userId);
        if (CollectionUtils.isEmpty(sseIdSet)) {
            return null;
        }
        List<SseEmitter> emitters = new ArrayList<>();
        for (String sseId : sseIdSet) {
            SseEmitter emitter =  SSE_EMITTER.get(sseId);
            if (!ObjectUtils.isEmpty(emitter)) {
                emitters.add(emitter);
            }
        }
        return emitters;
    }

}
