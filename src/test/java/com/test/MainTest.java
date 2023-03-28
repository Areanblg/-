package com.test;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;

import java.rmi.ServerException;

public class MainTest {
    public static void main(String[] args) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-beijing", "LTAI5tR2Gatda26vJpbHVJMR", "ldch4iLSfGOeEtBB6wZZ3o8JrZDYR8");
        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers("18739465575");//接收短信的手机号码
        request.setSignName("aearn");//短信签名名称
        request.setTemplateCode("SMS_273850577");//短信模板CODE
        request.setTemplateParam("4442");//短信模板变量对应的实际值

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }

    }

}
