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

import java.io.IOException;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FavoritesRepository favoritesRepository;


    public Iterable<Categories> listCategories() {
        return categoryRepository.findAll();
    }



    public News createNews(NewsRequest newsRequest) throws IOException {
        Categories categories = categoryRepository.findById(newsRequest.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        News news = new News();
        news.setTitulo(newsRequest.title());
        news.setConteudo(newsRequest.content());
        news.setCategoria(categories);
        news.setListImage(newsRequest.listImage().getBytes());   // Set new field
        news.setBodyImage1(newsRequest.bodyImage1().getBytes()); // Set new field
        news.setBodyImage2(newsRequest.bodyImage2().getBytes()); // Set new field

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
}