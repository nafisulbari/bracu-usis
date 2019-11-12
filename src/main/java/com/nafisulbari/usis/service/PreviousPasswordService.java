package com.nafisulbari.usis.service;

import com.nafisulbari.usis.entity.PasswordRequest;
import com.nafisulbari.usis.entity.PreviousPassword;
import com.nafisulbari.usis.entity.User;

public interface PreviousPasswordService {

     Boolean findPreviousPasswordByEmail(PasswordRequest passwordRequest);

     void deletePreviousPasswordsOfUser(User user);

     void saveApreviousPassword(PreviousPassword previousPassword);
}
