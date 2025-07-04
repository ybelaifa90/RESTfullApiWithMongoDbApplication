package com.usmobile.demo.repository;


import com.usmobile.demo.entity.BookDAO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepositoryInterface extends MongoRepository<BookDAO, String> {
}
