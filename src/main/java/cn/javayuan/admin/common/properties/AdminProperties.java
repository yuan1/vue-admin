package cn.javayuan.admin.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "admin")
public class AdminProperties {

    private ShiroProperties shiro = new ShiroProperties();

    private boolean openAopLog = true;

}
