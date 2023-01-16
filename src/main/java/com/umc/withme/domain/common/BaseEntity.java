package com.umc.withme.domain.common;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@MappedSuperclass
public abstract class BaseEntity extends BaseTimeEntity {

//    TODO: 인증 기능 구현하며 어떤 값을 넣을지 고민 필요
//    @CreatedBy
//    @Column(nullable = false, updatable = false)
//    protected String createdBy;

//    @LastModifiedBy
//    @Column(nullable = false,)
//    protected String modifiedBy;
}

