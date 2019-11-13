package com.yang.client;

import com.yang.configuration.FeignLogConfiguration;
import com.yang.pojo.Star;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "service-provider",fallback = StarClientFallback.class,configuration = FeignLogConfiguration.class)
public interface StarClient {

    @RequestMapping("star/{id}")
    Star queryById(@PathVariable("id") Integer id);
}
