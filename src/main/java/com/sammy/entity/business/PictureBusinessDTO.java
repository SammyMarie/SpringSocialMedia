package com.sammy.entity.business;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Builder
@AllArgsConstructor
@Table(name = "Pictures")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PictureBusinessDTO {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;
}