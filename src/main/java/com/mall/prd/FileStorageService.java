package com.mall.prd;
import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class FileStorageService {
	
	@Value("${file.upload-dir}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        File uploads = new File(uploadDir);

        //프론트에선 파일 첨부를 필수로 지정했어도, 백에서도 첨부파일 없는 경우 대비
        if (!uploads.exists()) {
            uploads.mkdirs();
        }
    }

    public String getUploadDir() {
        return uploadDir;
    }

}
