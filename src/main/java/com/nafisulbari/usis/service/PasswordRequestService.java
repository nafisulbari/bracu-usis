package com.nafisulbari.usis.service;

import com.nafisulbari.usis.entity.PasswordRequest;

import java.util.List;

public interface PasswordRequestService {

     void savePasswordRequest(PasswordRequest thePasswordRequest);

     void rejectByPasswordId(int theID);

     void acceptByPasswordEmail(int theID);

     List<PasswordRequest> findAllPasswordRequest();
}
