package com.jixianxueyuan.dto;

import java.util.Date;
import java.util.List;


public class TopicDTO
{
    private Long id;
    private String typec;
    private String title;
    private String content;
    private int imageCount;
    private int replyCount;
    private String createTime;
    private int status;
    private String videoSource;

    private UserInfoMinDTO userInfo;

    private List<MediaDTO> medias;


    public Long getId()
    {
        return id;
    }
    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTypec() {
        return typec;
    }

    public void setTypec(String typec) {
        this.typec = typec;
    }

    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public String getContent()
    {
        return content;
    }
    public void setContent(String content)
    {
        this.content = content;
    }
    public int getImageCount()
    {
        return imageCount;
    }
    public void setImageCount(int imageCount)
    {
        this.imageCount = imageCount;
    }
    public int getReplyCount()
    {
        return replyCount;
    }
    public void setReplyCount(int replyCount)
    {
        this.replyCount = replyCount;
    }

    public String getCreateTime()
    {
        return createTime;
    }
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    public int getStatus()
    {
        return status;
    }
    public void setStatus(int status)
    {
        this.status = status;
    }
    public String getVideoSource()
    {
        return videoSource;
    }
    public void setVideoSource(String videoSource)
    {
        this.videoSource = videoSource;
    }
    public UserInfoMinDTO getUserInfo()
    {
        return userInfo;
    }
    public void setUserInfo(UserInfoMinDTO user)
    {
        this.userInfo = user;
    }
    public List<MediaDTO> getMedias() {
        return medias;
    }
    public void setMedias(List<MediaDTO> medias) {
        this.medias = medias;
    }
	
}
