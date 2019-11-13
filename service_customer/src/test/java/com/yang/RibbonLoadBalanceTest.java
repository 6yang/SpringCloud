package com.yang;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceCustomerApplication.class)
public class RibbonLoadBalanceTest {
    @Autowired
    private RibbonLoadBalancerClient client;

    @Test
    public void test() {
        for (int i = 0; i < 50; i++) {
            ServiceInstance instance = client.choose("service-provider");
            System.out.println(instance.getHost()+":"+instance.getPort());
        }
    }
}
