package com.nafisulbari.usis.repo;

import com.nafisulbari.usis.entity.PreviousPassword;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreviousPasswordRepository extends CrudRepository<PreviousPassword, Integer> {

}
