package com.ibm.repository;

import org.springframework.data.repository.CrudRepository;

import com.ibm.model.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {
}