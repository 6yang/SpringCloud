package com.yang.controller;

import com.yang.pojo.Star;
import com.yang.service.StarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("star")
public class StarController {
    @Autowired
    private StarService starService;

    @RequestMapping("{id}")
    public Star queryById(@PathVariable("id") Integer id){
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return starService.queryById(id);
    }
}
