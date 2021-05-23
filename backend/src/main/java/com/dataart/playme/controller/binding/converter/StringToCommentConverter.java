package com.dataart.playme.controller.binding.converter;

import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.model.Comment;
import com.dataart.playme.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCommentConverter implements Converter<String, Comment> {

    private final CommentRepository commentRepository;

    @Autowired
    public StringToCommentConverter(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment convert(String commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchRecordException("Can't find comment by id"));
    }
}