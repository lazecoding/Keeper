package lazecoding.keeper.plugins.cluster;

import lazecoding.keeper.model.ResponseModel;

import java.util.LinkedList;
import java.util.List;

/**
 * 集群消息
 *
 * @author lazecoding
 */
public class ClusterMessageBean {

    /**
     * 用户访问标识
     */
    private String accessToken;

    /**
     * 响应内容
     */
    private List<ResponseModel> responseModelList = new LinkedList<>();

    public ClusterMessageBean() {
    }

    public ClusterMessageBean(String accessToken, ResponseModel responseModel) {
        this.accessToken = accessToken;
        this.responseModelList.add(responseModel);
    }

    public ClusterMessageBean addResponseModel(ResponseModel responseModel) {
        this.responseModelList.add(responseModel);
        return this;
    }

    public ClusterMessageBean addResponseModelList(List<ResponseModel> responseModelList) {
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
        return "ClusterMessageBean{" +
                "accessToken='" + accessToken + '\'' +
                ", responseModelList=" + responseModelList.toString() +
                '}';
    }
}
