package com.iotd.platdocking.nbiot.ft.service.tcp;

import com.iotd.platdocking.nbiot.ft.client.CenterServiceClient;
import com.iotd.platdocking.nbiot.ft.service.udp.UDPRespsonse;
import com.iotd.platdocking.nbiot.ft.utils.IMEIUtils;
import com.iotd.smartcover.common.constants.NbIotConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.util.List;


/**
 * @author: Limy
 * @create: 2020/06/04 17:51
 * @description: Tcp服务
 */
@Service
@Slf4j
@ChannelHandler.Sharable
public class TcpDecoderHandler extends MessageToMessageDecoder<Object> {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;
    @Autowired
    private CenterServiceClient centerServiceClient;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, Object o, List<Object> out) throws Exception{
        ByteBuf byteBuf = (ByteBuf)o;
        byte[] data = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(data);
        String msg = new String(data,"utf-8");
        log.info("TCP-->收到消息msg={}",msg);
        //获取IMEI
        String imei = IMEIUtils.getImei(msg);
        if(!StringUtils.isEmpty(msg) && !StringUtils.isEmpty(imei)){
            try {
                kafkaTemplate.send(NbIotConstants.FT_DEVICE_DATA_TOPIC, msg);
            } catch (Exception e) {
                log.error("TCP-->收到直联设备消息,写入Kafka出错",e);
            }
            //查询该设备是否有配置下发
            String instruction = centerServiceClient.getInstruction(imei);
            if (!StringUtils.isEmpty(instruction)){
                out.add(instruction); //将数据传入下一个TCPHandler
                log.info("TCP-->imei={}设备发下指令={}",imei,instruction);
            }else {
                out.add("");
                log.info("TCP-->imei={}设备无发下指令",imei);
            }
        }else {
            out.add("");
        }
    }

}
