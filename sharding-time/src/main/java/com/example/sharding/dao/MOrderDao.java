package com.example.sharding.dao;

import com.example.sharding.entity.MOrderDo;
import com.example.sharding.entity.OrderDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @Auther: zyan
 * @Date: 2021/8/17 10:55
 * @Description: MOrderDao
 * @Version 1.0.0
 */

@Repository
public interface MOrderDao extends JpaRepository<MOrderDo,Long>, JpaSpecificationExecutor<MOrderDo> {

    @Query(nativeQuery = true,
            value = "select * from m_order m " +
                    "where user_id = :userId and seller_id = :sellerId and create_time between :startTime and :endTime")
    MOrderDo getMOrderDoByuserIdSellerIdTime(@Param("userId") Long userId,@Param("sellerId") Long sellerId
            ,@Param("startTime") String startTime,@Param("endTime") String endTime);

}
