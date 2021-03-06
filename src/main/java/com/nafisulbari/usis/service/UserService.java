package com.nafisulbari.usis.service;

import com.nafisulbari.usis.entity.User;

import java.util.List;
//to hide the implementation
public interface UserService {

     User findUserById(int theID);

     User findUserByEmail(User theUser);

     List<User> findAllUsers();

     void saveOrUpdateUser(User theUser);

     void deleteUserById(int theID);


}
