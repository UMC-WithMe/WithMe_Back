package com.umc.withme.dto.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MessageCreateRequest {

    @NotBlank @Size(max = 500)
    private String content;

}
