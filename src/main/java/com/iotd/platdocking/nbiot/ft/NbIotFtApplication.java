package com.iotd.platdocking.nbiot.ft;

import com.iotd.platdocking.nbiot.ft.service.udp.UdpDecoderHandler;
import com.iotd.platdocking.nbiot.ft.service.udp.UdpEncoderHandler;
import com.iotd.platdocking.nbiot.ft.service.udp.UdpHandler;
import com.iotd.platdocking.nbiot.ft.service.tcp.TcpDecoderHandler;
import com.iotd.platdocking.nbiot.ft.service.tcp.TcpEncoderHandler;
import com.iotd.platdocking.nbiot.ft.service.tcp.TcpHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.netty.tcp.TcpServer;
import reactor.netty.udp.UdpServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class NbIotFtApplication {

    @Value("${udp.service.ip}")
    private String ip;
    @Value("${udp.service.port}")
    private int udpPort;
    @Value("${tcp.service.port}")
    private int tcpPort;


    public static void main(String[] args) {
        SpringApplication.run(NbIotFtApplication.class, args);
        System.out.println("----------------启动成功----------------");
    }

    @Bean
    CommandLineRunner serverRunner(UdpDecoderHandler udpDecoderHanlder, UdpEncoderHandler udpEncoderHandler, UdpHandler udpHandler,
                                   TcpDecoderHandler tcpDecoderHandler, TcpEncoderHandler tcpEncoderHandler, TcpHandler tcpHandler) {
        return strings -> {
            createUdpServer(udpDecoderHanlder, udpEncoderHandler, udpHandler);
            createTcpServer(tcpDecoderHandler,tcpEncoderHandler,tcpHandler);
        };
    }

    /**
     * 	 创建UDP Server
     * @param udpDecoderHandler： 用于解析UDP Client上报数据的handler
     * @param udpEncoderHandler： 用于向UDP Client发送数据进行编码的handler
     * @param udpHandler: 用户维护UDP链接的handler
     */
    private void createUdpServer(UdpDecoderHandler udpDecoderHandler, UdpEncoderHandler udpEncoderHandler, UdpHandler udpHandler) {
        UdpServer.create()
                .handle((in, out) -> {
                    in.receive()
                            .asByteArray()
                            .subscribe();
                    return Flux.never();
                })
                .host(ip).port(udpPort)
                .doOnBound(conn -> conn
                        .addHandler("decoder", udpDecoderHandler)
                        .addHandler("encoder", udpEncoderHandler)
                        .addHandler("handler", udpHandler)
                ) //可以添加多个handler
                .bindNow(Duration.ofSeconds(30));
    }

    /**
     * 	创建TCP Server
     * @param tcpDecoderHandler： 用于解析TCP Client上报数据的handler
     * @param tcpEncoderHandler： 用于向TCP Client发送数据进行编码的handler
     * @param tcpHandler: 用户维护TCP连接的handler
     */
    private void createTcpServer(TcpDecoderHandler tcpDecoderHandler, TcpEncoderHandler tcpEncoderHandler, TcpHandler tcpHandler) {
        TcpServer.create()
                .handle((in, out) -> {
                    in.receive()
                            .asByteArray()
                            .subscribe();
                    return Flux.never();
                })
//                .host(ip)
                .port(tcpPort)
                .doOnConnection(conn -> conn
                        .addHandler("decoder",tcpDecoderHandler)
                        .addHandler("encoder", tcpEncoderHandler)
                        .addHandler("handler",tcpHandler))
				.bindNow();
    }

    public static String getIp() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

}
