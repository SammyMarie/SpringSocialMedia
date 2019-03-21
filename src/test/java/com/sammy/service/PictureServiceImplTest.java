package com.sammy.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

//@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class PictureServiceImplTest {

    @Autowired
    private PictureService pictureService;

    @Test
    public void checkContextLoads(){
        assertThat(pictureService).isNotNull();
    }

    @Test
    public void checkGetResource_shouldSucceed(){
        assertThat(pictureService.findOneImage("individual")).isNotNull().isInstanceOf(Resource.class);
    }

    /*@Test
    public void checkCreatePicture_shouldSucceed() throws IOException{
        pictureService.createPicture(new MockMultipartFile("", new byte[]{}));
        assertThat(pictureService.findOneImage("")).isNotNull();
    }*/

    /*@Test
    public void checkDeletePicture_shouldSucceed() throws IOException {
        pictureService.deletePicture("individual");
        assertThat(pictureService.findOneImage("individual")).isNull();
    }*/
}