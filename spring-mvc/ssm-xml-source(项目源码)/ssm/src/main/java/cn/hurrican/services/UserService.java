package cn.hurrican.services;

import cn.hurrican.beans.UserInfo;
import cn.hurrican.dao.UserDao;

import java.util.HashMap;
import java.util.List;

/**
 * Created by NewObject on 2017/9/18.
 */
public class UserService {

    private UserDao dao;

    public void setDao(UserDao dao) {
        this.dao = dao;
    }

    public int queryUserNameIsOnlyOne(String name){
        assert name != null;
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", name);
        List<UserInfo> users = dao.selectUserByNameFromDb(map);
        if (users == null) {
            return 0;
        }
        return users.size();
    }

    public void addUserToDb(UserInfo user){
        dao.insertUser(user);
    }

    public UserInfo queryByUserId(Integer id){
        return dao.selectUserByIdFromDb(id);
    }

    public Long queryUsernameAndPwdIsValidity(String name, String pwd){
        UserInfo user = dao.selectUserByUsernameAndPwd(name, pwd);
        if (user == null) {
            return null;
        }
        return user.getId();
    }
}
