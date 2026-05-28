package com.everypicfound.imageasset.domain.generator;

import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class DefaultImageFileNameGenerator implements ImageFileNameGenerator {

    private static final int MAX_FILE_EXT_LENGTH = 20;
    
    @Override
    // 生成图片文件名：把图片ID和文件扩展名拼接成文件名，并返回。
    public String generateFileName(Long imageId, String fileExt) {
        validateImageId(imageId);// 验证图片ID是否合法

        String normalizedFileExt = normalizeFileExt(fileExt);
        validateFileExt(normalizedFileExt);

        // codex: 流程图中 fileName 会进入存储结果和入库命令，这里只允许 imageId.ext 这种稳定格式。
        return imageId + "." + normalizedFileExt;
    }

    // 验证图片ID是否合法
    private void validateImageId(Long imageId) {
        if (imageId == null || imageId <= 0) {
            throw new IllegalArgumentException("imageId must be positive");
        }
    }
    // 验证文件扩展名是否合法
    private String normalizeFileExt(String fileExt) {
        if (fileExt == null) {
            return "";
        }

        String normalized = fileExt.trim().toLowerCase(Locale.ROOT);
        return normalized.startsWith(".") ? normalized.substring(1) : normalized;
    }
    // 验证文件名是否合法
    private void validateFileExt(String fileExt) {
        if (fileExt.isBlank()) {
            throw new IllegalArgumentException("fileExt must not be blank");
        }

        // codex: 防止 "../jpg"、"png/evil"、"tar.gz" 等异常扩展名污染文件名或存储路径。
        if (fileExt.length() > MAX_FILE_EXT_LENGTH || !isAlphaNumeric(fileExt)) {
            throw new IllegalArgumentException("fileExt must contain only letters and numbers");
        }
    }

    // 验证文件扩展名是否只包含字母和数字
    // codex: 文件扩展名只允许 ASCII 字母和数字，避免中文、斜杠、点号等字符进入系统文件名。
    private boolean isAlphaNumeric(String fileExt) {
        for (int i = 0; i < fileExt.length(); i++) {
            char ch = fileExt.charAt(i);
            if (!isAsciiLetterOrDigit(ch)) {
                return false;
            }
        }
        return true;
    }
    // codex: 验证字符是否是 ASCII 字母或数字
    private boolean isAsciiLetterOrDigit(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9');
    }
}
