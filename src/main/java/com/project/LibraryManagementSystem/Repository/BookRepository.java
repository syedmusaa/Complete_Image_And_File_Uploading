package com.project.LibraryManagementSystem.Repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.LibraryManagementSystem.Entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

}