package cn.javayuan.admin.web.controller;

import cn.javayuan.admin.common.domain.AdminConstant;
import cn.javayuan.admin.common.domain.AdminResponse;
import cn.javayuan.admin.common.exception.AdminException;
import cn.javayuan.admin.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/movie")
public class MovieController {

    private String message;

    @GetMapping("hot")
    public AdminResponse getMoiveHot() throws AdminException {
        try {
            String data = HttpUtil.sendSSLPost(AdminConstant.TIME_MOVIE_HOT_URL, "locationId=328");
            return new AdminResponse().data(data);
        } catch (Exception e) {
            message = "获取热映影片信息失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }

    @GetMapping("coming")
    public AdminResponse getMovieComing() throws AdminException {
        try {
            String data = HttpUtil.sendSSLPost(AdminConstant.TIME_MOVIE_COMING_URL, "locationId=328");
            return new AdminResponse().data(data);
        } catch (Exception e) {
            message = "获取即将上映影片信息失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }

    @GetMapping("detail")
    public AdminResponse getDetail(@NotBlank(message = "{required}") String id) throws AdminException {
        try {
            String data = HttpUtil.sendSSLPost(AdminConstant.TIME_MOVIE_DETAIL_URL, "locationId=328&movieId=" + id);
            return new AdminResponse().data(data);
        } catch (Exception e) {
            message = "获取影片详情失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }

    @GetMapping("comments")
    public AdminResponse getComments(@NotBlank(message = "{required}") String id) throws AdminException {
        try {
            String data = HttpUtil.sendSSLPost(AdminConstant.TIME_MOVIE_COMMENTS_URL, "movieId=" + id);
            return new AdminResponse().data(data);
        } catch (Exception e) {
            message = "获取影片评论失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }
}
