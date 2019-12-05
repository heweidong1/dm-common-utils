package com.kgc.dm.utils.common;

import com.alibaba.fastjson.JSON;
import com.kgc.dm.pojo.DmUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TokenUtils {


    @Autowired
    RedisUtils redisApi;

    /**
     * 生成token
     */
    public  String generatorToken(String userAgent, DmUser user)
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("token:");
        //判断终端
        if(UserAgentUtil.CheckAgent(userAgent))
        {
            stringBuffer.append("MOBILE-");
        }else
        {
            stringBuffer.append("PC-");
        }
        stringBuffer.append(StringUtils.substring(MD5Util.MD5EncodeUtf8(user.getPhone()),0,32));
        stringBuffer.append("-");
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss");
        stringBuffer.append(time.format(new Date()));
        stringBuffer.append("-");
        stringBuffer.append(StringUtils.substring(MD5Util.MD5EncodeUtf8(userAgent),0,6));
        return stringBuffer.toString();
    }

    /**
     * 置换
     * @param userAgent
     * @param token
     * @return
     * @throws Exception
     */
    public  String replaceToken(String userAgent, String token) throws Exception {
        //获取token的剩余时间
        long ttl = redisApi.ttl(token);
        if(ttl<=-1)
        {
            return null;
        }
        //判断token是否在保护期
        if(ttl>60*60)
        {
            return null;
        }
        //将旧token重新设置过期时间
        redisApi.set(token,redisApi.get(token).toString(),(2*60));
        //生成新token
        String newToken = generatorToken(userAgent, JSON.parseObject(redisApi.get(token).toString(),
                DmUser.class));
        redisApi.set(newToken,redisApi.get(token).toString(),2*60*60);
        return  newToken;
    }
    /**
     * 根据token拿到user用户
     */
    public DmUser getUser(String token)
    {
        DmUser dmUser = JSON.parseObject(redisApi.get(token).toString(), DmUser.class);
        return dmUser;
    }
}
