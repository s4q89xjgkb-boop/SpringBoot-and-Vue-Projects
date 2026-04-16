package com.example.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestFileController {
    
    @GetMapping("/user-dir")
    public String getUserDir() {
        return System.getProperty("user.dir");
    }
    
    @GetMapping("/file-path")
    public String getFilePath() {
        return System.getProperty("user.dir") + "/files/";
    }
}
