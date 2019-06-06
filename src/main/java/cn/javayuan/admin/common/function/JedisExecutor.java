package cn.javayuan.admin.common.function;

import cn.javayuan.admin.common.exception.RedisConnectException;

@FunctionalInterface
public interface JedisExecutor<T, R> {
    R excute(T t) throws RedisConnectException;
}
