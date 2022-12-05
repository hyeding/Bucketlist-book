package com.mybucketlistbook.repository;

import com.mybucketlistbook.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryRepositry extends JpaRepository<Entry, Integer> {

    Integer deleteByUserId(Integer userId);
}
