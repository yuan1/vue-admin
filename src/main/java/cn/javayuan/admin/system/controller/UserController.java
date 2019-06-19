package cn.javayuan.admin.system.controller;

import cn.javayuan.admin.common.annotation.Log;
import cn.javayuan.admin.common.controller.BaseController;
import cn.javayuan.admin.common.domain.QueryRequest;
import cn.javayuan.admin.common.exception.AdminException;
import cn.javayuan.admin.common.utils.MD5Util;
import cn.javayuan.admin.system.controller.vm.UserVM;
import cn.javayuan.admin.system.domain.User;
import cn.javayuan.admin.system.domain.UserConfig;
import cn.javayuan.admin.system.service.UserConfigService;
import cn.javayuan.admin.system.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {

    private String message;

    @Autowired
    private UserService userService;
    @Autowired
    private UserConfigService userConfigService;

    @GetMapping("check/{username}")
    public boolean checkUserName(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userService.findByName(username) == null;
    }

    @GetMapping("/{username}")
    public User detail(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userService.findByName(username);
    }

    @GetMapping
    @RequiresPermissions("user:view")
    public Map<String, Object> userList(QueryRequest queryRequest, User user) {
        return getDataTable(userService.findUserDetail(user, queryRequest));
    }

    @Log("新增用户")
    @PostMapping
    @RequiresPermissions("user:add")
    public void addUser(@Valid @RequestBody User user) throws AdminException {
        try {
            this.userService.createUser(user);
        } catch (Exception e) {
            message = "新增用户失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }

    @Log("修改用户")
    @PutMapping
    @RequiresPermissions("user:update")
    public void updateUser(@Valid @RequestBody User user) throws AdminException {
        try {
            this.userService.updateUser(user);
        } catch (Exception e) {
            message = "修改用户失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }

    @Log("删除用户")
    @DeleteMapping("/{userIds}")
    @RequiresPermissions("user:delete")
    public void deleteUsers(@NotBlank(message = "{required}") @PathVariable String userIds) throws AdminException {
        try {
            String[] ids = userIds.split(StringPool.COMMA);
            this.userService.deleteUsers(ids);
        } catch (Exception e) {
            message = "删除用户失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }

    @PutMapping("profile")
    public void updateProfile(@Valid @RequestBody User user) throws AdminException {
        try {
            this.userService.updateProfile(user);
        } catch (Exception e) {
            message = "修改个人信息失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }

    @PutMapping("avatar")
    public void updateAvatar(@Valid @RequestBody UserVM.UserUAVM uavm) throws AdminException {
        try {
            this.userService.updateAvatar(uavm.getUsername(), uavm.getAvatar());
        } catch (Exception e) {
            message = "修改头像失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }

    @PutMapping("userconfig")
    public void updateUserConfig(@Valid @RequestBody UserConfig userConfig) throws AdminException {
        try {
            this.userConfigService.update(userConfig);
        } catch (Exception e) {
            message = "修改个性化配置失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }

    @GetMapping("password/check")
    public boolean checkPassword(@Valid @RequestBody UserVM.UserUPVM upvm) {
        String encryptPassword = MD5Util.encrypt(upvm.getUsername(), upvm.getPassword());
        User user = userService.findByName(upvm.getUsername());
        if (user != null)
            return StringUtils.equals(user.getPassword(), encryptPassword);
        else
            return false;
    }

    @PutMapping("password")
    public void updatePassword(@Valid @RequestBody UserVM.UserUPVM upvm) throws AdminException {
        try {
            userService.updatePassword(upvm.getUsername(), upvm.getPassword());
        } catch (Exception e) {
            message = "修改密码失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }

    @GetMapping("password/reset")
    @RequiresPermissions("user:reset")
    public void resetPassword(@NotBlank(message = "{required}") String ids) throws AdminException {
        try {
            String[] idsArr = ids.split(StringPool.COMMA);
            this.userService.resetPassword(idsArr);
        } catch (Exception e) {
            message = "重置用户密码失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }

    @GetMapping("excel")
    @RequiresPermissions("user:export")
    public void export(QueryRequest queryRequest,User user, HttpServletResponse response) throws AdminException {
        try {
            List<User> users = this.userService.findUserDetail(user, queryRequest).getRecords();
            ExcelKit.$Export(User.class, response).downXlsx(users, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }
}
