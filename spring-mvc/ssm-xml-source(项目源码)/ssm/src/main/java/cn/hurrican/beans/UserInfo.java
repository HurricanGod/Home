package cn.hurrican.beans;

import java.io.Serializable;

/**
 * Created by NewObject on 2017/8/10.
 */
public class UserInfo implements Serializable{
    private Long id;
    private String username;
    private String email;
    private String pwd;

    public UserInfo(String username, String email, String pwd) {
        this.username = username;
        this.email = email;
        this.pwd = pwd;
    }

    public UserInfo(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public UserInfo()
    {
        this.id = -1L;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPwd() {
        return pwd;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }



    @Override
    public String toString() {
        return new StringBuffer().append("id:\t").append(this.id)
                .append("\nusername:\t").append(this.username).append("\n").toString();
    }
}
