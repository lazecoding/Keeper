package lazecoding.keeper.plugins.batch;

import lazecoding.keeper.constant.RequestType;

/**
 * @author lazecoding
 */
public class BatchMain {
    public static void main(String[] args) {
        // 注册 BatchExecutor
        BatchExecutor.registered();

        // 处理
        BatchResponseModel batchResponseModel = new BatchResponseModel(RequestType.T_1.getCode(), "requestContext");
        BatchExecutor.offer(new BatchRequestBean("111", batchResponseModel).addResponseModel(batchResponseModel).addResponseModel(batchResponseModel));



        if (BatchExecutor.isEmpty()) {
            System.out.println("BatchExecutor.isEmpty()");
        }
        System.out.println("BatchExecutor  End.");

    }
}
