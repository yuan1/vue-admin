package cn.javayuan.admin.common.exception;

/**
 * 系统内部异常
 */
public class AdminException extends Exception {

    private static final long serialVersionUID = -994962710559017255L;

    public AdminException(String message) {
        super(message);
    }
}
