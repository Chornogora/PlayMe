package com.dataart.playme.controller.rest;

import com.dataart.playme.controller.binding.annotation.CurrentMusician;
import com.dataart.playme.dto.CreatePostDto;
import com.dataart.playme.dto.PostRequestDto;
import com.dataart.playme.dto.PostResponseDto;
import com.dataart.playme.model.Band;
import com.dataart.playme.model.Musician;
import com.dataart.playme.model.Post;
import com.dataart.playme.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/bands")
public class BandController {

    private final PostService postService;

    @Autowired
    public BandController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{band}/posts")
    public PostResponseDto getPostsByBand(@PathVariable Band band, PostRequestDto dto) {
        dto.setBands(Collections.singletonList(band));
        List<Post> posts = postService.getByBands(dto);
        int postAmount = postService.getPostsAmount(Collections.singletonList(band));
        return new PostResponseDto(posts, postAmount);
    }

    @PostMapping("/{band}/posts")
    public Post createPost(@RequestBody @Valid CreatePostDto dto,
                           BindingResult bindingResult,
                           @PathVariable Band band,
                           @CurrentMusician Musician currentMusician) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Invalid parameters");
        }
        return postService.createPost(dto, band, currentMusician);
    }
}
