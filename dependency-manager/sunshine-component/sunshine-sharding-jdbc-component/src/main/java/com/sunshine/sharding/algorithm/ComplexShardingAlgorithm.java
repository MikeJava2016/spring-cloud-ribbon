package com.sunshine.sharding.algorithm;/*
package com.gupaoedu.algorithm;

import org.apache.shardingsphere.api.sharding.ShardingValue;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;

import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.Collection;
import java.util.Map;


public class ComplexShardingAlgorithm<String> implements ComplexKeysShardingAlgorithm {
    @Override
    public Collection<java.lang.String> doSharding(Collection availableTargetNames, ComplexKeysShardingValue shardingValue) {
        return null;
    }
    */
/*@Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ComplexKeysShardingValue shardingValue) {
        System.out.println("collection:" + availableTargetNames + ",shardingValues:" + shardingValue);

        Map valueColumnNameAndRangeValuesMap = shardingValue.getColumnNameAndRangeValuesMap();
        Map columnNameAndShardingValuesMap = shardingValue.getColumnNameAndShardingValuesMap();

        return null;
  Collection<Integer> orderIdValues = getShardingValue(columnNameAndShardingValuesMap, "order_id");
        Collection<Integer> userIdValues = getShardingValue(shardingValues, "user_id");


 List<String> shardingSuffix = new ArrayList<>();

        // user_id，order_id分片键进行分表
        for (Integer userId : userIdValues) {
            for (Integer orderId : orderIdValues) {
                String suffix = userId % 2 + "_" + orderId % 2;
                for (String s : collection) {
                    if (s.endsWith(suffix)) {
                        shardingSuffix.add(s);
                    }
                }
            }
        }

        return shardingSuffix;

    }*//*



*
     * 例如: SELECT * FROM T_ORDER user_id = 100000 AND order_id = 1000009
     * 循环 获取SQL 中 分片键列对应的value值
     * @param shardingValues sql 中分片键的value值   -> 1000009
     * @param key 分片键列名                        -> user_id
     * @return shardingValues 集合                 -> [1000009]


 private Collection<Integer> getShardingValue(Collection<ShardingValue> shardingValues, final String key) {
        Collection<Integer> valueSet = new ArrayList<>();
        Iterator<ShardingValue> iterator = shardingValues.iterator();
        while (iterator.hasNext()) {
            ShardingValue next = iterator.next();
            if (next instanceof ListShardingValue) {
                ListShardingValue value = (ListShardingValue) next;
                // user_id，order_id分片键进行分表
                if (value.getColumnName().equals(key)) {
                    return value.getValues();
                }
            }
        }
        return valueSet;
    }


}
*/
