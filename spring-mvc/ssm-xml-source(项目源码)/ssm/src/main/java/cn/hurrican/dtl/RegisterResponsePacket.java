package cn.hurrican.dtl;

/**
 * Created by NewObject on 2017/9/18.
 */
public class RegisterResponsePacket {

    public boolean result;
    public String msg;

    public RegisterResponsePacket() {
        this.result = false;
    }

    public RegisterResponsePacket(String msg) {
        this.result = false;
        this.msg = msg;
    }
}
