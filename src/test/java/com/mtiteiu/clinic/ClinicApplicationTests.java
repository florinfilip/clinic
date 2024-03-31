package com.mtiteiu.clinic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class ClinicApplicationTests {

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();



    @Test
    void contextLoads() {
        System.out.println(passwordEncoder.encode("parola"));
    }

}
