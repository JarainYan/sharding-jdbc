package com.example.sharding.config;

/**
 * @Auther: zyan
 * @Date: 2021/8/16 11:08
 * @Description: TableShardingAlgorithm
 * @Version 1.0.0
 */

import java.util.Collection;
import java.util.Date;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

public class TableShardingAlgorithm implements PreciseShardingAlgorithm<Date> {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Date> preciseShardingValue) {
        String tb_name = preciseShardingValue.getLogicTableName();
        String new_tb_name = preciseShardingValue.getLogicTableName() + "_";
        Boolean flag = false;
        try {
            Date date = preciseShardingValue.getValue();
            String year = String.format("%tY", date);
            String mon = String.format("%tm", date);
//            String dat = String.format("%td", date);
//            new_tb_name = tb_name + year + mon + dat;
            new_tb_name = new_tb_name + year + mon;
            System.out.println("new_tb_name:" + new_tb_name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String each : collection) {
            System.out.println("t_order_:" + each);
            if (each.equals(new_tb_name)) {
                flag = true;
                return each;
            }
        }
        //如果表不存在 返回原始表
        if(!flag){
            new_tb_name = tb_name;
            return new_tb_name;
        }

        throw new IllegalArgumentException();

    }

}
