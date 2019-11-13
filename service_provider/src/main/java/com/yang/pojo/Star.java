package com.yang.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "star")
public class Star implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String group_id;

    private String star_info;

    private String face_taken;

    private String img_url;

    private String star_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getStar_info() {
        return star_info;
    }

    public void setStar_info(String star_info) {
        this.star_info = star_info;
    }

    public String getFace_taken() {
        return face_taken;
    }

    public void setFace_taken(String face_taken) {
        this.face_taken = face_taken;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getStar_id() {
        return star_id;
    }

    public void setStar_id(String star_id) {
        this.star_id = star_id;
    }
}
