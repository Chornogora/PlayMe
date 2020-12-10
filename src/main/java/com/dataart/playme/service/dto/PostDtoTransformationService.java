package com.dataart.playme.service.dto;

import com.dataart.playme.dto.CreatePostDto;
import com.dataart.playme.model.Post;

public interface PostDtoTransformationService {

    Post creationDtoToPost(CreatePostDto dto);
}
