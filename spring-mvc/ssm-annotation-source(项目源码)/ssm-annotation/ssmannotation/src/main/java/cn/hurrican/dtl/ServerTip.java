package cn.hurrican.dtl;

/**
 * Created by NewObject on 2017/9/15.
 */
public class ServerTip {
    private boolean status;
    private String type;
    private String msg;

    public ServerTip(boolean status, String type, String msg) {
        this.status = status;
        this.type = type;
        this.msg = msg;
    }

    public ServerTip() {
        status = false;
        msg = "服务器发生异常";

    }

    public boolean isStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
