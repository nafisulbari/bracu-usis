package com.nafisulbari.usis.repo;

import com.nafisulbari.usis.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//gives builtin CRUD access on entity

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String s);
}
