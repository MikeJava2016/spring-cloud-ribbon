package com.sunshine.configuration.ApplicationEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.cloud.client.discovery.event.InstancePreRegisteredEvent;
import org.springframework.cloud.client.discovery.event.InstanceRegisteredEvent;
import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;


@Configuration
public class EventConfig {

    private final static Logger logger = LoggerFactory.getLogger(EventConfig.class);

    @Bean(name = "getMyListener")
    @Order(LoggingApplicationListener.DEFAULT_ORDER + 1)
    public MyListener getMyListener(){
        return new MyListener(MyEventEnum.one,message -> logger.info("MyListener1 {} ",message));
    }

    @Bean(name = "getMyListener2")
    @Order(LoggingApplicationListener.DEFAULT_ORDER + 2)
    public MyListener getMyListener2(){
        return new MyListener(MyEventEnum.two, message -> logger.info("MyListener2 {} ",message));
    }

    @Bean(name = "instancePreRegisteredListener3")
    @Order(LoggingApplicationListener.DEFAULT_ORDER + 3)
    public ApplicationListener instancePreRegisteredListener(){
        InstancePreRegisteredListener instancePreRegisteredListener = new InstancePreRegisteredListener();
        logger.info(" instance InstancePreRegisteredListener finish");
        return instancePreRegisteredListener;
    }

    @Bean(name = "instanceRegisteredListener4")
    @Order(LoggingApplicationListener.DEFAULT_ORDER + 4)
    public ApplicationListener instanceRegisteredListener(){
        InstanceRegisteredListener instanceRegisteredListener = new InstanceRegisteredListener();
        logger.info(" instance InstanceRegisteredListener finish");
        return instanceRegisteredListener;
    }

    public static class InstancePreRegisteredListener implements ApplicationListener<InstancePreRegisteredEvent>{

        @Override
        public void onApplicationEvent(InstancePreRegisteredEvent event) {
            AbstractAutoServiceRegistration ServiceRegistration  = (AbstractAutoServiceRegistration) event.getSource();
            logger.info(" 服务注册 ServiceRegistration 开始。。。");
        }
    }

    public static class InstanceRegisteredListener implements ApplicationListener<InstanceRegisteredEvent>{

        @Override
        public void onApplicationEvent(InstanceRegisteredEvent event) {
            AbstractAutoServiceRegistration serviceRegistration  = (AbstractAutoServiceRegistration) event.getSource();
            logger.info(" 服务注册 ServiceRegistration 完成");

        }
    }


    public static class MyListener implements ApplicationListener<MyEvent> {

        private final MyEventEnum eventEnum;

        private final Consumer consumer;

        public MyListener(MyEventEnum eventEnum, Consumer consumer) {
            this.eventEnum = eventEnum;
            this.consumer = consumer;
        }

        private boolean support(MyEvent event){
            return this.eventEnum.equals(event.getEventEnum());
        }


        @Override
        public void onApplicationEvent(MyEvent event) {
          if (support(event)){
              consumer.accept(event.getMessage());
          }

        }


    }
    public static class MyEvent<T> extends ApplicationEvent {

        private T message;
        private final MyEventEnum eventEnum;

        public MyEvent(Object source, T message,MyEventEnum eventEnum) {
            super(source);
            this.message = message;
            this.eventEnum = eventEnum;
        }

        public Object getMessage() {
            return message;
        }

        public MyEventEnum getEventEnum() {
            return eventEnum;
        }
    }

    @Component
    public static class EventPublisher implements ApplicationContextAware{

        private ApplicationContext applicationContext;

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }

        public void publishEvent(ApplicationEvent event) {
             this.applicationContext.publishEvent(event);
        }
    }

    public enum MyEventEnum {


        one(1,"熟人添加"),
        two(2,"二维码转入");

        private Integer key;
        private String value;

        public static String getValue(Integer value) {
            MyEventEnum[] eventEnums = values();
            for (MyEventEnum eventEnum : eventEnums) {
                if (eventEnum.key.equals(value)) {
                    return eventEnum.value;
                }
            }
            return null;
        }

        private MyEventEnum(Integer key, String value) {
            this.key = key;
            this.value = value;
        }



        public static Map<String, String> getClueSourceMap() {
            return (Map) Arrays.stream(values()).collect(Collectors.toMap(MyEventEnum::getKey, MyEventEnum::getValue));
        }

        public Integer getKey() {
            return this.key;
        }

        public void setKey(Integer key) {
            this.key = key;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

  /*  public static void main(String[] args) {
        EventConfig.EventPublisher eventPublisher = applicationContext.getBean(EventConfig.EventPublisher.class);
        eventPublisher.publishEvent(new EventConfig.MyEvent(applicationContext,"我是事件!",EventConfig.MyEventEnum.one));
    }*/

}
