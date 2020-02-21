package com.mmall.controller.backend;
//后台
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by chen
 */
@Controller
@RequestMapping("/manage/user")
public class UserManageController {
    //注入
    @Autowired
    private IUserService iUserService;

    //登陆
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String passwprd, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, passwprd);
        if (response.isSuccess()){
            //获取user
            User user = response.getData();
            //判断是否是管理员
            if (user.getRole() == Const.Role.ROLE_ADMIN){
                //登陆的是管理员
                session.setAttribute(Const.CURRENT_USER,user);
                return response;
            }else {
                return ServerResponse.createByErrorMessage("不是管理员，无法登陆 ");
            }
        }
        return response;
    }

}
