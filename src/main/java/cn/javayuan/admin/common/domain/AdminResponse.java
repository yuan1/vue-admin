package cn.javayuan.admin.common.domain;

import java.util.HashMap;

public class AdminResponse extends HashMap<String, Object> {

    private static final long serialVersionUID = -8713837118340960775L;

    public AdminResponse message(String message) {
        this.put("message", message);
        return this;
    }

    public AdminResponse data(Object data) {
        this.put("data", data);
        return this;
    }

    @Override
    public AdminResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
