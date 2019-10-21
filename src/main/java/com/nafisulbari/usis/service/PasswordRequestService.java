package com.nafisulbari.usis.service;

import com.nafisulbari.usis.model.PasswordRequest;

import java.util.List;

public interface PasswordRequestService {

    public void savePasswordRequest(PasswordRequest thePasswordRequest);

    public void rejectByPasswordId(int theID);

    public void acceptByPasswordEmail(int theID);

    public List<PasswordRequest> findAllPasswordRequest();
}
