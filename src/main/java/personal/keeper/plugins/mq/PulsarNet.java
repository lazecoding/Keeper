package personal.keeper.plugins.mq;

import org.apache.pulsar.client.admin.PulsarAdminException;
 import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import personal.keeper.component.MessageSender;

/**
 * 测试
 *
 * @author lazecoding
 */
@RequestMapping("/mq")
@Controller
public class PulsarNet {

    /**
     * http://localhost:9988/mq/cleanNilSubscriptions
     *
     * @return
     */
    @RequestMapping(value = "/cleanNilSubscriptions")
    @ResponseBody
    public String cleanNilSubscriptions() throws PulsarAdminException {

        PulsarManager.cleanNilSubscriptions();

        return "cleanNilSubscriptions";
    }

    /**
     * http://localhost:9988/mq/broadcast
     *
     * @return
     */
    @RequestMapping(value = "/broadcast")
    @ResponseBody
    public String broadcast() {

        MessageSender.sendMessageForBroadcast("我是广播哦");

        return "sendMessageForBroadcast";
    }
}
