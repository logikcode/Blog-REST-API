package com.manuel.blogrestapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeDTO {

    private Long post;
    private Long comment;
    private Long user;

}