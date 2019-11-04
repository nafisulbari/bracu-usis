package com.nafisulbari.usis.repo;

import com.nafisulbari.usis.entity.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CourseRepository extends CrudRepository<Course, Integer> {
}
