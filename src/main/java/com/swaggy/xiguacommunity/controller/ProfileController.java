package com.swaggy.xiguacommunity.controller;

import com.swaggy.xiguacommunity.dto.PageNationDTO;
import com.swaggy.xiguacommunity.mapper.UserMapper;
import com.swaggy.xiguacommunity.model.User;
import com.swaggy.xiguacommunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String toProfile(@PathVariable("action") String action,
                            Model model,
                            HttpServletRequest request,
                            @RequestParam(value = "page",defaultValue = "1")Integer page,
                            @RequestParam(value = "size",defaultValue = "5")Integer size){

        User user = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length!=0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }
        if (user == null){
            return "redirect:/";
        }

        if ("questions".equals(action)) {
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的问题");
        }else if ("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }

        PageNationDTO pageNationDTO = questionService.getListByUser(user.getId(), page, size);
        model.addAttribute("pageNation",pageNationDTO);
        return "profile";
    }

}
