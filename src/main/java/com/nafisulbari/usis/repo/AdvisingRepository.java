package com.nafisulbari.usis.repo;

import com.nafisulbari.usis.entity.Advising;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvisingRepository extends CrudRepository<Advising, Integer> {
}
