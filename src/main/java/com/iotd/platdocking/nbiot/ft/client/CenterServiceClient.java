package com.iotd.platdocking.nbiot.ft.client;

import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;

/**
 * 系统中心服务
 */
@FeignClient("${iotd.smartcover.service.center}")
public interface CenterServiceClient {

    //获取待下发的指令
    @GetMapping("/deviceSendParam/getInstruction")
    String getInstruction(@RequestParam @NotBlank @ApiParam(name = "imei", value = "imei", required = true) String imei);

}
