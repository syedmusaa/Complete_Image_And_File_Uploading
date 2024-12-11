package com.project.LibraryManagementSystem.Exception;

public class ResourceNotFoundException extends RuntimeException {
   
	public ResourceNotFoundException(String message) {
    
		super(message);
//    ResourceNotFoundException:- It is use for handling scenarios when a requested resource (e.g., book) is not found.
	}
}