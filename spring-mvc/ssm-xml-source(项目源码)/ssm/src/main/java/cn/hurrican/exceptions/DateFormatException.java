package cn.hurrican.exceptions;

import cn.hurrican.dtl.ServerTip;

/**
 * Created by NewObject on 2017/9/15.
 */
public class DateFormatException extends Exception {

    public ServerTip tip = new ServerTip();
    public DateFormatException() {
    }

    public DateFormatException(String message) {
        super(message);
    }
}
