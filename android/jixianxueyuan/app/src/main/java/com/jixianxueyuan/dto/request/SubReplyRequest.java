package com.jixianxueyuan.dto.request;

import com.jixianxueyuan.dto.ReplyDTO;
import com.jixianxueyuan.dto.SubReplyDTO;
import com.jixianxueyuan.dto.UserMinDTO;

import java.io.Serializable;

/**
 * Created by pengchao on 8/26/15.
 */
public class SubReplyRequest implements Serializable {
    private String content;
    private UserMinDTO user;
    private ReplyDTO reply;
    private SubReplyDTO preSubReply;

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

    public ReplyDTO getReply() {
        return reply;
    }

    public void setReply(ReplyDTO reply) {
        this.reply = reply;
    }

    public SubReplyDTO getPreSubReply() {
        return preSubReply;
    }

    public void setPreSubReply(SubReplyDTO preSubReply) {
        this.preSubReply = preSubReply;
    }
}
