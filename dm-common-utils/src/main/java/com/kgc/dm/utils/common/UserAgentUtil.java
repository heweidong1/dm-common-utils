package com.kgc.dm.utils.common;

import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;

import java.io.IOException;

/**
 * 本类实现了UASparser的单例，该实例可通过分析user-agent信息判断当前Http请求的客户端浏览器类型
 *
 * @author
 */
public class UserAgentUtil {

    private static UASparser uasParser = null;

    public static UASparser getUasParser() {
        if (uasParser == null) {
            try {
                uasParser = new UASparser(OnlineUpdater.getVendoredInputStream());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return uasParser;
    }
    /**
     * 是否移动设备
     *
     * @param agent
     * @return
     */
    public static boolean CheckAgent(String agent) {
        boolean flag = false;

        String[] keywords = {"Android", "iPhone", "iPod", "iPad",
                "Windows Phone", "MQQBrowser"};

        // 排除 Windows 桌面系统
        if (!agent.contains("Windows NT")
                || (agent.contains("Windows NT") && agent
                .contains("compatible; MSIE 9.0;"))) {
            // 排除 苹果桌面系统
            if (!agent.contains("Windows NT") && !agent.contains("Macintosh")) {
                for (String item : keywords) {
                    if (agent.contains(item)) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }

}