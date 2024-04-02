package com.instabook.controller;

import cn.hutool.core.util.IdUtil;
import com.instabook.common.model.R;
import com.instabook.model.dos.File;
import com.instabook.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileControllerTest {

    @Mock
    private FileService fileService;

    @InjectMocks
    private FileController fileController;

    private MultipartFile multipartFile;
    private String requestId;
    private File expectedFile;

    @BeforeEach
    void setUp() {
        multipartFile = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Test content".getBytes());
        requestId = "testRequest";
        expectedFile = new File();
        expectedFile.setChannel(2);
        expectedFile.setFileId(IdUtil.getSnowflakeNextId()); // Using Hutool for ID generation
        expectedFile.setOuterId(requestId);
        // Mock the multipart file as needed
        expectedFile.setMultipartFile(multipartFile);
    }

    @Test
    void testUploadMessageFileSuccess() {
        // Setup
        when(fileService.upload(any(File.class))).thenReturn(expectedFile);

        // Execution
        R<File> response = fileController.uploadMessageFile(requestId, multipartFile);

        // Verification
        assertNotNull(response);
        assertEquals(expectedFile, response.getData());
        verify(fileService, times(1)).upload(any(File.class));
    }

    @Test
    void testUploadMessageFileFailure() {
        // Setup
        when(fileService.upload(any(File.class))).thenThrow(new RuntimeException("Upload failed"));

        // Execution & Verification
        Exception exception = assertThrows(RuntimeException.class, () ->
                        fileController.uploadMessageFile(requestId, multipartFile),
                "Expected uploadMessageFile to throw, but it didn't"
        );

        assertTrue(exception.getMessage().contains("Upload failed"));
    }

}

