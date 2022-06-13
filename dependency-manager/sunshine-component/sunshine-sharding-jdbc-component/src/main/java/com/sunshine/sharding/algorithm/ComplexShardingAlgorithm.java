package com.sunshine.sharding.algorithm;

import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 复合分片算法
 * 根据订单id(orderId)和客户id(customerId)后2位计算
 * 订单id 包含客户id 的后2位
 * 以客户id的后2位来确定是路由到那个表中
 * 1、目前处理 = 和 in 操作，其余的操作，比如 >、< 等不支持。
 *
 * @author huan.fu 2021/5/25 - 上午9:48
 * https://www.jianshu.com/p/4aed141b3000
 */


//如何扩展一致性hash算法实现分库分表
public class ComplexShardingAlgorithm implements ComplexKeysShardingAlgorithm<String> {
    /**
     * 订单id列名
     */
    private static final String COLUMN_ORDER_ID = "order_id";
    /**
     * 客户id列名
     */
    private static final String COLUMN_CUSTOMER_ID = "customer_id";

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ComplexKeysShardingValue<String> shardingValue) {
        if (!shardingValue.getColumnNameAndRangeValuesMap().isEmpty()) {
            throw new RuntimeException("不支持除了=和in的操作");
        }

        // 获取订单id
        Collection<String> orderIds = shardingValue.getColumnNameAndShardingValuesMap().getOrDefault(COLUMN_ORDER_ID, new ArrayList<>(1));
        // 获取客户id
        Collection<String> customerIds = shardingValue.getColumnNameAndShardingValuesMap().getOrDefault(COLUMN_CUSTOMER_ID, new ArrayList<>(1));

        // 整合订单id和客户id
        List<String> ids = new ArrayList<>(16);
        ids.addAll(orderIds);
        ids.addAll(customerIds);

        return ids.stream()
                // 截取 订单号或客户id的后2位
                .map(id -> id.substring(id.length() - 2))
                // 去重
                .distinct()
                // 转换成int
                .map(Integer::new)
                // 对可用的表名求余数，获取到真实的表的后缀
                .map(idSuffix -> idSuffix % availableTargetNames.size())
                // 转换成string
                .map(String::valueOf)
                // 获取到真实的表
                .map(tableSuffix -> availableTargetNames.stream().filter(targetName -> targetName.endsWith(tableSuffix)).findFirst().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


}

