package com.dataart.playme.repository;

import com.dataart.playme.model.Band;
import com.dataart.playme.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, String>, LimitedPostRepository {

    @Query("SELECT COUNT(p) FROM Post p WHERE p.band IN :#{#bands}")
    int getPostsAmount(@Param("bands") List<Band> bands);
}
