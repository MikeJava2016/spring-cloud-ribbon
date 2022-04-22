package com.sunshine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    /**
     * url http://localhost:8080/user/1/1
     * restTemplate 做rpc调用
     * @param id
     * @return
     */
    @GetMapping("/1/{id}")
    public String findUserById(@PathVariable("id") int id){
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:8080/hello/hello", String.class);
        System.out.println(forEntity.getBody());
        return "user:"+id;
    }


    @Autowired
    private LoadBalancerClient loadBalancerClient;

    /**
     * http://localhost:8080/user/2/1
     * @param id
     * @return
     */
    @GetMapping("/2/{id}")
    public String findUserById2(@PathVariable("id") int id){
        ServiceInstance serverInstance = loadBalancerClient.choose("spring-cloud-order-service");
        String url = String.format("http://%s:%s/user/1/%s", serverInstance.getHost(), serverInstance.getPort(),id);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        System.out.println(forEntity.getBody());
        return "user:"+id;
    }


    @Autowired
    @Qualifier("restTemplate3")
    @LoadBalanced
    private RestTemplate restTemplate3;

    /**
     * http://localhost:8080/user/3/1
     * @param id
     * @return
     */
    @GetMapping("/3/{id}")
    public String findUserById3(@PathVariable("id") int id){
        ResponseEntity<String> forEntity = restTemplate3.getForEntity("http://spring-cloud-order-service/hello/hello", String.class);
        System.out.println(forEntity.getBody());
        return "user:"+id;
    }
}
