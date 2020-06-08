package com.iotd.platdocking.nbiot.ft.service.udp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: Limy
 * @create: 2020/05/17 18:03
 * @description: UDP返回消息对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UDPRespsonse implements Serializable {

    private String ip;

    private int port;

    private String msg;

}
