package com.example.sharding.dao;

import com.example.sharding.entity.OrderDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Auther: zyan
 * @Date: 2021/8/12 14:10
 * @Description: Order_1
 * @Version 1.0.0
 */
@Repository
public interface OrderDao extends JpaRepository<OrderDo,Long>, JpaSpecificationExecutor<OrderDo> {
    /**
     * 新增订单
     * @param price 订单价格
     * @param userId 用户id
     * @param status 订单状态
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = " insert into t_order(price,user_id,status,product_id) value(:price,:userId,:status,:productId)",nativeQuery = true)
    int insertOrder(@Param("price") BigDecimal price, @Param("userId")Long userId,
                    @Param("status")String status,@Param("productId")String productId);

    /**
     * 新增用户
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = " insert into t_user(`name`,age) value(:name,:age)",nativeQuery = true)
    int insertUser(@Param("name") String name, @Param("age")Integer age);
    /**
     * 根据id列表查询多个订单
     * @param orderIds 订单id列表
     * @return
     */
    @Query(
            value = "select  * " +
            " from t_order t" +
            " where t.order_id in :orderIds",nativeQuery = true)
    List<OrderDo> selectOrderbyIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 根据商品id列表查询多个订单  联合查询
     * @param productId 商品id
     * @return
     */
    @Query(
            value = "select  * " +
                    " from t_order t left join product p on t.product_id = p.id " +
                    " where p.id = :productId",nativeQuery = true)
    List<Map> selectOrderbyProductId(@Param("productId") Long productId);

    /**
     * 新增商品
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = " insert into product(p_price,name) value(:price,:name)",nativeQuery = true)
    int insertProduct(@Param("price") BigDecimal price, @Param("name")String name);



    /**
     * 根据用户id列表查询订单  联合查询
     * @param userId 用户id
     * @return
     */
    @Query(
            value = "select  t.order_id " +
                    " from t_order t left join t_user u on t.user_id = u.user_id " +
                    " where u.user_id = :userId",nativeQuery = true)
    List<Map> selectOrderbUserId(@Param("userId") Long userId);

    /**
     * 根据多个用户id列表查询订单  联合查询
     * @param userIds 用户id
     * @return
     */
    @Query(
            value = "select  t.order_id " +
                    " from t_order t left join t_user u on t.user_id = u.user_id " +
                    " where u.user_id in :userIds",nativeQuery = true)
    List<Map> selectOrderbUserIds(@Param("userIds") List<Long> userIds);


    /**
     * 根据用户名称列表查询订单  联合查询
     * @param userName 用户名字
     * @return
     */
    @Query(
            value = "select  t.order_id " +
                    " from t_order t left join t_user u on t.user_name = u.name " +
                    " where u.name = :userName",nativeQuery = true)
    List<Map> selectOrderbUserName(@Param("userName") String userName);
}

