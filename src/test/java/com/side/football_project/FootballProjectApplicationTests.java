package com.side.football_project;

import com.side.football_project.global.common.service.S3Service;
import com.side.football_project.global.config.S3Config;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


@SpringBootTest
class FootballProjectApplicationTests {
    @MockitoBean
    private S3Config s3Config;

    @MockitoBean
    private S3Service s3Service;

    @Test
    void contextLoads() {
    }
}
