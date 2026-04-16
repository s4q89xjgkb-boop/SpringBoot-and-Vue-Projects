package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import com.example.common.Result;
import com.example.exception.CustomerException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileController {

    // 文件存储根路径（使用绝对路径确保可靠性）
    private static final String FILE_ROOT_PATH = "d:/lunwen/doc/code2025/files/";
    private static final String VIDEO_PATH = FILE_ROOT_PATH + "video/";

    /**
     * 文件下载
     */
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws Exception {
        // 首先尝试在视频目录中查找
        String realPath = VIDEO_PATH + fileName;
        boolean exist = FileUtil.exist(realPath);
        
        // 如果视频目录中不存在，再在根目录中查找
        if (!exist) {
            realPath = FILE_ROOT_PATH + fileName;
            exist = FileUtil.exist(realPath);
            if (!exist) {
                throw new CustomerException("文件不存在");
            }
        }
        
        // 根据文件扩展名设置正确的Content-Type
        String extName = FileUtil.extName(fileName).toLowerCase();
        String contentType;
        switch (extName) {
            case "jpg":
            case "jpeg":
                contentType = "image/jpeg";
                break;
            case "png":
                contentType = "image/png";
                break;
            case "gif":
                contentType = "image/gif";
                break;
            case "webp":
                contentType = "image/webp";
                break;
            case "mp4":
                contentType = "video/mp4";
                break;
            case "avi":
                contentType = "video/avi";
                break;
            case "mov":
                contentType = "video/quicktime";
                break;
            case "wmv":
                contentType = "video/x-ms-wmv";
                break;
            case "flv":
                contentType = "video/x-flv";
                break;
            case "mkv":
                contentType = "video/x-matroska";
                break;
            default:
                contentType = "application/octet-stream";
        }
        
        // 设置正确的响应头
        response.setContentType(contentType);
        // 对于图片和视频资源，不要设置attachment，让浏览器直接显示/播放
        if (!contentType.equals("application/octet-stream")) {
            response.setHeader("Content-Disposition", "inline;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
        } else {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
        }
        
        // 设置CORS头
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
        
        byte[] bytes = FileUtil.readBytes(realPath);
        ServletOutputStream os = response.getOutputStream();
        os.write(bytes);
        os.flush();
        os.close();
    }

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new CustomerException("请选择要上传的文件");
        }
        
        System.out.println("开始处理文件上传");
        System.out.println("文件名: " + file.getOriginalFilename());
        System.out.println("文件大小: " + file.getSize() + " bytes");
        System.out.println("文件类型: " + file.getContentType());
        
        // 确保根目录存在
        System.out.println("检查根目录: " + FILE_ROOT_PATH);
        if (!FileUtil.isDirectory(FILE_ROOT_PATH)) {
            System.out.println("创建根目录: " + FILE_ROOT_PATH);
            FileUtil.mkdir(FILE_ROOT_PATH);
        }
        
        // 检查文件类型，为视频创建单独目录
        String contentType = file.getContentType();
        String savePath = FILE_ROOT_PATH;
        if (contentType != null && contentType.startsWith("video/")) {
            savePath = VIDEO_PATH;
            System.out.println("视频文件，保存路径: " + savePath);
            if (!FileUtil.isDirectory(savePath)) {
                System.out.println("创建视频目录: " + savePath);
                FileUtil.mkdir(savePath);
            }
        } else {
            System.out.println("非视频文件，保存路径: " + savePath);
        }
        
        try {
            byte[] bytes = file.getBytes();
            System.out.println("文件读取成功，大小: " + bytes.length + " bytes");
            
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String fullFilePath = savePath + fileName;
            System.out.println("准备写入文件: " + fullFilePath);
            
            FileUtil.writeBytes(bytes, fullFilePath);
            System.out.println("文件写入成功");
            
            String url = "/files/download/" + fileName;
            System.out.println("上传成功，返回URL: " + url);
            return Result.success(url);
        } catch (Exception e) {
            System.err.println("文件上传失败: " + e.getMessage());
            e.printStackTrace();
            throw new CustomerException("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 新增：删除文件（根据完整URL或文件名）
     * @param fileUrl 旧头像的完整URL（如 http://localhost:1306/files/download/1736542312345_test.jpg）
     */
    @DeleteMapping("/delete")
    public Result deleteFile(@RequestParam String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return Result.success("无旧文件需要删除");
        }
        // 从URL中提取文件名（关键：截取 /download/ 后面的部分）
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/download/") + 10);
        
        // 先尝试在视频目录中查找
        String realPath = VIDEO_PATH + fileName;
        if (!FileUtil.exist(realPath)) {
            // 如果视频目录中不存在，再在根目录中查找
            realPath = FILE_ROOT_PATH + fileName;
        }
        
        // 判断文件是否存在，存在则删除
        if (FileUtil.exist(realPath)) {
            FileUtil.del(realPath);
            return Result.success("旧文件删除成功");
        } else {
            return Result.success("旧文件不存在，无需删除");
        }
    }
    /**
     * wang-editor编辑器文件上传接口
     */
    @PostMapping("/wang/upload")
    public Map<String, Object> wangEditorUpload(MultipartFile file) {
        String flag = System.currentTimeMillis() + "";
        String fileName = file.getOriginalFilename();
        Map<String, Object> resMap = new HashMap<>();
        
        try {
            // 检查文件类型，为视频创建单独目录
            String contentType = file.getContentType();
            String savePath = FILE_ROOT_PATH;
            if (contentType != null && contentType.startsWith("video/")) {
                savePath = VIDEO_PATH;
                if (!FileUtil.isDirectory(savePath)) {
                    FileUtil.mkdir(savePath);
                }
            } else {
                if (!FileUtil.isDirectory(savePath)) {
                    FileUtil.mkdir(savePath);
                }
            }
            
            // 文件存储形式：时间戳-文件名
            String fullFileName = flag + "-" + fileName;
            FileUtil.writeBytes(file.getBytes(), savePath + fullFileName);
            System.out.println(fileName + "--上传成功");
            
            String http = "http://localhost:1306/files/download/";
            // wangEditor上传成功后， 需要返回的参数
            resMap.put("errno", 0);
            resMap.put("data", CollUtil.newArrayList(Dict.create().set("url", http + fullFileName)));
        } catch (Exception e) {
            System.err.println(fileName + "--文件上传失败: " + e.getMessage());
            // wangEditor上传失败后， 需要返回的参数
            resMap.put("errno", 1);
            resMap.put("data", CollUtil.newArrayList());
        }
        
        return resMap;
    }
}