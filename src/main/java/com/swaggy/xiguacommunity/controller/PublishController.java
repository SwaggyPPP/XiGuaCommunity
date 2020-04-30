package com.swaggy.xiguacommunity.controller;

import com.swaggy.xiguacommunity.mapper.QuestionMapper;
import com.swaggy.xiguacommunity.mapper.UserMapper;
import com.swaggy.xiguacommunity.model.Question;
import com.swaggy.xiguacommunity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    UserMapper userMapper;

    /**
     * 来到发布页面
     * @return
     */
    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    /**
     * 处理发布新帖子的请求
     * @return
     */
    @PostMapping("/publish")
    public String doPublish(@RequestParam("title") String title,
                            @RequestParam("description") String description,
                            @RequestParam("tag") String tag,
                            HttpServletRequest request,
                            Model model){

        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        if (title == null || "".equals(title)){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if (description == null || "".equals(description)){
            model.addAttribute("error","内容不能为空");
            return "publish";
        }
        if (tag == null || "".equals(tag)){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }

        User user =null;
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
        if (user==null) {
            model.addAttribute("error","用户还未登陆，请先登录");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.create(question);

        return "redirect:/";
    }

}
