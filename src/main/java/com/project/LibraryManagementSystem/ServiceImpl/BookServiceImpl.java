package com.project.LibraryManagementSystem.ServiceImpl;


import org.springframework.beans.factory.annotation.Autowired;



import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.LibraryManagementSystem.DTO.BookDTO;
//import com.project.LibraryManagementSystem.DTO.BookDTO;
import com.project.LibraryManagementSystem.Entity.Book;
import com.project.LibraryManagementSystem.Exception.BadRequestException;
import com.project.LibraryManagementSystem.Exception.ResourceNotFoundException;
import com.project.LibraryManagementSystem.Repository.BookRepository;
import com.project.LibraryManagementSystem.Service.BookService;
import com.project.LibraryManagementSystem.Service.FileStorageService;

import jakarta.annotation.PostConstruct;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

//	@Autowired
//    private FileStorageService fileStorageService;

	
    @Autowired
    private BookRepository bookRepository;

        private static final String UPLOAD_DIR = "C:\\Users\\dell\\Desktop\\LibraryManagementSystemChatGpt4\\src\\main\\resources\\static\\uploads";

        @PostConstruct
        public void init() {
            // Create the upload directory if it doesn't exist
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
        }	
        
        public String saveFile(MultipartFile file) throws IOException {
            // Generate a unique file name
//            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        	String fileName = "AmbedhkarLibrary" + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            
            // Save the file to the upload directory
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName; // Return the file name to store in the database
        }


        
        /**
         * Fetch file data by file path
         * 
         * @param filePath The path to the file
         * @return File data as a byte array
         * @throws IOException if the file cannot be found or read
         */
        public byte[] getFile(String filePath) throws IOException {
            Path fullPath = Paths.get(UPLOAD_DIR, filePath);

            if (!Files.exists(fullPath)) {
                throw new IOException("File not found: " + filePath);
            }

            return Files.readAllBytes(fullPath);
        }

	    
	    

	    
	    @Override
	    public Book updateBook(Long bookId, Book updatedBook, MultipartFile file) throws IOException {
	        // Fetch the existing book by ID
	        Book existingBook = bookRepository.findById(bookId)
	                .orElseThrow(() -> new RuntimeException("Book not found"));

	        // Update the book's details
//	        existingBook.setId(updatedBook.getId());
	        existingBook.setTitle(updatedBook.getTitle());
	        existingBook.setAuthor(updatedBook.getAuthor());
	        existingBook.setIsAvailable(updatedBook.getIsAvailable());

	        // If a new file is uploaded, save it and update the imagePath
	        if (file != null && !file.isEmpty()) {
	            String filePath = saveFile(file);
	            existingBook.setImagePath(filePath);
	        }

	        // Save the updated book in the repository
	        return bookRepository.save(existingBook);
	    }

	    
	    @Override
	    public List<BookDTO> getAllBooks() {
	        return bookRepository.findAll().stream()
	                .map(book -> new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getIsAvailable(), book.getImagePath()))
	                .collect(Collectors.toList());
	    }

	    @Override
	    public BookDTO getBookById(Long id) {
	        Book book = bookRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
	        return new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getIsAvailable(), book.getImagePath());
	    }

	    @Override
	    public void deleteBook(Long id) {
	        bookRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
	        bookRepository.deleteById(id);
	    }
	    
}