package com.dataart.playme.controller.binding.converter;

import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.model.Post;
import com.dataart.playme.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToPostConverter implements Converter<String, Post> {

    private final PostRepository postRepository;

    @Autowired
    public StringToPostConverter(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post convert(String postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchRecordException("Can't find post by id"));
    }
}