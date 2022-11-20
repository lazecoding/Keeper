package lazecoding.keeper.plugins.batch;

import lazecoding.keeper.model.ResponseModel;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 批量请求
 *
 * @author lazecoding
 */
public class BatchRequestBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户访问标识
     */
    private String accessToken;

    /**
     * 响应内容
     */
    private List<ResponseModel> responseModelList = new LinkedList<>();

    public BatchRequestBean() {
    }

    public BatchRequestBean(String accessToken, ResponseModel responseModel) {
        this.accessToken = accessToken;
        this.responseModelList.add(responseModel);
    }

    public BatchRequestBean addResponseModel(ResponseModel responseModel) {
        this.responseModelList.add(responseModel);
        return this;
    }

    public BatchRequestBean addResponseModelList(List<ResponseModel> responseModelList) {
        this.responseModelList.addAll(responseModelList);
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<ResponseModel> getResponseModelList() {
        return responseModelList;
    }

    public void setResponseModelList(List<ResponseModel> responseModelList) {
        this.responseModelList = responseModelList;
    }

    @Override
    public String toString() {
        return "BatchRequestBean{" +
                "accessToken='" + accessToken + '\'' +
                ", responseModelList=" + responseModelList +
                '}';
    }
}
