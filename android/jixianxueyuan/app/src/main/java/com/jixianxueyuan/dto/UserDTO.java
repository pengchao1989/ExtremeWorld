package com.jixianxueyuan.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserDTO implements Serializable {
    private Long id;
    private String loginName;
    private String name;
    private String registerDate;
    private String gender;
    private String birth;
    private String avatar;
    private String description;
    private String signature;
    private String bg;
    private String hobbyStamp;
    private double distance;
    private String geoModifyTime;

    private List<UserInterestDTO> interests;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    public String getHobbyStamp() {
        return hobbyStamp;
    }

    public void setHobbyStamp(String hobbyStamp) {
        this.hobbyStamp = hobbyStamp;
    }

    public List<UserInterestDTO> getInterests() {
        return interests;
    }

    public void setInterests(List<UserInterestDTO> interests) {
        this.interests = interests;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getGeoModifyTime() {
        return geoModifyTime;
    }

    public void setGeoModifyTime(String geoModifyTime) {
        this.geoModifyTime = geoModifyTime;
    }

/*    @Override
    protected UserDTO clone() {
        try {
            return (UserDTO) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }*/
}
