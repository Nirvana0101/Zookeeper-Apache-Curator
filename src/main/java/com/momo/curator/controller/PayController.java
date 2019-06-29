package com.momo.curator.controller;

import com.momo.curator.service.PayService;
import com.momo.curator.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class PayController {
    @Autowired
    private PayService payService;
    @RequestMapping("/buy")
    @ResponseBody
    public JSONResult buy(String itemId) {
        boolean result = payService.buy(itemId);
        return JSONResult.ok(result ? "订单创建成功..." : "订单创建失败...");
    }
    @RequestMapping("/buy1")
    @ResponseBody
    public JSONResult buy1(String itemId) {
        boolean result = payService.buy(itemId);
        return JSONResult.ok(result ? "订单创建成功..." : "订单创建失败...");
    }

}
