package com.sunshine.sharding.algorithm;


import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;
import java.util.Date;

public class TableShardingAlgorithm implements PreciseShardingAlgorithm<Date> {

    //Collection<String> doSharding(Collection<String> availableTargetNames, ComplexKeysShardingValue<T> shardingValue);
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Date> preciseShardingValue) {


        String tb_name = preciseShardingValue.getLogicTableName() + "_";
        return tb_name;
 /*
        String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(preciseShardingValue.getValue());
        System.out.println("tb_name:" + yyyyMMdd);
        tb_name = tb_name + yyyyMMdd;


        for (String each : collection) {
            System.out.println("tb:" + each);
            if (each.equals(tb_name)) {
                return each;
            }
        }
        throw new IllegalArgumentException();*/
    }
}