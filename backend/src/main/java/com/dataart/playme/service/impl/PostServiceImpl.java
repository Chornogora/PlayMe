package com.dataart.playme.service.impl;

import com.dataart.playme.dto.CreateCommentDto;
import com.dataart.playme.dto.CreatePostDto;
import com.dataart.playme.dto.PostRequestDto;
import com.dataart.playme.exception.ApplicationRuntimeException;
import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.exception.NoSufficientPrivilegesException;
import com.dataart.playme.model.Band;
import com.dataart.playme.model.Comment;
import com.dataart.playme.model.Musician;
import com.dataart.playme.model.Post;
import com.dataart.playme.repository.CommentRepository;
import com.dataart.playme.repository.FileRepository;
import com.dataart.playme.repository.PhotoRepository;
import com.dataart.playme.repository.PostRepository;
import com.dataart.playme.service.BandService;
import com.dataart.playme.service.FileService;
import com.dataart.playme.service.PostService;
import com.dataart.playme.service.dto.PostDtoTransformationService;
import com.dataart.playme.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    private final BandService bandService;

    private final PostDtoTransformationService postDtoTransformationService;

    private final FileService fileService;

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final FileRepository fileRepository;

    private final PhotoRepository photoRepository;

    @Autowired
    public PostServiceImpl(BandService bandService, PostDtoTransformationService postDtoTransformationService,
                           FileService fileService, PostRepository postRepository, CommentRepository commentRepository, FileRepository fileRepository, PhotoRepository photoRepository) {
        this.bandService = bandService;
        this.postDtoTransformationService = postDtoTransformationService;
        this.fileService = fileService;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.fileRepository = fileRepository;
        this.photoRepository = photoRepository;
    }

    @Override
    public Post createPost(CreatePostDto dto, Band band, Musician musician) {
        if (bandService.isMemberOf(band, musician)) {
            if (bandService.canChangePost(band, musician)) {
                return savePost(dto, band, musician);
            }
        }
        throw new NoSufficientPrivilegesException("Musician is not a member of current band");
    }

    @Override
    public List<Post> getByBands(PostRequestDto dto) {
        if (dto.getStartsWith() == null) {
            return postRepository.findByBand(dto);
        }
        return postRepository.findByBandBefore(dto, dto.getStartsWith());
    }

    @Override
    public Post getPost(String postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchRecordException("Cannot find a post"));
    }

    @Override
    public void deletePost(Post post, Musician currentMusician) {
        if (bandService.isMemberOf(post.getBand(), currentMusician)) {
            if (bandService.canChangePost(post.getBand(), currentMusician)) {
                postRepository.delete(post);
                return;
            }
        }
        throw new NoSufficientPrivilegesException("Musician is not a member of current band");
    }

    @Override
    public Comment createComment(Post post, Musician musician, CreateCommentDto dto) {
        Comment comment = new Comment();
        comment.setText(dto.getText());
        comment.setPost(post);
        comment.setAuthor(musician);
        comment.setCreationDatetime(new Date(System.currentTimeMillis()));
        comment.setId(UUID.randomUUID().toString());
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getComments(Post post) {
        return commentRepository.findByPost(post);
    }

    @Override
    public void deleteComment(Comment comment, Musician musician) {
        if (musician.equals(comment.getAuthor())) {
            commentRepository.delete(comment);
        } else {
            throw new NoSufficientPrivilegesException("Only author can delete a comment");
        }
    }

    @Override
    public int getPostsAmount(List<Band> bands) {
        return postRepository.getPostsAmount(bands);
    }

    @Transactional
    protected Post savePost(Post post) {
        photoRepository.saveAll(post.getPhotos());
        fileRepository.saveAll(post.getFiles());
        return postRepository.save(post);
    }

    private Post savePost(CreatePostDto dto, Band band, Musician musician) {
        savePostPhotos(dto);
        savePostFiles(dto);
        Post post = postDtoTransformationService.creationDtoToPost(dto);
        setPostBand(post, band);
        post.setCreator(musician);
        setPostId(post);
        post.setCreationDatetime(new Date(System.currentTimeMillis()));
        return savePost(post);
    }

    private void setPostId(Post post) {
        post.setId(UUID.randomUUID().toString());
        post.getPhotos().forEach(photo -> photo.setId(UUID.randomUUID().toString()));
        post.getFiles().forEach(file -> file.setId(UUID.randomUUID().toString()));
    }

    private void savePostPhotos(CreatePostDto dto) {
        if (dto.getPhotos() != null) {
            dto.getPhotos().forEach(photo -> {
                String imageUrl = createImage(photo.getFileContent(), photo.getFileName());
                photo.setFileUrl(imageUrl);
            });
        }
    }

    private void savePostFiles(CreatePostDto dto) {
        if (dto.getFiles() != null) {
            dto.getFiles().forEach(file -> {
                String fileUrl = createFile(file.getFileContent(), file.getFileName());
                file.setFileUrl(fileUrl);
            });
        }
    }

    private void setPostBand(Post post, Band band) {
        post.setBand(band);
        post.getPhotos().forEach(photo -> photo.setOwner(band));
        post.getFiles().forEach(file -> file.setOwner(band));
    }

    private String createImage(String imageEncoded, String fileName) {
        try {
            byte[] data = Base64.getDecoder().decode(imageEncoded.split(",")[1]);
            String imageDirectory = Constants.get(Constants.IMAGE_ROOT_DIRECTORY_ID);
            return fileService.writeToFile(imageDirectory, fileName, data);
        } catch (IOException e) {
            throw new ApplicationRuntimeException("Cannot write image to file");
        }
    }

    private String createFile(String fileEncoded, String fileName) {
        try {
            byte[] data = Base64.getDecoder().decode(fileEncoded.split(",")[1]);
            String fileDirectory = Constants.get(Constants.FILE_ROOT_DIRECTORY_ID);
            return fileService.writeToFile(fileDirectory, fileName, data);
        } catch (IOException e) {
            throw new ApplicationRuntimeException("Cannot write file");
        }
    }
}
