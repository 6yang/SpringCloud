package com.yang.service;

import com.yang.mapper.StarMapper;
import com.yang.pojo.Star;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StarService {
    @Autowired
    private StarMapper starMapper;
    public Star queryById(Integer id){
        return this.starMapper.selectByPrimaryKey(id);
    }
}
