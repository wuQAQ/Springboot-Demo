package com.wuqaq.mutipartDemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/")
public class Multipart {

    @GetMapping(value = {"", "multipart"})
    public String multipart() {
        return "multipart";
    }

    @GetMapping(value = {"multipart2"})
    public String multipart2() {
        return "multipart2";
    }

    @PostMapping(value = "upload")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                /*
                 * 这段代码执行完毕之后，图片上传到了工程的跟路径； 大家自己扩散下思维，如果我们想把图片上传到
                 * d:/files大家是否能实现呢？ 等等;
                 * 这里只是简单一个例子,请自行参考，融入到实际中可能需要大家自己做一些思考，比如： 1、文件路径； 2、文件名；
                 * 3、文件格式; 4、文件大小的限制;
                 */
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(
                                Objects.requireNonNull(file.getOriginalFilename()))));
                System.out.println(file.getName());
                out.write(file.getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            }

            return "上传成功";

        } else {
            return "上传失败，因为文件是空的.";
        }
    }

    @RequestMapping(value = "/batch/upload", method = RequestMethod.POST)
    @ResponseBody
    public String handleFileUpload(HttpServletRequest request) {
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("file");
        String name=params.getParameter("name");
        System.out.println("name:"+name);
        String id=params.getParameter("id");
        System.out.println("id:"+id);
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(
                            new File(Objects.requireNonNull(file.getOriginalFilename()))));
                    stream.write(bytes);
                    stream.close();
                } catch (Exception e) {
                    stream = null;
                    return "You failed to upload " + i + " => "
                            + e.getMessage();
                }
            } else {
                return "You failed to upload " + i
                        + " because the file was empty.";
            }
        }
        return "upload successful";
    }
}
