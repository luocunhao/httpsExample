package com.pulan.wechat.entity;

import lombok.*;


public class TextMessage  extends BaseMessage{
    private String Content;

    public TextMessage() {
        super();
    }

    public TextMessage(String toUserName, String fromUserName, long createTime, String msgType, int funcFlag, String content) {
        super(toUserName, fromUserName, createTime, msgType, funcFlag);
        this.Content = content;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        this.Content = content;
    }

    @Override
    public String toString() {
        return "BaseMessage{" +
                "ToUserName='" + getToUserName() + '\'' +
                ", FromUserName='" + getFromUserName() + '\'' +
                ", CreateTime=" + getCreateTime() +
                ", MsgType='" + getMsgType() + '\'' +
                ", FuncFlag=" + getFuncFlag() +
                ", Content=" + Content +
                '}';
    }
}
