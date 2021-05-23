package com.dataart.playme.repository;

import com.dataart.playme.model.Comment;
import com.dataart.playme.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    List<Comment> findByPost(Post post);
}
