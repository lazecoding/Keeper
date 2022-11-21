package lazecoding.keeper.plugins.batch;

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
    private List<BatchResponseModel> batchResponseModelList = new LinkedList<>();

    public BatchRequestBean() {
    }

    public BatchRequestBean(String accessToken, BatchResponseModel batchResponseModel) {
        this.accessToken = accessToken;
        this.batchResponseModelList.add(batchResponseModel);
    }

    public BatchRequestBean addResponseModel(BatchResponseModel batchResponseModel) {
        this.batchResponseModelList.add(batchResponseModel);
        return this;
    }

    public BatchRequestBean addResponseModelList(List<BatchResponseModel> batchResponseModelList) {
        this.batchResponseModelList.addAll(batchResponseModelList);
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<BatchResponseModel> getResponseModelList() {
        return batchResponseModelList;
    }

    public void setResponseModelList(List<BatchResponseModel> batchResponseModelList) {
        this.batchResponseModelList = batchResponseModelList;
    }

    @Override
    public String toString() {
        return "BatchRequestBean{" +
                "accessToken='" + accessToken + '\'' +
                ", responseModelList=" + batchResponseModelList +
                '}';
    }
}
