package com.everypicfound.imageasset.domain.extractor;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import com.everypicfound.common.exception.BizException;
import com.everypicfound.imageasset.application.command.ImageUploadCommand;
import com.everypicfound.imageasset.error.ImageAssetErrorCode;

@Component
public class DefaultImageMetadataExtractor implements ImageMetadataExtractor {

    // codex: 不再定义 private BufferedImage bufferedImage，避免 Spring 单例组件在并发上传时互相覆盖。

    // 解析图片基础信息,包括宽高、文件大小、扩展名、MIME类型等。
    @Override
    public ImageMetadata extract(ImageUploadCommand command) {
        // codex: 补充 command/inputStream 空值防护，避免元数据解析阶段直接 NullPointerException。
        validateCommand(command);

        BufferedImage image = readImage(command);
        // codex: extract 使用 resolveFileExt 兜底解析扩展名，避免 Validator 未回填 fileExt 时出现 NullPointerException。
        String fileExt = resolveFileExt(command);

        ImageMetadata metadata = new ImageMetadata();
        metadata.setWidth(image.getWidth());
        metadata.setHeight(image.getHeight());
        metadata.setFileSize(command.getFileSize());
        metadata.setMimeType(normalizeMimeType(command.getMimeType()));
        metadata.setFileExt(fileExt);

        command.setFileExt(fileExt);
        command.setWidth(image.getWidth());
        command.setHeight(image.getHeight());

        return metadata;
    }

    @SuppressWarnings("resource")
    private BufferedImage readImage(ImageUploadCommand command) {
        InputStream inputStream = command.getInputStream();
        // BufferedInputStream用于给输入流增加缓冲能力，也支持mark/reset。
        BufferedInputStream bufferedInputStream = inputStream instanceof BufferedInputStream
                ? (BufferedInputStream) inputStream
                : new BufferedInputStream(inputStream);

        command.setInputStream(bufferedInputStream);
        bufferedInputStream.mark(readLimit(command));

        try {
            // ImageIO用于从输入流读取图片数据，并转换为BufferedImage，以便解析宽高等基础信息。
            BufferedImage image = ImageIO.read(bufferedInputStream);
            if (image == null) {
                // codex: 图片内容不可解析统一抛 IMAGE_DECODE_FAILED，和上传校验链路的错误语义保持一致。
                throw new BizException(ImageAssetErrorCode.IMAGE_DECODE_FAILED);
            }
            return image;
        } catch (IOException e) {
            throw new BizException(ImageAssetErrorCode.IMAGE_DECODE_FAILED);
        } finally {
            try {
                // codex: 读取图片后 reset 输入流，避免后续 hash 计算、文件保存读不到内容。
                // 重置输入流，以便后续hash计算、文件保存等步骤可以重新读取输入流中的数据。
                bufferedInputStream.reset();
            } catch (IOException e) {
                throw new BizException(ImageAssetErrorCode.IMAGE_DECODE_FAILED);
            }
        }
    }

    private void validateCommand(ImageUploadCommand command) {
        if (command == null || command.getInputStream() == null) {
            throw new BizException(ImageAssetErrorCode.IMAGE_EMPTY);
        }
    }

    private String resolveFileExt(ImageUploadCommand command) {
        if (command.getFileExt() != null && !command.getFileExt().isBlank()) {
            return normalizeFileExt(command.getFileExt());
        }

        String originalFileName = command.getOriginalFileName();
        if (originalFileName == null || originalFileName.isBlank()) {
            // 如果原始文件名为null或空白字符串，则返回一个空字符串作为文件扩展名。
            return "";
        }

        // lastIndexOf方法用于查找字符串中最后一次出现点号'.'的位置；如果没有点号，则lastDotIndex为-1。
        int lastDotIndex = originalFileName.lastIndexOf('.');
        if (lastDotIndex < 0 || lastDotIndex == originalFileName.length() - 1) {
            return "";
        }

        return normalizeFileExt(originalFileName.substring(lastDotIndex + 1));
    }

    private String normalizeFileExt(String fileExt) {
        if (fileExt == null) {
            // 如果扩展名为null，则返回空字符串。
            return "";
        }

        // 去除前后空格并转换为小写；如果扩展名前带点号，则去掉点号后返回。
        String normalized = fileExt.trim().toLowerCase(Locale.ROOT);
        return normalized.startsWith(".") ? normalized.substring(1) : normalized;
    }

    private String normalizeMimeType(String mimeType) {
        if (mimeType == null) {
            return "";
        }
        return mimeType.trim().toLowerCase(Locale.ROOT);
    }

    private int readLimit(ImageUploadCommand command) {
        Long fileSize = command.getFileSize();
        if (fileSize == null || fileSize >= Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        // 如果文件大小没有超过int上限，则转换为int并加1，作为BufferedInputStream的mark读取限制。
        return fileSize.intValue() + 1;
    }
}
