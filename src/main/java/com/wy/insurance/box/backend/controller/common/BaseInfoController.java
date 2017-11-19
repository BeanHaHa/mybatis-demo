package com.wy.insurance.box.backend.controller.common;

import com.alibaba.fastjson.JSON;
import com.wy.insurance.box.backend.service.order.ClientOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author daobin<wdb@winbaoxian.com>
 * @date 2017/11/17.
 */
@RestController
@RequestMapping("box/common")
public class BaseInfoController {

    @Autowired
    ClientOrderService orderService;

    @RequestMapping(value = "/test")
    public String test() throws Exception {
        String result = JSON.toJSONString(orderService.getOrderByUUid("788289983d324262bd37446ac0fd094688"));

        return result;
    }

    @RequestMapping(value = "/chinese")
    public String chinese() throws Exception {

        return "result: 中文乱码";
    }
}
