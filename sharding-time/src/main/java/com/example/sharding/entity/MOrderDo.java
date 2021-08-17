package com.example.sharding.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: zyan
 * @Date: 2021/8/12 15:51
 * @Description: order
 * @Version 1.0.0
 */

@Entity
@Table(name = "m_order")
@Data
public class MOrderDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long OrderId;
    private BigDecimal price;
    private Long userId;
    private Long sellerId;
    private Long productId;
    private String user_name;
    private String status;
    private Date createTime;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "userId", referencedColumnName = "userId",
//            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT),
//            insertable = false, updatable = false)
//    private UserDo userDo;
}
