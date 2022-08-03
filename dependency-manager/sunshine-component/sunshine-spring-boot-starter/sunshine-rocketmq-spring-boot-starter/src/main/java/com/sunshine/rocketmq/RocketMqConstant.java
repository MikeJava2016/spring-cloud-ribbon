package com.sunshine.rocketmq;

public class RocketMqConstant {
//    public static final String namesrvAddr = "192.168.56.100:9876";
    public static final String namesrvAddr = "127.0.0.1:9876";

    public static class Topic{

        public static final String LoadBalanceConsumer = "wyc_from_order_to_finicial";

    }

    public static class Group{

        public static final String GROUP_ORDER = "group_order";
    }

    public static class Tag{
        public static final String TAG_ORDER = "tag_order_1";
    }
}
