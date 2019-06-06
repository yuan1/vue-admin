package cn.javayuan.admin.web.controller;

import cn.javayuan.admin.common.domain.AdminConstant;
import cn.javayuan.admin.common.domain.AdminResponse;
import cn.javayuan.admin.common.exception.AdminException;
import cn.javayuan.admin.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @GetMapping
    @RequiresPermissions("article:view")
    public AdminResponse queryArticle(String date) throws AdminException {
        String param;
        String data;
        try {
            if (StringUtils.isNotBlank(date)) {
                param = "dev=1&date=" + date;
                data = HttpUtil.sendSSLPost(AdminConstant.MRYW_DAY_URL, param);
            } else {
                param = "dev=1";
                data = HttpUtil.sendSSLPost(AdminConstant.MRYW_TODAY_URL, param);
            }
            return new AdminResponse().data(data);
        } catch (Exception e) {
            String message = "获取文章失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }
}
