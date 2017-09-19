package cn.hurrican.controllers;

import cn.hurrican.dtl.AccountMsgData;
import cn.hurrican.dtl.ServerTip;
import cn.hurrican.services.AccountMsgService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

/**
 * Created by NewObject on 2017/9/18.
 *
 */

@Controller
@RequestMapping(value = "/account")
public class AccountMsgController {

    @Autowired
    @Qualifier(value = "accountMsgService")
    private AccountMsgService service;

    @RequestMapping(value = "/add.do")
    public void handlerAdditionAccount(AccountMsgData msgData,
                                       HttpServletResponse response)
            throws ParseException, IOException {

        service.addAccountMsg(AccountMsgData.convertAccountMsg(msgData));

        ServerTip tip = new ServerTip(true, "json", "添加成功");
        JSONObject jsonObject = JSONObject.fromObject(tip);
        String s = jsonObject.toString();

        PrintWriter writer = response.getWriter();
        writer.write(s);
        writer.close();

    }

    @ExceptionHandler
    public void handleException(HttpServletResponse response, Exception e){
        ServerTip tip = new ServerTip();
        tip.setMsg(e.toString());

        JSONObject jsonObject = JSONObject.fromObject(tip);
        String s = jsonObject.toString();

        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(s);

        } catch (IOException e1) {
            e1.printStackTrace();
        }finally {
            assert writer != null;
            writer.close();
        }
    }
}
