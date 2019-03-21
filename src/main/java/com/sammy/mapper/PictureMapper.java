package com.sammy.mapper;

import com.sammy.entity.api.PictureApiDTO;
import com.sammy.entity.business.PictureBusinessDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PictureMapper {

    public static PictureApiDTO toApi(PictureBusinessDTO businessDTO){
        return PictureApiDTO.builder()
                            .id(businessDTO.getId())
                            .name(businessDTO.getName())
                            .build();
    }

    public static PictureBusinessDTO toBusiness(PictureApiDTO apiDTO){
        return PictureBusinessDTO.builder()
                                 .id(apiDTO.getId())
                                 .name(apiDTO.getName())
                                 .build();
    }

    public static Page<PictureApiDTO> toApiPageable(Page<PictureBusinessDTO> businessDTO){
        return businessDTO.map(PictureMapper::toApi);
    }
}