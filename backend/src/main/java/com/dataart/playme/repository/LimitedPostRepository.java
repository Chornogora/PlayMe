package com.dataart.playme.repository;

import com.dataart.playme.dto.PostRequestDto;
import com.dataart.playme.model.Post;

import java.util.Date;
import java.util.List;

public interface LimitedPostRepository {

    List<Post> findByBand(PostRequestDto dto);

    List<Post> findByBandBefore(PostRequestDto dto, Date date);
}
