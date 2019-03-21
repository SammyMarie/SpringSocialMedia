package com.sammy.mapper;

import com.sammy.entity.api.PictureApiDTO;
import com.sammy.entity.business.PictureBusinessDTO;
import org.junit.Before;
import org.junit.Test;

import static com.sammy.mapper.PictureMapper.toApi;
import static com.sammy.mapper.PictureMapper.toBusiness;
import static org.assertj.core.api.Assertions.assertThat;

public class PictureMapperTest {

    private PictureBusinessDTO businessDTO;
    private PictureApiDTO apiDTO;

    @Before
    public void setup(){
        apiDTO = PictureApiDTO.builder()
                              .id(2345L)
                              .name("Personal")
                              .build();

        businessDTO = PictureBusinessDTO.builder()
                                        .id(12L)
                                        .name("Individual")
                                        .build();
    }

    @Test
    public void convertToBusiness_shouldSucceed(){
        assertThat(toApi(businessDTO).getId()).isEqualTo(businessDTO.getId());
        assertThat(toApi(businessDTO).getName()).isEqualTo(businessDTO.getName());
    }

    @Test
    public void convertToApi_shouldSucceed(){
        assertThat(toBusiness(apiDTO).getId()).isEqualTo(apiDTO.getId());
        assertThat(toBusiness(apiDTO).getName()).isEqualTo(apiDTO.getName());
    }
}