package com.dataart.playme.controller.rest;

import com.dataart.playme.controller.binding.annotation.ActiveBand;
import com.dataart.playme.controller.binding.annotation.CurrentMusician;
import com.dataart.playme.dto.*;
import com.dataart.playme.model.Band;
import com.dataart.playme.model.Membership;
import com.dataart.playme.model.Musician;
import com.dataart.playme.model.Post;
import com.dataart.playme.service.BandService;
import com.dataart.playme.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
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
                                @ActiveBand Band band, @CurrentMusician Musician addedBy) {
        dto.setBand(band);
        return bandService.addMember(dto, addedBy);
    }

    @PostMapping("/{band}/posts")
    public Post createPost(@RequestBody @Valid CreatePostDto dto,
                           @ActiveBand Band band,
                           @CurrentMusician Musician currentMusician) {
        if (dto.getFiles() == null && dto.getPhotos() == null && dto.getText() == null
                || dto.getFiles() == null && dto.getPhotos() == null && dto.getText().length() == 0) {
            throw new BadRequestException("No data provided");
        }
        return postService.createPost(dto, band, currentMusician);
    }

    @PreAuthorize("hasAnyRole('ROLE_administrator')")
    @PostMapping("/{band}/_disable")
    public Band disableBand(@PathVariable Band band) {
        return bandService.disableBand(band);
    }

    @PreAuthorize("hasAnyRole('ROLE_administrator')")
    @PostMapping("/{band}/_enable")
    public Band activateBand(@PathVariable Band band) {
        return bandService.activateBand(band);
    }

    @PutMapping("/{band}/members")
    public Membership updateMember(@Valid @RequestBody MemberDto dto,
                                   @ActiveBand Band band, @CurrentMusician Musician changedBy) {
        dto.setBand(band);
        return bandService.updateMember(dto, changedBy);
    }

    @PatchMapping("/{band}")
    public Band updateBand(@RequestBody @Valid BandCreatingDto bandCreatingDto, @ActiveBand Band band,
                           @CurrentMusician Musician changedBy) {
        return bandService.updateBand(bandCreatingDto, band, changedBy);
    }

    @DeleteMapping("/{band}/members/{musician}")
    public void deleteMember(@ActiveBand Band band,
                             @PathVariable Musician musician,
                             @CurrentMusician Musician deletedBy) {
        bandService.deleteMember(band, musician, deletedBy);
    }
}
