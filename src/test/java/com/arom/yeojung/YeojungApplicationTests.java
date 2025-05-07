package com.arom.yeojung;

import com.arom.yeojung.service.FileS3UploadService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class YeojungApplicationTests {

	@MockitoBean
	private FileS3UploadService fileS3UploadService;

	@Test
	void contextLoads() {
	}

}
