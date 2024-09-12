package com.authentication.module.dtos;

import org.springframework.web.multipart.MultipartFile;

public record NewsRequest(
    String title,
    String content,
    Long categoryId,
    String author,
    String summary,
    MultipartFile listImage,  // New field
    MultipartFile bodyImage1, // New field
    MultipartFile bodyImage2  // New field
) {}