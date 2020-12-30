package com.dataart.playme.repository;

import com.dataart.playme.model.Comment;
import com.dataart.playme.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {

    List<Comment> findByPost(Post post);
}
