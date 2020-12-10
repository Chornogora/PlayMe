package com.dataart.playme.dto;

import com.dataart.playme.model.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostResponseDto {

    private List<Post> posts;

    private int total;
}
