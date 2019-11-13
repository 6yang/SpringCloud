package com.yang.client;

import com.yang.pojo.Star;
import org.springframework.stereotype.Component;

@Component
public class StarClientFallback implements StarClient {
    @Override
    public Star queryById(Integer id) {
        Star star = new Star();
        star.setFace_taken("服务器正忙 请稍后再试");
        return star;
    }
}
