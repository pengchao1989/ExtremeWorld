package com.jixianxueyuan.dto.request;

import com.jixianxueyuan.dto.HobbyDTO;
import com.jixianxueyuan.dto.UserMinDTO;

/**
 * Created by pengchao on 12/3/15.
 */
public class SponsorshipRequestDTO {
    private Long id;
    private double sum;
    private String message;
    private String ticket;
    private UserMinDTO user;
    private HobbyDTO hobby;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public UserMinDTO getUser() {
        return user;
    }

    public void setUser(UserMinDTO user) {
        this.user = user;
    }

    public HobbyDTO getHobby() {
        return hobby;
    }
    public void setHobby(HobbyDTO hobby) {
        this.hobby = hobby;
    }
}
