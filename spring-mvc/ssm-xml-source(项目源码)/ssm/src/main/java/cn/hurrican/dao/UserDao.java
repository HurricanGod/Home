package cn.hurrican.dao;

import cn.hurrican.beans.UserInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by NewObject on 2017/9/18.
 */
public interface UserDao {

    void insertUser(UserInfo user);

    void updateUserById(UserInfo user);

    void deleteUserById(UserInfo user);

    List<UserInfo> selectUserByNameFromDb(Map<String, Object> params);

    UserInfo selectUserByIdFromDb(Integer id);

    UserInfo selectUserByUsernameAndPwd(String name, String pwd);
}
