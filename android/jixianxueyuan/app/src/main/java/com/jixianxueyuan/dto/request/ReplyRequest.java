package com.jixianxueyuan.dto.request;

import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.dto.UserMinDTO;

import java.io.Serializable;

/**
 * Created by pengchao on 5/30/15.
 */
public class ReplyRequest implements Serializable {

    private String content;
    private UserMinDTO user;
    private TopicDTO topic;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserMinDTO getUser() {
        return user;
    }

    public void setUser(UserMinDTO user) {
        this.user = user;
    }

    public TopicDTO getTopic() {
        return topic;
    }

    public void setTopic(TopicDTO topic) {
        this.topic = topic;
    }
}
