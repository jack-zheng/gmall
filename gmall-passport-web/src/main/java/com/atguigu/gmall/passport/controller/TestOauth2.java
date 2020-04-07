package com.atguigu.gmall.passport.controller;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.util.HttpclientUtil;

import java.util.HashMap;
import java.util.Map;

public class TestOauth2 {
    public static void main(String[] args) {
        // app key: 3149469948
        // App Secret：2962eb93da235494753fc5a2b20e7731

        // 授权地址 https://api.weibo.com/oauth2/authorize?client_id=YOUR_CLIENT_ID&response_type=code&redirect_uri=YOUR_REGISTERED_REDIRECT_URI
        // 授权地址 https://api.weibo.com/oauth2/authorize?client_id=3149469948&response_type=code&redirect_uri=http://127.0.0.1:8085/vlogin

        // 授权结束，返回 code: http://127.0.0.1:8085/vlogin?code=8548160adf3702fe8a0c09b9a470b66d

        // 使用 code 发送 post 请求拿到 access_token
        // sample: https://api.weibo.com/oauth2/access_token?client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET&grant_type=authorization_code&redirect_uri=YOUR_REGISTERED_REDIRECT_URI&code=CODE

//        String s3 = "https://api.weibo.com/oauth2/access_token?";//?client_id=187638711&client_secret=a79777bba04ac70d973ee002d27ed58c&grant_type=authorization_code&redirect_uri=http://passport.gmall.com:8085/vlogin&code=CODE";
//        Map<String,String> paramMap = new HashMap<>();
//        paramMap.put("client_id","3149469948");
//        paramMap.put("client_secret","2962eb93da235494753fc5a2b20e7731");
//        paramMap.put("grant_type","authorization_code");
//        paramMap.put("redirect_uri","http://127.0.0.1:8085/vlogin");
//        paramMap.put("code","8548160adf3702fe8a0c09b9a470b66d");// 授权有效期内可以使用，没新生成一次授权码，说明用户对第三方数据进行重启授权，之前的access_token和授权码全部过期
//        String access_token_json = HttpclientUtil.doPost(s3, paramMap);
//
//        Map<String,String> access_map = JSON.parseObject(access_token_json,Map.class);
//
//        System.out.println(access_map.get("access_token"));
//        System.out.println(access_map.get("uid"));

        //2.00dVvgCH5ArI8D96ba1622b20fWumL
        //6452713341

        String s4 = "https://api.weibo.com/2/users/show.json?access_token=2.00dVvgCH5ArI8D96ba1622b20fWumL&uid=6452713341";
        String user_json = HttpclientUtil.doGet(s4);
        Map<String, String> user_map = JSON.parseObject(user_json, Map.class);
        System.out.println(user_map);
    }
}
