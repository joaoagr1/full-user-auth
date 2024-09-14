package com.authentication.module.controllers;

import com.authentication.module.domain.News;
import com.authentication.module.domain.Categories;
import com.authentication.module.domain.Favorites;
import com.authentication.module.dtos.NewsRequest;
import com.authentication.module.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @PostMapping("/create")
    public ResponseEntity<News> createNews(@RequestBody NewsRequest news) {
        News createdNews = newsService.createNews(news);
        return ResponseEntity.ok(createdNews);
    }

    @PostMapping("/category/create")
    public ResponseEntity<Categories> createCategory(@RequestBody Categories categories) {
        Categories createdCategories = newsService.createCategory(categories);
        return ResponseEntity.ok(createdCategories);
    }

    @PostMapping("/favorites/add")
    public ResponseEntity<Favorites> addToFavorites(@RequestBody Favorites favorite) {
        Favorites addedFavorite = newsService.addToFavorites(favorite);
        return ResponseEntity.ok(addedFavorite);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity<Iterable<News>> listNews() {
        Iterable<News> news = newsService.listNews();
        return ResponseEntity.ok(news);
    }

    @GetMapping("/categories/list")
    public ResponseEntity<Iterable<Categories>> listCategories() {
        Iterable<Categories> categories = newsService.listCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/listByCategory/{categoryId}")
    public ResponseEntity<List<News>> listNewsByCategory(@PathVariable Long categoryId) {
        List<News> news = newsService.listNewsByCategory(categoryId);
        return ResponseEntity.ok(news);
    }

}