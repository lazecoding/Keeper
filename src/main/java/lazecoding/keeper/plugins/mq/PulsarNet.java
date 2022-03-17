package lazecoding.keeper.plugins.mq;

import org.apache.pulsar.client.admin.PulsarAdminException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import lazecoding.keeper.component.MessageSender;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试
 *
 * @author lazecoding
 */
@RequestMapping("/mq")
@Controller
public class PulsarNet {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * http://localhost:9988/mq/redisTemplate
     *
     * @return
     */
    @RequestMapping(value = "/redisTemplate")
    @ResponseBody
    public String cleanNilSubscriptions() throws PulsarAdminException {

        redisTemplate.opsForValue().set("key:1", "value:1:我");
        redisTemplate.opsForValue().set("key:2", "value:1:是");
        redisTemplate.opsForValue().set("key:三", "value:1:猪");

        List<String> list = new ArrayList<>();
        list.add("key:1");
        list.add("key:2");
        list.add("key:三");

        List<String> values = redisTemplate.opsForValue().multiGet(list);

        System.out.println(values);

        redisTemplate.delete(list);

        values = redisTemplate.opsForValue().multiGet(list);

        System.out.println(values);

        return "redisTemplate";
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
