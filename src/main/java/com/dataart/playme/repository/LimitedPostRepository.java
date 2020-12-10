package com.dataart.playme.repository;

import com.dataart.playme.dto.PostRequestDto;
import com.dataart.playme.model.Post;

import java.util.List;

public interface LimitedPostRepository {

    List<Post> findByBand(PostRequestDto dto);
}
