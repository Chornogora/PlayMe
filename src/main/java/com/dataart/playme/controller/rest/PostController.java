package com.dataart.playme.controller.rest;

import com.dataart.playme.controller.binding.annotation.CurrentMusician;
import com.dataart.playme.dto.CreateCommentDto;
import com.dataart.playme.dto.PostRequestDto;
import com.dataart.playme.dto.PostResponseDto;
import com.dataart.playme.exception.ConflictException;
import com.dataart.playme.model.Band;
import com.dataart.playme.model.Comment;
import com.dataart.playme.model.Musician;
import com.dataart.playme.model.Post;
import com.dataart.playme.service.BandService;
import com.dataart.playme.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    private final BandService bandService;

    @Autowired
    public PostController(PostService postService, BandService bandService) {
        this.postService = postService;
        this.bandService = bandService;
    }

    @GetMapping("/{postId}")
    public Post getPost(@PathVariable String postId) {
        return postService.getPost(postId);
    }

    @GetMapping
    public PostResponseDto getPosts(@RequestParam(name = "bandId") List<String> bandIds, PostRequestDto requestDto) {
        List<Band> bands = bandService.getByMultipleId(bandIds);
        requestDto.setBands(bands);
        List<Post> posts = postService.getByBands(requestDto);
        int postsAmount = postService.getPostsAmount(bands);
        return new PostResponseDto(posts, postsAmount);
    }

    @GetMapping("/{post}/comments")
    public List<Comment> getComments(@PathVariable Post post) {
        return postService.getComments(post);
    }

    @PostMapping("/{post}/comments")
    public Comment addComment(@PathVariable Post post,
                              @CurrentMusician Musician musician,
                              @Valid @RequestBody CreateCommentDto dto,
                              BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            if (bandService.isActiveBand(post.getBand())) {
                return postService.createComment(post, musician, dto);
            }
            throw new ConflictException("Band is disabled");
        }
        throw new BadRequestException("Invalid data");
    }

    @DeleteMapping("/{post}")
    public Post deletePost(@PathVariable Post post,
                           @CurrentMusician Musician musician) {
        if (bandService.isActiveBand(post.getBand())) {
            return postService.deletePost(post, musician);
        }
        throw new ConflictException("Band is disabled");
    }

    @DeleteMapping("/{post}/comments/{comment}")
    public Comment deleteComment(@PathVariable Comment comment,
                                 @CurrentMusician Musician currentMusician) {
        postService.deleteComment(comment, currentMusician);
        return comment;
    }
}
