package com.wy.insurance.box.backend.util;

import com.wy.insurance.box.backend.model.ResponsePageWrapper;
import com.wy.insurance.box.backend.model.ResponseWrapper;

/**
 * @description 封装请求结果
 * @author: daobin<wdb@wy.cn>
 * @date: 2016/08/22 18:44
 */
public class PackageReqResultUtils {

    /**
     * 返回请求成功结果
     *
     * @param data 请求结果集
     * @return
     */
    public static ResponseWrapper returnSuccessWrapper(Object data) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(Constants.RET_CODE_SUCCESS);
        responseWrapper.setMsg(Constants.RET_MSG_SUCCESS);
        responseWrapper.setData(data);
        return responseWrapper;
    }

    /**
     * 返回请求失败结果
     *
     * @return
     */
    public static ResponseWrapper returnFailedWrapper() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(Constants.RET_CODE_FAILED);
        responseWrapper.setMsg(Constants.RET_MSG_FAILED);
        return responseWrapper;
    }

    /**
     * 返回分页数据
     *
     * @param total
     * @param rows
     * @return
     */
    public static ResponseWrapper returnPagenationWrapper(Long total, Object rows) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(Constants.RET_CODE_SUCCESS);
        responseWrapper.setMsg(Constants.RET_MSG_SUCCESS);
        responseWrapper.setData(new ResponsePageWrapper(total, rows));
        return responseWrapper;
    }
}
