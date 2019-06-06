package cn.javayuan.admin.web.controller;

import cn.javayuan.admin.common.domain.AdminConstant;
import cn.javayuan.admin.common.domain.AdminResponse;
import cn.javayuan.admin.common.exception.AdminException;
import cn.javayuan.admin.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @GetMapping
    @RequiresPermissions("weather:view")
    public AdminResponse queryWeather(@NotBlank(message = "{required}") String areaId) throws AdminException {
        try {
            String data = HttpUtil.sendPost(AdminConstant.MEIZU_WEATHER_URL, "cityIds=" + areaId);
            return new AdminResponse().data(data);
        } catch (Exception e) {
            String message = "天气查询失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }
}
