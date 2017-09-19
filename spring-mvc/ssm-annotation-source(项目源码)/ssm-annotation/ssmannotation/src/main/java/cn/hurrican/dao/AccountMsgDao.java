package cn.hurrican.dao;

import cn.hurrican.beans.AccountMsg;

import java.util.Map;
import java.util.Set;

/**
 * Created by NewObject on 2017/9/18.
 */
public interface AccountMsgDao {

    void insertRecord(AccountMsg msg);

    Set<AccountMsg> queryByNameFuzzy(Map<String, Object> params);

    Set<AccountMsg> queryByUserId(Map<String, Object> params);

    Set<AccountMsg> queryById(Integer id);

    void updateAccountMsg(AccountMsg msg);
}
