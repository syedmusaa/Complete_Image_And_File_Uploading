package com.project.LibraryManagementSystem.Exception;


public class BadRequestException extends RuntimeException {
	
    public BadRequestException(String message) {
    
    	super(message);
    	
//  BadRequestException:- It is use for handling invalid input or bad requests(400).
    
    }
}

