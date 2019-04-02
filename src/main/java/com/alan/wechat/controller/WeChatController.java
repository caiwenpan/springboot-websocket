package com.alan.wechat.controller;

import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author alanpan
 * @title: WeChatController
 * @projectName springboot-websocket
 * @description: TODO
 * @date 2019/4/122:26
 */
@RestController
public class WeChatController {

    @GetMapping("/")
    public ModelAndView login()
    {
        return new ModelAndView("/login");
    }

    @GetMapping(value = "/index")
    public ModelAndView index(String username, String pass, HttpServletRequest request)throws Exception
    {
        if (StringUtils.isEmpty(username)) {
            username = "匿名用户" + System.currentTimeMillis();
        }
        ModelAndView mav = new ModelAndView("/wechat");
        mav.addObject("username", username);
        mav.addObject("webSocketUrl", "ws://"+InetAddress.getLocalHost().getHostAddress()+":"+request.getServerPort()+request.getContextPath()+"/wechat/"+username);
        return mav;
    }
}
