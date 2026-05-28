package com.everypicfound.storage.infrastructure.local;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; // 基于日期的存储路径生成器
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.everypicfound.storage.core.StoragePathContext;
import com.everypicfound.storage.core.StoragePathGenerator;

// 基于日期的存储路径生成器实现，按照年月日格式生成路径。
@Component
public class DateBasedStoragePathGenerator implements StoragePathGenerator {

    private static final DateTimeFormatter YEAR_FORMATTER = DateTimeFormatter.ofPattern("yyyy");
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MM");
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("dd");
//原本有Locale.getDefault() 的意思是：获取当前 JVM 运行环境的默认语言/地区设置，但在这里我们直接使用 Locale.ENGLISH 来确保日期格式化的一致性，不要受机器环境影响
    // 根据保存请求生成存储路径。
    // 生成基于日期的存储路径，格式为：/{basePath}/{yyyy}/{MM}/{dd}/{imageId}_{timestamp}.{fileExt}
    @Override
    public String generatePath(StoragePathContext context) {
        validateContext(context);// 校验上下文合法性

        LocalDateTime upLoadTime = resolveUploadTime(context);// 获取上传时间
        String fileExt = normalizeFileExt(context.getFileExt());// 获取并规范化文件扩展名

        validateImageId(context.getImageId());// 校验图片ID合法性
        validateFileExt(fileExt);// 校验文件扩展名合法性

        // codex: 这里只生成相对 storagePath，不拼 basePath，也不拼 accessUrlPrefix。
        return YEAR_FORMATTER.format(upLoadTime)
        + "/" + MONTH_FORMATTER.format(upLoadTime)
        + "/" + DAY_FORMATTER.format(upLoadTime)
        + "/" + context.getImageId()
        + "." + fileExt;

    }


    // 校验存储路径上下文的合法性。
    private void validateContext(StoragePathContext context){
    if (context == null) {
        throw new IllegalArgumentException("storage path context must not be null");
    }
}


    // 获取上传时间，如果没有指定，则使用当前时间。
    private LocalDateTime resolveUploadTime(StoragePathContext context) {
        if(context.getUploadTime() == null){
            return LocalDateTime.now();
        }
        return context.getUploadTime();

    }
    // 获取图片ID，如果没有指定，则使用默认值。
    private void validateImageId(Long imageId){
        if(imageId == null || imageId <= 0){
            throw new IllegalArgumentException("imageId must be positive");
        }
    }
    // 获取文件扩展名，并验证扩展名。
    private String normalizeFileExt(String fileExt){
        if(fileExt == null){
            return "";
        }
        String normalized = fileExt.trim().toLowerCase(Locale.ROOT);//去掉前后的空格，并转换为小写
        return normalized.startsWith(".") ? normalized.substring(1) : normalized;//如果以点开头，则去掉点，否则直接返回
    }

    // 验证文件扩展名。
    private void validateFileExt(String fileExt){
        if(fileExt == null || fileExt.isEmpty()){
            throw new IllegalArgumentException("fileExt must not be blank");
        }
        // codex: 防止 "../jpg"、"png/evil"、"tar.gz" 等异常扩展名污染存储路径。
        if(!isAlphaNumeric(fileExt)){
            throw new IllegalArgumentException("fileExt must be alphanumeric");
        }
    }
// 判断字符串是否只包含字母和数字。
    private boolean isAlphaNumeric(String fileExt) {
for(int u = 0;u<fileExt.length();u++){
    char ch = fileExt.charAt(u);
    if(!isAsciiLetterOrDigit(ch)){
        return false;
        }
    }
    return true;
}

// 判断字符是否是 ASCII 字母或数字。
    private boolean isAsciiLetterOrDigit(char ch){
return((ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9'));
    }
}
