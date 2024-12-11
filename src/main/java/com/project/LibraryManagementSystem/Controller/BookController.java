 package com.project.LibraryManagementSystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.project.LibraryManagementSystem.DTO.BookDTO;
//import com.project.LibraryManagementSystem.DTO.BookDTO;
import com.project.LibraryManagementSystem.Entity.Book;
import com.project.LibraryManagementSystem.Repository.BookRepository;
import com.project.LibraryManagementSystem.Service.BookService;
import com.project.LibraryManagementSystem.ServiceImpl.BookServiceImpl;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;
    
    @Autowired
    private BookRepository bookRepository;
    
    
    @PostMapping("/upload")
    public ResponseEntity<String> uploadBookImage(@RequestParam("file") MultipartFile file, 
                                                  @RequestParam("title") String title,
                                                  @RequestParam("author") String author,
                                                  @RequestParam("isAvailable") Boolean isAvailable) {
        try {
            // Save the file and get the file name
            String fileName = bookService.saveFile(file);

            // Save book details in the database
            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setIsAvailable(isAvailable);
            book.setImagePath(fileName);

            bookRepository.save(book);

            return ResponseEntity.ok("Book and image uploaded successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
        }
    }

    
    
    
    @PutMapping("/update/{bookId}")
    public ResponseEntity<?> updateBook(@PathVariable Long bookId,
//    		                            @RequestParam(value = "bookId", required = false) Long id,
                                        @RequestParam("title") String title,
                                        @RequestParam("author") String author,
                                        @RequestParam("isAvailable") Boolean isAvailable,
                                        @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            // Create an updated Book object with the new data
            Book updatedBook = new Book();
//            updatedBook.setId(id);
            updatedBook.setTitle(title);
            updatedBook.setAuthor(author);
            updatedBook.setIsAvailable(isAvailable);

            // Call the service to update the book
            Book updatedBookData = bookService.updateBook(bookId, updatedBook, file);

            // Return the updated book as JSON
            return ResponseEntity.ok(updatedBookData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating book: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Get file by ID or fileName
     */
    @GetMapping("/view")
    public ResponseEntity<byte[]> getFile(
            @RequestParam(required = false) Long id, 
            @RequestParam(required = false) String fileName
    ) {
        try {
            byte[] fileData;
            String filePath;

            // Handle retrieval by ID
            if (id != null) {
                Book book = bookRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Book not found"));
                filePath = book.getImagePath();
            } 
            // Handle retrieval by fileName
            else if (fileName != null) {
                filePath = fileName;
            } else {
                throw new IllegalArgumentException("Either id or fileName must be provided");
            }

            // Fetch the file data
            fileData = bookService.getFile(filePath);

            // Set appropriate headers
            HttpHeaders headers = new HttpHeaders();
            String fileExtension = filePath.substring(filePath.lastIndexOf('.') + 1);
            headers.setContentType(getMediaType(fileExtension));
            headers.setContentDisposition(ContentDisposition.inline().filename(filePath).build());

            return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Determine MediaType based on file extension
     */
    private MediaType getMediaType(String extension) {
        switch (extension.toLowerCase()) {
            case "png":
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "pdf":
                return MediaType.APPLICATION_PDF;
            case "gif":
                return MediaType.IMAGE_GIF;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
    }
