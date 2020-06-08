package com.iotd.platdocking.nbiot.ft.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * @author: Limy
 * @create: 2020/06/05 09:22
 * @description: 解析IMEI方法
 */
@Slf4j
public class IMEIUtils {

    //报文获取IMEI
    public static String getImei (String message) {
        try {
            if (StringUtils.isEmpty(message)) {
                return null;
            }
            if (message.startsWith("\\\\r,") && message.endsWith(",\\\\r")) {
                message = message.replace("\\\\r,", "").replace(",\\\\r", "");
            } else if (message.startsWith("\\r,") && message.endsWith(",\\r")) {
                message = message.replace("\\r,", "").replace(",\\r", "");
            } else if (message.startsWith("\r,") && message.endsWith(",\r")) {
                message = message.replace("\r,", "").replace(",\r", "");
            }
            int indexMessage = message.indexOf("{");
            int indexImei = message.indexOf(",");
            if (indexMessage > 0 && indexImei > 0){
                String[] split = message.substring(0, indexMessage).split(",");
                return split[1];
            }else {
                log.error("直联报文获取IMEI失败,message={}",message);
                return null;
            }
        } catch (Exception e) {
            log.error("直联报文获取IMEI出错",e);
            return null;
        }
    }

}
