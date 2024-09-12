package com.authentication.module.repositories;

import com.authentication.module.domain.Categories;
import com.authentication.module.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByCategoria(Categories category);
}
