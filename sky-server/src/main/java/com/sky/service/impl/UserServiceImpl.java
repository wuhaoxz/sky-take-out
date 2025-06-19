package com.sky.service.impl;

import com.alibaba.druid.util.HttpClientUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.Dish;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatProperties weChatProperties;

    @Override
    public User login(UserLoginDTO userLoginDTO) {

        //1.通过httpClient，构造登凭证校验请求
        Map<String, String> map = new HashMap<>();
        // appid	string	是	小程序 appId
        // secret	string	是	小程序 appSecret
        // js_code	string	是	登录时获取的 code，可通过wx.login获取
        // grant_type	string	是	授权类型，此处只需填写 authorization_code
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",userLoginDTO.getCode());
        map.put("grant_type","authorization_code");
        String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/jscode2session", map);
        // log.info(s);
        //2.解析响应结果，获取openid
        JSONObject jsonObject = JSON.parseObject(s);
        String openid = jsonObject.getString("openid");

        if(openid == null){
            throw new LoginFailedException("登录失败");
        }

        //3.通过openid查询user表，判断是否是新用户
        User user  = userMapper.getByOpenid(openid);
        if(user == null){
            //如果是，则插入user表
            user = new User();
            user.setOpenid(openid);
            user.setCreateTime(LocalDateTime.now());
            user.setName(openid.substring(0,5));
            userMapper.save(user);//mapper中设置主键返回
        }

        //返回user
        return user;
    }
}
