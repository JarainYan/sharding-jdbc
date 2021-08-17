package com.example.sharding.config;

import com.example.sharding.utils.DateUtil;
import com.example.sharding.utils.TimeUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Auther: zyan
 * @Date: 2021/8/17 10:17
 * @Description: MyDBComplexKeysShardingAlgorithm
 * @Version 1.0.0
 */
@Slf4j
public class MyDBComplexKeysShardingAlgorithm implements ComplexKeysShardingAlgorithm<String> {
    @Override
    public Collection<String> doSharding(Collection<String> collection, ComplexKeysShardingValue<String> complexKeysShardingValue) {
        List<Range<String>> create_times = getShardingRangeValue(complexKeysShardingValue, "create_time");
        Collection<String> user_ids = getShardingValue(complexKeysShardingValue, "user_id");
        Collection<String> seller_ids = getShardingValue(complexKeysShardingValue, "seller_id");

        ArrayList<String> userIdTables = Lists.newArrayList();
        ArrayList<String> sellerIdTables = Lists.newArrayList();
        List<String> createTimeTables = Lists.newCopyOnWriteArrayList();

        if(!CollectionUtils.isEmpty(create_times)){
            createTimeTables = getTableNames(collection, create_times);
        }
        if(!CollectionUtils.isEmpty(user_ids)){
            for (Object user_id : user_ids) {
                String tableName = complexKeysShardingValue.getLogicTableName() + "_" + (Long.valueOf(String.valueOf(user_id)) % 2 + 1);
                if(collection.contains(tableName)){
                    userIdTables.add(tableName);
                }
            }
        }
        if(!CollectionUtils.isEmpty(seller_ids)){
            seller_ids.forEach(seller_id->{
                String tableName = complexKeysShardingValue.getLogicTableName() + "_" + (Long.valueOf(seller_id) % 2 + 1);
                if(collection.contains(tableName)){
                    sellerIdTables.add(tableName);
                }
            });
        }
        return null;
    }

    private Collection<String> getShardingValue(ComplexKeysShardingValue<String> shardingValues, final String key) {
        Collection<String> valueSet = new ArrayList<>();
        Map<String, Collection<String>> columnNameAndShardingValuesMap = shardingValues.getColumnNameAndShardingValuesMap();
        if (columnNameAndShardingValuesMap.containsKey(key)) {
            valueSet.addAll(columnNameAndShardingValuesMap.get(key));
        }
        return valueSet;
    }

    private List<Range<String>> getShardingRangeValue(ComplexKeysShardingValue<String> shardingValues, final String key) {
        List<Range<String>> valueSet = new ArrayList<>();
        Map<String, Range<String>> columnNameAndRangeValuesMap = shardingValues.getColumnNameAndRangeValuesMap();
        if(!ObjectUtils.isEmpty(columnNameAndRangeValuesMap)&&columnNameAndRangeValuesMap.containsKey(key)){
            valueSet.add(columnNameAndRangeValuesMap.get(key));
        }
        return valueSet;
    }

    private List<String> getTableNames(Collection<String> availableTargetNames,List<Range<String>> ranges ){
        CopyOnWriteArrayList<String> targetNames = new CopyOnWriteArrayList<String>();
        Range<String> valueRange = ranges.get(0);
        Integer lowerYear = null, upperYear = null;
        if (valueRange.hasLowerBound()) {
            lowerYear = Integer.parseInt(DateUtil.date2Str(TimeUtil.stringToDate(valueRange.lowerEndpoint(),TimeUtil.WHOLE_TIME), DateUtil.YEAR_MONTH_DAY_NUMBER));
        }
        if (valueRange.hasUpperBound()) {
            upperYear = Integer.parseInt(DateUtil.date2Str(TimeUtil.stringToDate(valueRange.upperEndpoint(),TimeUtil.WHOLE_TIME), DateUtil.YEAR_MONTH_DAY_NUMBER));
        }
        if (lowerYear == null) {
            lowerYear = Integer.parseInt(DateUtil.date2Str(TimeUtil.getMonthBegin(new Date()), DateUtil.YEAR_MONTH_DAY_NUMBER));
        }
        if (upperYear == null) {
            upperYear = Integer.parseInt(DateUtil.date2Str(new Date(), DateUtil.YEAR_MONTH_DAY_NUMBER));
        }
        for (String targetName : availableTargetNames) {

            Date lowerYearValue = DateUtil.str2Date(lowerYear.toString(), DateUtil.YEAR_MONTH_DAY_NUMBER);
            Date upperYearValue = DateUtil.str2Date(upperYear.toString(), DateUtil.YEAR_MONTH_DAY_NUMBER);

            if (isEquals(upperYearValue)) {
                upperYearValue = new Date();
            }

            String upperYearMonth = getYearMonth( upperYearValue, 0);
            String yearMonth = null;
            while (true) {
                if (null == yearMonth) {
                    yearMonth = getYearMonth( lowerYearValue, 0);
                } else {
                    log.debug("----+++++++---yearMonth:{}------upperYearMonth:{}----------", yearMonth, upperYearMonth);
                    yearMonth = getYearMonth( DateUtil.str2Date(yearMonth, DateUtil.YEAR_MONTH_NUMBER), 1);
                }

                log.debug("-------yearMonth:{}------upperYearMonth:{}----------", yearMonth, upperYearMonth);
                if (Integer.valueOf(upperYearMonth)<(Integer.valueOf(yearMonth))) {
                    break;
                }
                if(targetName.contains(yearMonth)){
                    targetNames.add(targetName);
                }
            }

        }
        //查询表是否存在 不存在 去除
        for(String tableName : targetNames){
            if(!availableTargetNames.contains(tableName)){
                targetNames.remove(tableName);
            }
        }
        return targetNames;
    }

    boolean isEquals(Date upperYearValue) {
        Date tupperYearValue = DateUtil.dateFormat(upperYearValue, DateUtil.YEAR_MONTH_NUMBER);
        Date nowDate = DateUtil.dateFormat(new Date(), DateUtil.YEAR_MONTH_NUMBER);

        //log.debug("upperYearValue:{}, tupperYearValue:{}, nowDate:{}",upperYearValue, tupperYearValue, nowDate);
        if (tupperYearValue.after(nowDate)) {
            return true;
        }

        if (tupperYearValue.equals(nowDate)) {
            return true;
        }


        return false;
    }

    //实现日期加一天的方法

    public  String getYearMonth(Date date, int n) {

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

            Calendar calendar = Calendar.getInstance();

            calendar.setTime(date);

            calendar.add(Calendar.MONTH, n);// 增加一天
            return sdf.format(calendar.getTime());

        } catch (Exception e) {

            return null;

        }
    }
}
