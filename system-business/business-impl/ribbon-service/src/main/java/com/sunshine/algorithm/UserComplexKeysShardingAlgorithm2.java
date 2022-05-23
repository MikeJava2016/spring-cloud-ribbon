package com.sunshine.algorithm;

import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
public class UserComplexKeysShardingAlgorithm2 implements ComplexKeysShardingAlgorithm {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");


    @Override
    public Collection<String> doSharding(Collection availableTargetNames, ComplexKeysShardingValue shardingValue) {
        Map columnNameAndShardingValuesMap1 = shardingValue.getColumnNameAndShardingValuesMap();
        Map<String,Object> columnNameAndShardingValuesMap = columnNameAndShardingValuesMap1;
        Set allTables = (Set) availableTargetNames;//  user_info20210517  user_info20210516  user_info
        String logicTableName = shardingValue.getLogicTableName();// user_info
        System.out.println("所有的sharding列的值为"+columnNameAndShardingValuesMap);
        Date createDate = (Date) (((List) shardingValue.getColumnNameAndShardingValuesMap().get("create_date")).get(0));
       // Long userId = (Long) (((List) shardingValue.getColumnNameAndShardingValuesMap().get("user_id")).get(0));
        System.out.println("createDate = "+ createDate);
//        System.out.println("userId = "+ userId);
        String tableName = formatter.format(createDate);
        tableName = logicTableName.concat(tableName);
        Set<String> result = new HashSet<>();
        result.add(tableName);
        return result;
    }
}
