package com.project.LibraryManagementSystem.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private Boolean isAvailable;
    private String imagePath;
}