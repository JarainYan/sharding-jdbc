package com.example.sharding.service;

import com.example.sharding.entity.OrderDo;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Auther: zyan
 * @Date: 2021/8/12 14:51
 * @Description: OrderService
 * @Version 1.0.0
 */

public interface OrderService {
    int insertOrder( BigDecimal price,Long userId,
                    String status);
    List<OrderDo> selectOrderbyIds(List<Long> orderIds);
}
