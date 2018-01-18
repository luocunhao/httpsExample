package com.pulan.wechat.controller;

import com.pulan.wechat.entity.BaseMessage;
import com.pulan.wechat.entity.TextMessage;
import com.pulan.wechat.utils.HttpsClient;
import com.pulan.wechat.utils.MessageUtil;
import com.pulan.wechat.utils.ResponseMessageUtil;
import com.pulan.wechat.utils.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/wechat")
public class MessageController {
    private final static Logger logger = LoggerFactory.getLogger(MessageController.class);
    @RequestMapping(value = "/robot",method = RequestMethod.GET)
    public String validate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("------------------微信url校验---------------");
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            return echostr;
        }else{
            return null;
        }
    }
    @RequestMapping(value = "/robot",method = RequestMethod.POST)
    public String dialog(HttpServletRequest request){
        logger.info("---------------微信回复------------");
        String resp = "";
        try {
            //获取用户消息对象
            BaseMessage message = MessageUtil.request2Message(request);
            logger.info("用户发送的消息:"+((TextMessage)message).toString());
           //根据用户消息回复
            switch (message.getMsgType()){
                case MessageUtil.REQ_MESSAGE_TYPE_TEXT:
                    resp = ResponseMessageUtil.respTextMessage((TextMessage)message);
                    break;
                default:
                    resp = ResponseMessageUtil.respErrorMessage((TextMessage)message);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("语义平台回复的消息:"+resp);
        return resp;
    }
    @RequestMapping("/test")
    public String test(){
        HttpsClient httpsClient = new HttpsClient();
        return "testaaaa";
    }
}
