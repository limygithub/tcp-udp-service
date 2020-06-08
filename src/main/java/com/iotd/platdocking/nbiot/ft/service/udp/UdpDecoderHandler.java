package com.iotd.platdocking.nbiot.ft.service.udp;

import com.iotd.platdocking.nbiot.ft.client.CenterServiceClient;
import com.iotd.platdocking.nbiot.ft.utils.IMEIUtils;
import com.iotd.smartcover.common.constants.NbIotConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
@Service
public class UdpDecoderHandler extends MessageToMessageDecoder<DatagramPacket>  {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;
    @Autowired
    private CenterServiceClient centerServiceClient;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> out) throws Exception {
        InetSocketAddress  remoteAddress = datagramPacket.sender();
        String ip = remoteAddress.getAddress().getHostAddress();
        int port = remoteAddress.getPort();
        //ByteBuf byteBuf = datagramPacket.content();
        ByteBuf buf = datagramPacket.copy().content();
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        String msg = new String(data, "UTF-8");
        log.info("UDP-->收到消息ip={},msg={}",ip,msg);
        //获取IMEI
        String imei = IMEIUtils.getImei(msg);
        if(!StringUtils.isEmpty(msg) && !StringUtils.isEmpty(imei)){
            try {
                kafkaTemplate.send(NbIotConstants.FT_DEVICE_DATA_TOPIC, msg);
            } catch (Exception e) {
                log.error("UDP-->收到直联设备消息,写入Kafka出错",e);
            }
            //查询该设备是否有配置下发
            String instruction = centerServiceClient.getInstruction(imei);
            if (!StringUtils.isEmpty(instruction)){
                out.add(new UDPRespsonse(ip,port,instruction)); //将数据传入下一个UdpHandler
                log.info("UDP-->imei={}设备发下指令={}",imei,instruction);
            }else {
                out.add(new UDPRespsonse(ip,port,null));
                log.info("UDP-->imei={}设备无发下指令",imei);
            }
        }else {
            out.add(new UDPRespsonse(ip,port,null));
        }
    }

}
