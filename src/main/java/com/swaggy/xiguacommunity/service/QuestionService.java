package com.swaggy.xiguacommunity.service;

import com.swaggy.xiguacommunity.dto.PageNationDTO;
import com.swaggy.xiguacommunity.dto.QuestionDTO;
import com.swaggy.xiguacommunity.mapper.QuestionMapper;
import com.swaggy.xiguacommunity.mapper.UserMapper;
import com.swaggy.xiguacommunity.model.Question;
import com.swaggy.xiguacommunity.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionMapper questionMapper;

    public PageNationDTO getList(Integer page, Integer size) {

        Integer offSet = size * (page - 1);

        List<Question> questions = questionMapper.getList(offSet,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        PageNationDTO pageNationDTO = new PageNationDTO();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageNationDTO.setQuestions(questionDTOList);
        Integer totalCount = questionMapper.count();
        pageNationDTO.setPageNation(totalCount,page,size);
        return pageNationDTO;
    }
}
