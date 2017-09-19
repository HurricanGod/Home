package cn.hurrican.beans;

import java.util.Date;

/**
 * Created by NewObject on 2017/9/15.
 */
public class AccountMsg {

    private Integer id;
    private Integer userid;
    private String account;
    private String pwd;
    private String describe;
    private String bindEmail;
    private String bindPhone;
    private Date registerTime;

    public AccountMsg(String account, String pwd, String decsribe,
                      String bindEmail, String bindPhone, Date registerTime) {
        this.account = account;
        this.pwd = pwd;
        this.describe = decsribe;
        this.bindEmail = bindEmail;
        this.bindPhone = bindPhone;
        this.registerTime = registerTime;
    }

    public AccountMsg() {
    }

    public Integer getId() {
        return id;
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

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}
