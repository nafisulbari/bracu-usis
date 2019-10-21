package com.nafisulbari.usis.repo;

import com.nafisulbari.usis.model.PasswordRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRequestRepository extends CrudRepository<PasswordRequest, Integer> {
}
