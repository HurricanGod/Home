package cn.hurrican.services;

import cn.hurrican.beans.AccountMsg;
import cn.hurrican.dao.AccountMsgDao;

/**
 * Created by NewObject on 2017/9/18.
 */
public class AccountMsgService {

    private AccountMsgDao dao;

    public void setDao(AccountMsgDao dao) {
        this.dao = dao;
    }

    public AccountMsgDao getDao() {
        return dao;
    }


    public void addAccountMsg(AccountMsg msg){
        dao.insertRecord(msg);
    }
}
