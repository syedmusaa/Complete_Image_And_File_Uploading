package com.project.LibraryManagementSystem.Service;



import java.io.IOException;

import java.util.List;


import org.springframework.web.multipart.MultipartFile;

import com.project.LibraryManagementSystem.DTO.BookDTO;
import com.project.LibraryManagementSystem.Entity.Book;


public interface BookService {

	public String saveFile(MultipartFile file) throws IOException;
	public byte[] getFile(String filePath) throws IOException;
    List<BookDTO> getAllBooks();
    BookDTO getBookById(Long id);
    Book updateBook(Long bookId, Book updatedBook, MultipartFile file) throws Exception;
    void deleteBook(Long id);
}