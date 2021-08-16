package com.example.sharding.service.impl;

import com.example.sharding.dao.OrderDao;
import com.example.sharding.entity.OrderDo;
import com.example.sharding.service.OrderService;
import com.example.sharding.utils.TimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: zyan
 * @Date: 2021/8/12 14:51
 * @Description: OrderServiceImpl
 * @Version 1.0.0
 */
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao_1;
    public OrderServiceImpl(OrderDao orderDao_1) {
        this.orderDao_1 = orderDao_1;
    }

    @Override

    public int insertOrder(BigDecimal price, Long userId, String status) {
        return orderDao_1.insertOrder(price,userId,status,"9", new Date());
    }

    @Override
    public List<OrderDo> selectOrderbyIds(List<Long> orderIds) {
        return null;
    }
}
