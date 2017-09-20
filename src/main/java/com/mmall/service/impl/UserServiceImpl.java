package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dizk on 2017/9/13.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUserName(username);
        if(resultCount == 0){
            return ServerResponse.CreateByErrorMessage("用户名不存在");
        }

        //MD5加密
        String md5Password = MD5Util.MD5EncodeUtf8(password);

        User user = userMapper.selectLogin(username,md5Password);
        if(user == null){
            return ServerResponse.CreateByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.CreateBySuccess("登录成功",user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse vaildResponse = this.checkVaild(user.getUsername(),Const.USERNAME);
        if(!vaildResponse.isSuccess()){
            return vaildResponse;
        }

        vaildResponse = this.checkVaild(user.getEmail(),Const.EMAIL);
        if(!vaildResponse.isSuccess()){
            return vaildResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);

        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if(resultCount==0){
            return ServerResponse.CreateByErrorMessage("注册失败");
        }
        return ServerResponse.CreateBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkVaild(String str, String type) {
        if(StringUtils.isNotBlank(type)){
            if(type.equals(Const.USERNAME)){
                int resultCount = userMapper.checkUserName(str);
                if(resultCount > 0){
                    return ServerResponse.CreateByErrorMessage("用户名已存在");
                }
            }
            if(type.equals(Const.EMAIL)){
                int resultCount = userMapper.checkEmail(str);
                if(resultCount > 0){
                    return ServerResponse.CreateByErrorMessage("邮箱已存在");
                }
            }
        }
        else {
            return ServerResponse.CreateByErrorMessage("参数错误");
        }
        return ServerResponse.CreateBySuccessMessage("校验成功");
    }
}
