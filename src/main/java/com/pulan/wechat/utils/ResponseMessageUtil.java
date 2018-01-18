package com.pulan.wechat.utils;

import com.alibaba.fastjson.JSONObject;
import com.pulan.wechat.entity.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.tags.form.TextareaTag;

import java.util.Date;
@Component
public class ResponseMessageUtil {
    /*
    * 根据传入的textmessage对象
    * 封装回复的textmessage对象
    * */
    private final static Logger logger = LoggerFactory.getLogger(ResponseMessageUtil.class);
    private static String apikey;
    @Value("${pulan.apikey}")
    public void setApikey(String apikey1){
        apikey = apikey1;
    }
    public static String respTextMessage(TextMessage textMessage){
        //封装回复对象resptextmessage content属性待定
        TextMessage respTextMessange = new TextMessage();
        respTextMessange.setFromUserName(textMessage.getToUserName());
        respTextMessange.setToUserName(textMessage.getFromUserName());
        respTextMessange.setCreateTime(new Date().getTime());
        respTextMessange.setMsgType(MessageUtil.REQ_MESSAGE_TYPE_TEXT);
        respTextMessange.setFuncFlag(0);
        //拼装多轮对话参数
        JSONObject body = new JSONObject();
        //userid设置为用户的openid
        body.put("userid",textMessage.getFromUserName());
        body.put("apikey",apikey);
        body.put("content",textMessage.getContent());
        String respJson = HttpUtil.postRequest("http://210.75.8.38:8021/pulan/api", body.toJSONString());
        logger.info("语义平台回复JSON："+respJson);
        JSONObject ret = JSONObject.parseObject(respJson);
        //如果语义平台没有回复 resptextmessage的content则设置为
        if(respJson==null){
            respTextMessange.setContent("服务连接超时");
            return  MessageUtil.textMessageToXml(respTextMessange);
        }
        String type = ret.getString("type");
        if("pl_semantic".equals(type)){
            JSONObject jo = new JSONObject();
            try {
                JSONObject resp = ret.getJSONObject("resp");
                String person = resp.getString("person");
                String service = resp.getString("service");
                String date = resp.getString("date");
                String intent = resp.getString("intent");
                System.out.println("person:"+person+",service:"+service+",date:"+date+",intent:"+intent);
                //如果进入多轮对话 resptextmessage的content则设置为
                respTextMessange.setContent("查无数据");
                return  MessageUtil.textMessageToXml(respTextMessange);
            } catch (Exception e) {
                //如果进入语义服务出错  resptextmessage的content则设置为
                respTextMessange.setContent("该技能还在开发中");
                return MessageUtil.textMessageToXml(respTextMessange);
            }
        }
        //如果没有进入多轮对话则直接赋值给respContent返回
        respTextMessange.setContent(ret.getString("resp"));
        return  MessageUtil.textMessageToXml(respTextMessange);
    }
    public static String respErrorMessage(TextMessage textMessage){
        TextMessage respTextMessange = new TextMessage();
        respTextMessange.setFromUserName(textMessage.getToUserName());
        respTextMessange.setToUserName(textMessage.getFromUserName());
        respTextMessange.setCreateTime(new Date().getTime());
        respTextMessange.setMsgType(MessageUtil.REQ_MESSAGE_TYPE_TEXT);
        respTextMessange.setFuncFlag(0);
        respTextMessange.setContent("消息类型暂不支持");
        return MessageUtil.textMessageToXml(respTextMessange);
    }
}
