package com.usmobile.demo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "book")
public class BookDAO {


    @Id
    private String id;

    private String title;

    private String author;

    @Indexed(unique = true)
    private String isbn;

    private LocalDate publishedDate;


}
