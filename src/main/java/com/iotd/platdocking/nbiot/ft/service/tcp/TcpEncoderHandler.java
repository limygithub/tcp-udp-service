package com.iotd.platdocking.nbiot.ft.service.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Limy
 * @create: 2020/06/05 09:09
 * @description: TcpEncoderHandler
 */
@Slf4j
@Service
@ChannelHandler.Sharable
public class TcpEncoderHandler extends MessageToMessageEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object o, List<Object> list) throws Exception {
        byte[] data = o.toString().getBytes("utf-8");
        ByteBuf buf = ctx.alloc().buffer(data.length);
        buf.writeBytes(data);
        list.add(buf);
//        log.info("返回给TCP客户端信息msg={}",o.toString());
    }
}
