package lazecoding.keeper.http;

import lazecoding.keeper.model.ResultBean;
import lazecoding.keeper.service.ServerKeepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ServerCleaner
 *
 * @author lazecoding
 */
@Controller
@RequestMapping("interface/cleaner")
public class ServerCleaner {

    @Autowired
    private ServerKeepService serverKeepService;

    /**
     * 指定用户推送
     */
    @GetMapping("do-clean")
    @ResponseBody
    public ResultBean doClean() {
        ResultBean resultBean = ResultBean.getInstance();
        boolean isSuccess = false;
        String message = "";
        try {
            serverKeepService.clean();
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
