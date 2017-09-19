package cn.hurrican.dtl;

import cn.hurrican.beans.AccountMsg;
import cn.hurrican.utils.DateTimeFormatUtil;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by NewObject on 2017/9/18.
 */
public class AccountMsgData {
    private Integer id;
    private Integer userid;
    private String account;
    private String pwd;
    private String describe;
    private String bindEmail;
    private String bindPhone;
    private String registerTime;


    public static AccountMsg convertAccountMsg(AccountMsgData data) throws ParseException {
        AccountMsg msg = new AccountMsg();

        msg.setId(data.getId());
        msg.setAccount(data.getAccount());
        msg.setBindEmail(data.getBindEmail());
        msg.setBindPhone(data.getBindPhone());
        msg.setDescribe(data.getDescribe());
        msg.setPwd(data.getPwd());
        msg.setUserid(data.getUserid());

        String registerTime = data.getRegisterTime();
        if (DateTimeFormatUtil.checkDateTimeFormat(registerTime)) {

            String s = DateTimeFormatUtil.formatDateTimeString(registerTime);
            Date date = DateTimeFormatUtil.parseStringToDate(s);
            msg.setRegisterTime(date);
        }

        return msg;
    }


    public AccountMsgData(Integer id, Integer userid, String account, String pwd,
                          String describe, String bindEmail, String bindPhone) {
        this.id = id;
        this.userid = userid;
        this.account = account;
        this.pwd = pwd;
        this.describe = describe;
        this.bindEmail = bindEmail;
        this.bindPhone = bindPhone;
    }

    public AccountMsgData() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserid() {
        return userid;
    }

    public String getAccount() {
        return account;
    }

    public String getPwd() {
        return pwd;
    }

    public String getDescribe() {
        return describe;
    }

    public String getBindEmail() {
        return bindEmail;
    }

    public String getBindPhone() {
        return bindPhone;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setBindEmail(String bindEmail) {
        this.bindEmail = bindEmail;
    }

    public void setBindPhone(String bindPhone) {
        this.bindPhone = bindPhone;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }
}
