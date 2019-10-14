package com.nafisulbari.usis.service;

import com.nafisulbari.usis.entity.User;

import java.util.List;

public interface UserService {

    public User findUserById(int theID);

    public User findUserByEmail(User theUser);

    public void saveUser(User theUser);

    public void deleteUserById(int theID);



    public String loginAuthenticator(User theUser);

}
