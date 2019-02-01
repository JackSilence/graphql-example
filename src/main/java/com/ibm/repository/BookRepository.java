package com.ibm.repository;

import org.springframework.data.repository.CrudRepository;

import com.ibm.model.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
}