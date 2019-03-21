package com.sammy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringSocialMediaApplicationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void checkContextLoad(){
        assertThat(context).isNotNull();
    }
}