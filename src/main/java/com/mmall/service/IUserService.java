package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by dizk on 2017/9/13.
 */
public interface IUserService {

    ServerResponse<User> login(String username, String password);

}
