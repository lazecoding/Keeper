package lazecoding.keeper.hander;

import lazecoding.keeper.component.ClientMessageSender;
import lazecoding.keeper.component.SseContainer;
import lazecoding.keeper.exception.NilParamException;
import lazecoding.keeper.model.ClientMessageBean;
import lazecoding.keeper.model.ResultBean;
import lazecoding.keeper.model.WebSocketRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * SSE 处理器
 *
 * @author lazecoding
 */
@RestController()
@RequestMapping("sse")
public class SseHandler {

    private final static Logger logger = LoggerFactory.getLogger(SseHandler.class);

    /**
     * 建立 SSE 连接
     */
    @GetMapping(path = "/register/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter register(@PathVariable String userId) {
        return SseContainer.registerEmitter(userId);
    }


    /**
     * 建立 SSE 连接
     */
    @GetMapping(path = "content", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter content() {
        String userId = "";
        // TODO 通过 token 获取用户
        return SseContainer.registerEmitter(userId);
    }

    /**
     * 断开 SSE 连接
     */
    @GetMapping(path = "/close/{sseId}")
    public ResultBean close(@PathVariable String sseId) {
        if (ObjectUtils.isEmpty(sseId)) {
            throw new NilParamException("sseId is nil");
        }
        ResultBean resultBean = ResultBean.getInstance();
        SseEmitter emitter = SseContainer.SSE_EMITTER.get(sseId);
        String message = "";
        if (!ObjectUtils.isEmpty(emitter)) {
            emitter.complete();
            message = "断开链接";
        } else {
            message = "链接已断开";
        }
        resultBean.setMessage(message);
        return resultBean;
    }


    /**
     * 客户端推送消息
     */
    @GetMapping(path = "/send")
    @ResponseBody
    public ResultBean send(@RequestBody WebSocketRequest request) {
        ResultBean resultBean = ResultBean.getInstance();
        if (ObjectUtils.isEmpty(request)) {
            throw new NilParamException("request is nil");
        }
        // TODO 通过 token 获取用户
        String userId = "";
        String app = request.getApp();
        String event = request.getEvent();
        if (!StringUtils.hasText(app)) {
            throw new NilParamException("request.app is nil");
        }
        if (!StringUtils.hasText(event)) {
            throw new NilParamException("request.event is nil");
        }
        ClientMessageBean clientMessageBean = new ClientMessageBean(app, event, request.getData(), userId);
        boolean isSuccess = false;
        String message = null;
        try {
            ClientMessageSender.sendClientMessage(clientMessageBean);
            isSuccess = true;
            message = "发送成功";
        } catch (Exception e) {
            logger.error("ClientMessageSender.sendClientMessage Exception", e);
            message = "发送失败";
        }
        resultBean.setSuccess(isSuccess);
        resultBean.setMessage(message);
        return resultBean;
    }


    @GetMapping(path = "/monitor")
    @ResponseBody
    public ResultBean monitor() {
        ResultBean resultBean = ResultBean.getInstance();
        resultBean.addData("SSE_EMITTER", SseContainer.SSE_EMITTER);
        resultBean.addData("SSE_USER", SseContainer.SSE_USER);
        resultBean.addData("USER_SSE", SseContainer.USER_SSE);
        return resultBean;
    }

}
