package com.nafisulbari.usis.service;

import com.nafisulbari.usis.entity.User;

import java.util.List;

public interface UserService {

    public User findUserById(int theID);

    public User findUserByEmail(User theUser);

    public void saveOrUpdateUser(User theUser);

    public void deleteUserById(int theID);

    public List<User> findAllUsers();



    public String loginAuthenticator(User theUser);

}
