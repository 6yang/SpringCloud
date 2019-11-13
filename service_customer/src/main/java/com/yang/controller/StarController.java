package com.yang.controller;


import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.yang.client.StarClient;
import com.yang.pojo.Star;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("customer/star")
//@DefaultProperties(defaultFallback = "fallBackMethod")
public class StarController {

//    @Autowired
//    RestTemplate restTemplate;
//    @Autowired
//    private DiscoveryClient discoveryClient;  //服务地址列表
    @Autowired
    private StarClient starClient;

    //    @HystrixCommand
    @GetMapping
    public String queryStar(@RequestParam("id") Integer id){
//        if(id==24){
//            throw new RuntimeException();
//        }

        //通过服务的id 获取服务实例 的集合
//        List<ServiceInstance> instances = discoveryClient.getInstances("service_provider");
//        ServiceInstance instance = instances.get(0);
//        return  restTemplate.getForObject("http://service-provider/star/" + id, String.class);
        return this.starClient.queryById(id).toString();
    }
//    // 参数列表必须一致
//    public String queryStarFallBack(Integer id){
//        return "服务器正忙，请稍后再试";
//    }
//    public String fallBackMethod(){
//        return "服务器有点忙，请稍后再试";
//    }

}
