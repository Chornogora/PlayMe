package com.dataart.playme.controller.rest;

import com.dataart.playme.controller.binding.annotation.CurrentMusician;
import com.dataart.playme.dto.*;
import com.dataart.playme.exception.ConflictException;
import com.dataart.playme.model.Band;
import com.dataart.playme.model.Membership;
import com.dataart.playme.model.Musician;
import com.dataart.playme.model.Post;
import com.dataart.playme.service.BandService;
import com.dataart.playme.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/bands")
public class BandController {

    private final BandService bandService;

    private final PostService postService;

    @Autowired
    public BandController(BandService bandService, PostService postService) {
        this.bandService = bandService;
        this.postService = postService;
    }

    @GetMapping
    public List<Band> getBands(BandFilterBean filterBean) {
        return bandService.findBands(filterBean);
    }

    @GetMapping("/{band}")
    public Band getBand(@PathVariable Band band) {
        band.setMembers(getMembers(band));
        return band;
    }

    @GetMapping("/{band}/members")
    public List<Membership> getMembers(@PathVariable Band band) {
        return band.getMembers();
    }

    @GetMapping("/{band}/posts")
    public PostResponseDto getPostsByBand(@PathVariable Band band, PostRequestDto dto) {
        dto.setBands(Collections.singletonList(band));
        List<Post> posts = postService.getByBands(dto);
        int postAmount = postService.getPostsAmount(Collections.singletonList(band));
        return new PostResponseDto(posts, postAmount);
    }

    @PostMapping
    public Band createBand(@RequestBody @Valid BandCreatingDto dto, @CurrentMusician Musician musician) {
        return bandService.createBand(dto, musician);
    }

    @PostMapping("/{band}/members")
    public Membership addMember(@Valid @RequestBody MemberDto dto,
                                @PathVariable Band band, @CurrentMusician Musician addedBy) {
        if (bandService.isActiveBand(band)) {
            dto.setBand(band);
            return bandService.addMember(dto, addedBy);
        }
        throw new ConflictException("Band is disabled");
    }

    @PostMapping("/{band}/posts")
    public Post createPost(@RequestBody @Valid CreatePostDto dto,
                           @PathVariable Band band,
                           @CurrentMusician Musician currentMusician) {
        if (bandService.isActiveBand(band)) {
            return postService.createPost(dto, band, currentMusician);
        }
        throw new ConflictException("Band is disabled");
    }

    @PostMapping("/{band}/_disable")
    public Band disableBand(@PathVariable Band band) {
        return bandService.disableBand(band);
    }

    @PostMapping("/{band}/_enable")
    public Band activateBand(@PathVariable Band band) {
        return bandService.activateBand(band);
    }

    @PutMapping("/{band}/members")
    public Membership updateMember(@Valid @RequestBody MemberDto dto,
                                   @PathVariable Band band, @CurrentMusician Musician changedBy) {
        if (bandService.isActiveBand(band)) {
            dto.setBand(band);
            return bandService.updateMember(dto, changedBy);
        }
        throw new ConflictException("Band is disabled");
    }

    @PatchMapping("/{band}/")
    public Band updateBand(@Valid BandCreatingDto bandCreatingDto, @PathVariable Band band,
                           @CurrentMusician Musician changedBy) {
        if (bandService.isActiveBand(band)) {
            return bandService.updateBand(bandCreatingDto, band, changedBy);
        }
        throw new ConflictException("Band is disabled");
    }

    @DeleteMapping("/{band}/members/{musician}")
    public void deleteMember(@PathVariable Band band,
                             @PathVariable Musician musician,
                             @CurrentMusician Musician deletedBy) {
        if (bandService.isActiveBand(band)) {
            bandService.deleteMember(band, musician, deletedBy);
        }
        throw new ConflictException("Band is disabled");
    }
}
