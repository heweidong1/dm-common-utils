package com.kgc.dm.utils.common;


import com.cloopen.rest.sdk.CCPRestSmsSDK;
import org.springframework.stereotype.Component;

import java.util.HashMap;


@Component
public class SMSSendUtil {
    public boolean sendSms(String phonenum, String b, String str[]) {
        HashMap<String, Object> result = null;
        boolean ok = false;
//初始化SDK
        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
        restAPI.init("app.cloopen.com", "8883");

        restAPI.setAccount("8a216da86c8a1a54016cc1406097257f", "dae5ca39c4294ce78fe637d181c42e27");


        restAPI.setAppId("8a216da86c8a1a54016cc14060ed2586");

        result = restAPI.sendTemplateSMS(phonenum, b, str);

        System.out.println("SDKTestGetSubAccounts result=" + result);
        String resultstate = (String) result.get("statusCode");

        if ("000000".equals(resultstate)) {

            ok = true;
        } else {

            System.out.println("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
            ok = false;
        }
        return ok;
    }
}
