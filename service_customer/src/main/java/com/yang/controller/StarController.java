package com.yang.controller;


import com.yang.pojo.Star;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("customer/star")
public class StarController {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;  //服务地址列表

    @GetMapping("{id}")
    public Star queryStar(@PathVariable("id")Integer id){
        //通过服务的id 获取服务实例 的集合
        List<ServiceInstance> instances = discoveryClient.getInstances("service_provider");
        ServiceInstance instance = instances.get(0);
        Star star = restTemplate.getForObject("http://"+instance.getHost()+":"+instance.getPort()+"/star/" + id, Star.class);
        return star;
    }
}
