package com.example.sharding.config;

/**
 * @Auther: zyan
 * @Date: 2021/8/16 11:08
 * @Description: TableShardingAlgorithm
 * @Version 1.0.0
 */

import com.example.sharding.utils.DateUtil;
import com.example.sharding.utils.TimeUtil;
import com.google.common.collect.Range;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

@Slf4j
public class TableShardingAlgorithmRan implements RangeShardingAlgorithm<Date> {


    public static String START_TIME;
    @Value("${sharding.jdbc.start-time}")
    private void setStartTime(String startTime) {
        START_TIME = startTime;
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Date> rangeShardingValue) {

        CopyOnWriteArrayList<String> targetNames = new CopyOnWriteArrayList<String>();
        Range<Date> valueRange = rangeShardingValue.getValueRange();
        Integer lowerYear = null, upperYear = null;
        if (valueRange.hasLowerBound()) {
            lowerYear = Integer.parseInt(DateUtil.date2Str(valueRange.lowerEndpoint(), DateUtil.YEAR_MONTH_DAY_NUMBER));
        }
        if (valueRange.hasUpperBound()) {
            upperYear = Integer.parseInt(DateUtil.date2Str(valueRange.upperEndpoint(), DateUtil.YEAR_MONTH_DAY_NUMBER));
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
        if(targetNames.size()==0){
            targetNames.add(rangeShardingValue.getLogicTableName());
        }
        return targetNames;
    }

    private boolean isEquals(Date upperYearValue) {
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
