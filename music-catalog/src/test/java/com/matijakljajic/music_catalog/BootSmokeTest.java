package com.matijakljajic.music_catalog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("h2")
class BootSmokeTest {
    @Test
    void contextLoads() {
        // just verify Spring context starts
    }
}
