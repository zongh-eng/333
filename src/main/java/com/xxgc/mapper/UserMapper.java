package com.xxgc.mapper;

//UserMapper
import com.xxgc.entity.User;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
@Mapper
public interface UserMapper {
    List<User> findAll();
}
