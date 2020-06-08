package com.iotd.platdocking.nbiot.ft.service.udp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
@Service
public class UdpEncoderHandler extends MessageToMessageEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object o, List list) throws Exception {

        UDPRespsonse udpRespsonse = (UDPRespsonse)o;
        byte[] data = udpRespsonse.getMsg().getBytes();
        ByteBuf buf = ctx.alloc().buffer(data.length);
        buf.writeBytes(data);
        InetSocketAddress inetSocketAddress = new InetSocketAddress(udpRespsonse.getIp(),udpRespsonse.getPort());//指定客户端的IP及端口
        list.add(new DatagramPacket(buf, inetSocketAddress));
//        log.info("UDP-->返回给客户端信息ip={},port={},msg={}",udpRespsonse.getIp(),udpRespsonse.getPort(),udpRespsonse.getMsg());
    }
}
