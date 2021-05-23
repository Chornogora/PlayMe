package com.dataart.playme.service;

import com.dataart.playme.dto.CreateCommentDto;
import com.dataart.playme.dto.CreatePostDto;
import com.dataart.playme.dto.PostRequestDto;
import com.dataart.playme.model.Band;
import com.dataart.playme.model.Comment;
import com.dataart.playme.model.Musician;
import com.dataart.playme.model.Post;

import java.util.List;

public interface PostService {

    Post createPost(CreatePostDto dto, Band band, Musician creator);

    List<Post> getByBands(PostRequestDto dto);

    Post getPost(String postId);

    void deletePost(Post post, Musician currentMusician);

    Comment createComment(Post post, Musician musician, CreateCommentDto dto);

    List<Comment> getComments(Post post);

    void deleteComment(Comment comment, Musician musician);

    int getPostsAmount(List<Band> bands);
}
