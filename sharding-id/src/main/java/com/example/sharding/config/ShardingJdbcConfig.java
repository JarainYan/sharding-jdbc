package com.example.sharding.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Auther: zyan
 * @Date: 2021/8/12 17:26
 * @Description: ShardingJdbcConfig
 * @Version 1.0.0
 */

@Configuration
public class ShardingJdbcConfig {
    // 定义数据源
    Map<String, DataSource> createDataSourceMap() {
        DruidDataSource dataSource1 = new DruidDataSource();
        dataSource1.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource1.setUrl("jdbc:mysql://localhost:3306/order_db?useUnicode=true");
        dataSource1.setUsername("root");
        dataSource1.setPassword("Altran2021@@@");
        Map<String, DataSource> result = new HashMap<>();
        result.put("m1", dataSource1);
        return result;
    }
    // 定义主键生成策略
    private static KeyGeneratorConfiguration getKeyGeneratorConfiguration() {
        KeyGeneratorConfiguration result = new
                KeyGeneratorConfiguration("SNOWFLAKE","order_id");
        return result;
    }

    private static KeyGeneratorConfiguration getUserKeyGeneratorConfiguration() {
        KeyGeneratorConfiguration result = new
                KeyGeneratorConfiguration("SNOWFLAKE","user_id");
        return result;
    }
    // 定义t_order表的分片策略
    TableRuleConfiguration getOrderTableRuleConfiguration() {
        TableRuleConfiguration result = new TableRuleConfiguration("t_order","m1.t_order_${1..2}");
        result.setTableShardingStrategyConfig(new
                InlineShardingStrategyConfiguration("order_id", "t_order_${order_id % 2 + 1}"));
        result.setKeyGeneratorConfig(getKeyGeneratorConfiguration());
        return result;
    }
    // 定义t_user表的分片策略
    TableRuleConfiguration getUserTableRuleConfiguration() {
        TableRuleConfiguration result = new TableRuleConfiguration("t_user","m1.t_user_${1..2}");
        result.setTableShardingStrategyConfig(new
                InlineShardingStrategyConfiguration("user_id", "t_user_${user_id % 2 + 1}"));
        result.setKeyGeneratorConfig(getUserKeyGeneratorConfiguration());
        return result;
    }
    // 定义sharding‐Jdbc数据源
    @Bean
    DataSource getShardingDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(getOrderTableRuleConfiguration());
        shardingRuleConfig.getTableRuleConfigs().add(getUserTableRuleConfiguration());
//spring.shardingsphere.props.sql.show = true
        Properties properties = new Properties();
        properties.put("sql.show","true");
        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(),shardingRuleConfig,properties);
    }
}
