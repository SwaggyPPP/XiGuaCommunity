package com.swaggy.xiguacommunity.mapper;

import com.swaggy.xiguacommunity.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,tag) values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    @Select("select * from question limit #{offSet},#{size}")
    List<Question> getList(@Param(value = "offSet") Integer offSet, @Param(value = "size") Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator = #{userId}limit #{offSet},#{size}")
    List<Question> getListByUserId(@Param("userId") Integer userId, @Param(value = "offSet") Integer offSet, @Param(value = "size") Integer size);
}
