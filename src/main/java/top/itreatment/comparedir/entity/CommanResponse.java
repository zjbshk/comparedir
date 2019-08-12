package top.itreatment.comparedir.entity;

import lombok.Data;

@Data
public class CommanResponse {

    public static final CommanResponse SUCCESS = new CommanResponse(200, "成功");
    public static final CommanResponse FAIL = new CommanResponse(100, "失败");
    public static final CommanResponse NUKNOW = new CommanResponse(-100, "未知");
    public static final CommanResponse ERROR = new CommanResponse(0, "服务器出错啦");

    private int code;
    private String msg;
    private Object o;

    CommanResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CommanResponse setO(Object o) {
        this.o = o;
        return this;
    }
}
