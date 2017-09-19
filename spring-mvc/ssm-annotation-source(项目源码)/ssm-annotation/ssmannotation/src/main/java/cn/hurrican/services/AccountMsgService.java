package cn.hurrican.services;

import cn.hurrican.beans.AccountMsg;
import cn.hurrican.dao.AccountMsgDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by NewObject on 2017/9/18.
 */

@Service(value = "accountMsgService")
public class AccountMsgService {

    @Resource(name = "accountMsgDao")
    private AccountMsgDao dao;

    public void setDao(AccountMsgDao dao) {
        this.dao = dao;
    }

    public AccountMsgDao getDao() {
        return dao;
    }


    @Transactional
    public void addAccountMsg(AccountMsg msg){
        dao.insertRecord(msg);
    }
}
