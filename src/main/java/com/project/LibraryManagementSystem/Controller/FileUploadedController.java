//package com.project.LibraryManagementSystem.Controller;
//
//import java.io.IOException;
//
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.InputStreamResource;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequestMapping("/books")
//public class FileUploadedController {
//	
//	@Value("${file.upload-dir:C:\\Users\\dell\\OneDrive\\Desktop\\LibraryManagementSystem\\uploads}")
//	private String uploadDir;
//
////	@Value("${file.upload-dir}")
////    private String uploadDir;
//
//
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return ResponseEntity.badRequest().body("No file selected");
//        }
//         
//        try {
//            // Ensure the upload directory exists
//            Path path = Paths.get(uploadDir + file.getOriginalFilename());
//            Files.createDirectories(path.getParent()); // Ensure parent directories exist
//            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//            return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
//        } catch (IOException e) {
//            e.printStackTrace(); // Log exception for debugging
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
//        }
//    }
//    
//    @GetMapping("/{filename}")
//    public ResponseEntity<InputStreamResource> serveFile(@PathVariable String filename) {
//        // Path to the file
//        Path filePath = Paths.get(uploadDir).resolve(filename);
//        
//        // Check if the file exists
//        if (Files.exists(filePath)) {
//            try {
//                // Create an InputStream from the file
//                InputStream inputStream = Files.newInputStream(filePath);
//
//                // Get the content type of the file
//                String contentType = Files.probeContentType(filePath);
//                if (contentType == null) {
//                    contentType = "application/octet-stream"; // Default content type
//                }
//                
//                // Return the file as a response with the content type
//                return ResponseEntity.ok()
//                        .contentType(MediaType.parseMediaType(contentType))
//                        .body(new InputStreamResource(inputStream));
//            } catch (Exception e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body(null);  // Return a 500 if something goes wrong
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(null);  // Return a 404 if the file is not found
//        }
//    }
//}
//    
