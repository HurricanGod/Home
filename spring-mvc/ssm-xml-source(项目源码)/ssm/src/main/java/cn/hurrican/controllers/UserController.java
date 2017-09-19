package cn.hurrican.controllers;

import cn.hurrican.beans.UserInfo;
import cn.hurrican.dtl.RegisterResponsePacket;
import cn.hurrican.dtl.ServerTip;
import cn.hurrican.services.UserService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by NewObject on 2017/9/18.
 */

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    @Qualifier(value = "userService")
    private UserService service;

    @RequestMapping(value = "/register.do")
    public void doRegister(UserInfo usr, HttpServletResponse response) throws IOException {

        Writer writer = response.getWriter();

        int number = service.queryUserNameIsOnlyOne(usr.getUsername());
        RegisterResponsePacket packet = new RegisterResponsePacket();
        if (number > 0) {

            packet.msg = usr.getUsername() + " 已经存在！";
            JSONObject jsonObject = JSONObject.fromObject(packet);
            String s = jsonObject.toString();
            writer.write(s);
            writer.close();
            return;
        }

        service.addUserToDb(usr);

        packet.msg = "注册成功";
        JSONObject jsonObject = JSONObject.fromObject(packet);
        String s = jsonObject.toString();
        writer.write(s);
        writer.close();
    }


    @RequestMapping(value = "/login.do")
    public void doLogin(UserInfo user, HttpServletRequest request,
                        HttpServletResponse response) throws IOException {

        Writer writer = response.getWriter();
        ServerTip tip = new ServerTip();
        Long id= service.queryUsernameAndPwdIsValidity(user.getUsername(),user.getPwd());
        if (id == null) {
            tip.setMsg("帐号或密码不正确!");
            tip.setType("json");

            JSONObject jsonObject = JSONObject.fromObject(tip);
            String s = jsonObject.toString();
            writer.write(s);
            return;
        }

        Long currentTimeMillis = System.currentTimeMillis();
        String key = id + "_" + currentTimeMillis;

        HttpSession session = request.getSession();
        session.setAttribute("id", id);

        request.getServletContext().setAttribute(key, id);
        Cookie cookie = new Cookie("token", key);
        cookie.setMaxAge(60*60*24*3);
        response.addCookie(cookie);

        tip.setStatus(true);
        tip.setMsg("登录成功！");
        JSONObject jsonObject = JSONObject.fromObject(tip);
        String s = jsonObject.toString();
        writer.write(s);
        writer.close();

    }
}
