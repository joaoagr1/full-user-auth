package com.authentication.module.services;

import com.authentication.module.domain.News;
import com.authentication.module.domain.Categories;
import com.authentication.module.domain.Favorites;
import com.authentication.module.dtos.NewsRequest;
import com.authentication.module.repositories.NewsRepository;
import com.authentication.module.repositories.CategoryRepository;
import com.authentication.module.repositories.FavoritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FavoritesRepository favoritesRepository;


    public News createNews(NewsRequest newsRequest) {
        Categories categories = categoryRepository.findById(newsRequest.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        News news = new News();
        news.setTitulo(newsRequest.title());
        news.setConteudo(newsRequest.content());
        news.setCategoria(categories);

        return newsRepository.save(news);
    }

    public Categories createCategory(Categories categories) {
        return categoryRepository.save(categories);
    }

    public Favorites addToFavorites(Favorites favorite) {
        return favoritesRepository.save(favorite);
    }

    public void deleteNews(Long newsId) {
        newsRepository.deleteById(newsId);
    }

    public Iterable<News> listNews() {
        return newsRepository.findAll();
    }

    public Iterable<Categories> listCategories() {
        return categoryRepository.findAll();
    }

    public List<News> listNewsByCategory(Long categoryId) {
        Categories category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return newsRepository.findByCategoria(category);
    }

}