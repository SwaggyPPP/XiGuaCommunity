package com.swaggy.xiguacommunity.controller;

import com.swaggy.xiguacommunity.dto.PageNationDTO;
import com.swaggy.xiguacommunity.dto.QuestionDTO;
import com.swaggy.xiguacommunity.mapper.QuestionMapper;
import com.swaggy.xiguacommunity.mapper.UserMapper;
import com.swaggy.xiguacommunity.model.Question;
import com.swaggy.xiguacommunity.model.User;
import com.swaggy.xiguacommunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionService questionService;

    @GetMapping("/")
    public String hello(HttpServletRequest request,
                        Model model,
                        @RequestParam(value = "page",defaultValue = "1")Integer page,
                        @RequestParam(value = "size",defaultValue = "5")Integer size){
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length!=0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }

        PageNationDTO pageNation = questionService.getList(page,size);
        model.addAttribute("pageNation",pageNation);
        return "index";
    }

}
