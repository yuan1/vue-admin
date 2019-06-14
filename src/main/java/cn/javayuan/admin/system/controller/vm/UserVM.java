package cn.javayuan.admin.system.controller.vm;

import lombok.Data;

import javax.validation.constraints.NotBlank;

public class UserVM {
    @Data
    public static class UserUAVM{
        @NotBlank(message = "{required}")
        public String username;
        @NotBlank(message = "{required}")
        public String avatar;
    }
    @Data
    public static class UserUPVM{
        @NotBlank(message = "{required}")
        public String username;
        @NotBlank(message = "{required}")
        public String password;
    }
}
