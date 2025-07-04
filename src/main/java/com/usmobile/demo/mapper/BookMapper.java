package com.usmobile.demo.mapper;

import com.usmobile.demo.core.BookDTO;
import com.usmobile.demo.entity.BookDAO;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Component;

/**
 * Mapper class responsible for converting between BookDTO and BookDAO objects.
 * Uses ModelMapper to perform the mapping.
 */
@Component
public class BookMapper {

    private final ModelMapper modelMapper;

    /**
     * Constructs a new BookMapper instance and configures the ModelMapper.
     * The ModelMapper is configured to ignore null values and enable field matching.
     */
    public BookMapper() {
        this.modelMapper = new ModelMapper();

        // Configure ModelMapper to ignore null values and enable field matching
        this.modelMapper.getConfiguration()
                .setPropertyCondition(Conditions.isNotNull()) // Ignores nulls
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
    }

    /**
     * Maps a BookDTO object to a BookDAO object.
     *
     * @param bookDTO the BookDTO object to map
     * @return the mapped BookDAO object
     */
    public BookDAO toDAO(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, BookDAO.class);
    }

    /**
     * Maps a BookDAO object to a BookDTO object.
     *
     * @param bookDAO the BookDAO object to map
     * @return the mapped BookDTO object
     */
    public BookDTO toCore(BookDAO bookDAO) {
        return modelMapper.map(bookDAO, BookDTO.class);
    }
}