package com.dataart.playme.service.dto.impl;

import com.dataart.playme.dto.CreatePostDto;
import com.dataart.playme.model.File;
import com.dataart.playme.model.Photo;
import com.dataart.playme.model.Post;
import com.dataart.playme.service.dto.PostDtoTransformationService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class PostDtoTransformationServiceImpl implements PostDtoTransformationService {

    @Override
    public Post creationDtoToPost(CreatePostDto dto) {
        Post post = new Post();
        post.setText(dto.getText());

        post.setPhotos(dto.getPhotos().stream()
                .map(fileDto -> {
                    Photo photo = new Photo();
                    photo.setPhotoUrl(fileDto.getFileUrl());
                    return photo;
                })
                .collect(Collectors.toList()));

        post.setFiles(dto.getFiles().stream()
                .map(fileDto -> {
                    File file = new File();
                    file.setFileUrl(fileDto.getFileUrl());
                    return file;
                })
                .collect(Collectors.toList()));

        return post;
    }
}
