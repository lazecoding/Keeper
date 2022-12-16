package lazecoding.keeper.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lazecoding.keeper.component.MessageSender;
import lazecoding.keeper.exception.NilParamException;
import lazecoding.keeper.model.MessageBody;
import lazecoding.keeper.model.ResultBean;
import lazecoding.keeper.model.WebSocketResult;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

/**
 * @author lazecoding
 */
@Controller
@RequestMapping("interface/sender")
public class HttpSender {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 指定用户推送
     */
    @PostMapping("send")
    @ResponseBody
    public ResultBean send(String app, String event, String data, String userIds) {
        if (!StringUtils.hasText(app)) {
            throw new NilParamException("app is nil.");
        }
        if (!StringUtils.hasText(event)) {
            throw new NilParamException("event is nil.");
        }
        if (!StringUtils.hasText(data)) {
            throw new NilParamException("data is nil.");
        }
        if (!StringUtils.hasText(userIds)) {
            throw new NilParamException("userIds is nil.");
        }

        ResultBean resultBean = ResultBean.getInstance();
        boolean isSuccess = false;
        String message = "";
        try {
            // data = StringEscapeUtils.unescapeHtml(data);
            // 表示由消息方组织的消息体
            MessageBody messageBody = new MessageBody(app, event, data);
            List<String> userIdList = Arrays.asList(userIds.split(","));
            // TODO 中间经过 MQ 广播
            // TODO 从 MQ 中收到了 messageBody，组织成 WebSocketResult，返回给客户端
            WebSocketResult webSocketResult = new WebSocketResult(messageBody.getApp(), messageBody.getEvent(), messageBody.getData(), messageBody.getTraceId());
            String objJson = MAPPER.writeValueAsString(webSocketResult);
            // 推送给客户端
            MessageSender.sendLocalMessageToUser(userIdList, objJson);
            isSuccess = true;
        } catch (NilParamException e) {
            isSuccess = false;
            message = e.getMessage();
        } catch (Exception e) {
            isSuccess = false;
            message = "系统异常";
        }
        resultBean.setSuccess(isSuccess);
        resultBean.setMessage(message);
        return resultBean;
    }

    /**
     * 指定用户推送
     */
    @PostMapping("send-all")
    @ResponseBody
    public ResultBean sendAll(String app, String event, String data) {
        if (!StringUtils.hasText(app)) {
            throw new NilParamException("app is nil.");
        }
        if (!StringUtils.hasText(event)) {
            throw new NilParamException("event is nil.");
        }
        if (!StringUtils.hasText(data)) {
            throw new NilParamException("data is nil.");
        }
        ResultBean resultBean = ResultBean.getInstance();
        boolean isSuccess = false;
        String message = "";
        try {
            // data = StringEscapeUtils.unescapeHtml(data);
            MessageBody messageBody = new MessageBody(app, event, data);
            // TODO 中间经过 MQ 广播
            // TODO 从 MQ 中收到了 messageBody，组织成 WebSocketResult，返回给客户端
            WebSocketResult webSocketResult = new WebSocketResult(messageBody.getApp(), messageBody.getEvent(), messageBody.getData(), messageBody.getTraceId());
            String objJson = MAPPER.writeValueAsString(webSocketResult);
            // 推送给客户端
            MessageSender.sendLocalMessageForBroadcast(objJson);
            isSuccess = true;
        } catch (Exception e) {
            isSuccess = false;
            message = "系统异常";
        }
        resultBean.setSuccess(isSuccess);
        resultBean.setMessage(message);
        return resultBean;
    }

}
