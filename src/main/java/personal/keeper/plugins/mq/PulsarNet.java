package personal.keeper.plugins.mq;

import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.CompletableFuture;

/**
 * 测试
 *
 * @author lazecoding
 */
@RequestMapping("/mq")
@Controller
public class PulsarNet {

    /**
     * http://localhost:9988/mq/topic1?message=qweasdzxc
     *
     * @param message
     * @return
     */
    @RequestMapping(value = "/topic1")
    @ResponseBody
    public String topic1(String message) throws PulsarClientException, PulsarAdminException {
        Producer<byte[]> producer = PulsarProducer.getInstance(MqConstant.MESSAGE_SYNC.getTopicName());
        // producer.send(("send > " + message).getBytes());

        CompletableFuture<MessageId> futureAsync = producer.newMessage()
                .key("sendAsync-key")
                .value(("sendAsync >" + message).getBytes())
                .sendAsync();

        futureAsync.handle((v, ex) -> {
            if (ex == null) {
                v.toByteArray();
                // System.out.println("futureAsync 成功");
            } else {
                // System.out.println("futureAsync 失败");
            }
            return null;
        });

        PulsarManager.cleanNilSubscriptions();

        return message;
    }
}
