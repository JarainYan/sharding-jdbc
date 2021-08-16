package com.example.sharding.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @Auther: zyan
 * @Date: 2021/8/13 11:24
 * @Description: UserDo
 * @Version 1.0.0
 */
@Entity
@Table(name = "t_user")
@Data
public class UserDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;
    private Integer age;

}
