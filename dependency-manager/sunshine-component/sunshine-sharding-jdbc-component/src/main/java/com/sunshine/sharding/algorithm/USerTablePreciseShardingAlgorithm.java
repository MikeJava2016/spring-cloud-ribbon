package com.sunshine.sharding.algorithm;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

public class USerTablePreciseShardingAlgorithm implements PreciseShardingAlgorithm<Date> {

    private final static Logger logger = LoggerFactory.getLogger(USerTablePreciseShardingAlgorithm.class);

    public USerTablePreciseShardingAlgorithm(){
        System.out.println("USerTablePreciseShardingAlgorithm1223");
    }

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Date> preciseShardingValue) {
        System.out.println(Arrays.toString(collection.toArray()));
        try {
            String tableName = preciseShardingValue.getLogicTableName();
            logger.info("tableName--------------{}",tableName);
            String dataTime = formatter.format(preciseShardingValue.getValue());
            return tableName.concat(dataTime);
        }catch (Exception e){
            throw new IllegalArgumentException("没有匹配到库:" + preciseShardingValue.getValue());
        }
    }
}
