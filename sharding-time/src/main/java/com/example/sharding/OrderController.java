package com.example.sharding;

import com.example.sharding.entity.OrderDo;
import com.example.sharding.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Auther: zyan
 * @Date: 2021/8/12 14:53
 * @Description: OrderController
 * @Version 1.0.0
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @RequestMapping("/insertOrder")
    public int insertOrder(BigDecimal price, Long userId, String status) {
        return orderService.insertOrder(price,userId,status);
    }

    @RequestMapping("/selectOrderbyIds")
    public List<OrderDo> selectOrderbyIds(List<Long> orderIds) {
        return orderService.selectOrderbyIds(orderIds);
    }
}
