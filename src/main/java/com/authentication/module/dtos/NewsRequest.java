package com.authentication.module.dtos;

public record NewsRequest(String title, String content, Long categoryId) {
}