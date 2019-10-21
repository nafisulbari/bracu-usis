package com.nafisulbari.usis.service;

import com.nafisulbari.usis.entity.PasswordRequest;
import com.nafisulbari.usis.entity.PreviousPassword;

import java.util.List;

public interface PreviousPasswordService {

    public Boolean findPreviousPasswordByEmail(PasswordRequest passwordRequest);
}
