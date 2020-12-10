package com.dataart.playme.service.dto.impl;

import com.dataart.playme.dto.CreatePostDto;
import com.dataart.playme.model.Post;
import com.dataart.playme.service.dto.PostDtoTransformationService;
import org.springframework.stereotype.Service;

@Service
public class PostDtoTransformationServiceImpl implements PostDtoTransformationService {

    @Override
    public Post creationDtoToPost(CreatePostDto dto) {
        Post post = new Post();
        post.setText(dto.getText());
        post.setPhotoURL(dto.getPhoto());
        post.setFileURL(dto.getFile());
        return post;
    }
}
