package com.everypicfound.imageasset.domain.validator;//DefaultImageUploadValidator是一个实现了ImageUploadValidator接口的类，负责对上传的图片进行默认的校验。它包含了一系列的校验方法，例如validateFileNotEmpty、validateFileSize、validateFileExt、validateMimeType和validateImageReadable等。这些方法分别用于校验文件是否为空、文件大小是否超过限制、文件扩展名是否受支持、MIME类型是否受支持以及图片是否可解析等。通过调用这些校验方法，可以确保上传的图片符合预期的要求和规范。

import java.awt.image.BufferedImage;//BizException是一个业务异常类，通常用于表示业务逻辑错误。
import java.io.BufferedInputStream;//ImageUploadCommand是一个命令对象，通常用于封装上传图片的相关信息，例如文件大小、输入流、文件扩展名、MIME类型等。在DefaultImageUploadValidator类中，validate方法接受一个ImageUploadCommand对象作为参数，通过调用不同的校验方法，对上传图片的相关数据进行验证，确保其合法性和符合预期的要求。
import java.io.IOException; //ImageAssetErrorCode是一个枚举类，用于定义与图像资产相关的错误代码。它包含了一系列常量，每个常量代表一个特定的错误情况，例如文件过大、文件类型不支持等。通过使用ImageAssetErrorCode，可以在代码中统一管理和处理与图像资产相关的错误，提高代码的可读性和维护性。
import java.io.InputStream;//StorageProperties是Spring Boot提供的一个配置类，用于管理存储相关的配置信息。
import java.util.Locale;//@RequiredArgsConstructor是Lombok库提供的一个注解，用于自动生成一个包含所有final字段的构造函数。它可以简化代码，减少样板代码的编写。当一个类被标注为@RequiredArgsConstructor时，Lombok会自动生成一个构造函数，该构造函数接受所有被声明为final的字段作为参数，并将它们赋值给相应的字段。这对于依赖注入和不可变对象非常有用，可以提高代码的可读性和维护性。
import java.util.Set;

import javax.imageio.ImageIO;//ImageIO是Java中的一个类，用于读取和写入图像文件。它提供了静态方法来处理各种图像格式，例如JPEG、PNG等。ImageIO可以用于从文件、输入流等来源读取图像数据，并将其转换为BufferedImage对象进行操作。

import org.springframework.stereotype.Component;//BufferedImage是Java中的一个类，用于表示图像数据。它提供了丰富的方法来操作图像，例如获取像素值、修改图像内容等。BufferedImage可以用于处理各种类型的图像文件，如JPEG、PNG等。

import com.everypicfound.common.exception.BizException;//BufferedInputStream是Java中的一个类，用于提供缓冲功能的输入流。它通过在内存中创建一个缓冲区来提高读取效率，减少对底层输入流的访问次数。BufferedInputStream可以包装任何InputStream对象，例如FileInputStream、ByteArrayInputStream等。
import com.everypicfound.imageasset.application.command.ImageUploadCommand;//IOException是Java中的一个异常类，用于表示输入输出操作中发生的错误。它是一个受检异常，必须在代码中进行处理或声明抛出。IOException可以在文件操作、网络通信等场景中发生，例如文件不存在、网络连接失败等。
import com.everypicfound.imageasset.error.ImageAssetErrorCode;//InputStream是Java中的一个抽象类，用于表示输入流。它提供了一系列方法来读取数据，例如read()、read(byte[] b)等。InputStream可以用于读取文件、网络连接等各种数据源。
import com.everypicfound.storage.infrastructure.config.StorageProperties;//Locale是Java中的一个类，用于表示特定的地理、政治或文化区域。它包含了语言、国家和变体等信息，可以用于国际化和本地化的操作。Locale类提供了多种方法来获取和设置区域信息，例如getLanguage()、getCountry()等。

import lombok.RequiredArgsConstructor;//set是一个接口，表示一个不包含重复元素的集合。它继承自Collection接口，并且不保证元素的顺序。Set接口有多个实现类，如HashSet、Linked

@Component
@RequiredArgsConstructor
public class DefaultImageUploadValidator implements ImageUploadValidator {

    private static final Set<String> DEFAULT_ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "bmp","webp");
    private static final Set<String> DEFAULT_ALLOWED_MIME_TYPES = Set.of("image/jpeg", "image/png", "image/gif", "image/bmp","image/webp");
    // 统一校验上传图片。
    @Override
    public void validate(ImageUploadCommand command) {
        validateFileNotEmpty(command);
        validateFileSize(command);
        validateFileExt(command);
        validateMimeType(command);
        validateImageReadable(command);
    }
    /*command是一个对象，通常用于封装上传图片的相关信息，例如文件大小、输入流、文件扩展名、MIME类型等。
    在validate方法中，传入了一个ImageUploadCommand对象作为参数，这个对象包含了上传图片的相关数据。通过调用不同的校验方法，可以对这些数据进行验证，确保上传的图片符合预期的要求和规范。
*/
    private final StorageProperties storageProperties;
    //StorageProperties是一个配置类，通常用于存储相关的配置信息，例如文件大小限制、允许的文件扩展名、允许的MIME类型等。在DefaultImageUploadValidator类中，通过注入StorageProperties对象，可以获取这些配置信息，并在校验方法中使用它们来验证上传图片的合法性。
    // 校验文件不能为空。
    public void validateFileNotEmpty(ImageUploadCommand command) {
        if(command.getFileSize() == null ||command.getFileSize() <= 0
        || command.getInputStream() == null||command!= null){
            throw new BizException(ImageAssetErrorCode.IMAGE_EMPTY);
            
        }
        
    }

    // 校验文件大小。
    public void validateFileSize(ImageUploadCommand command) {
        Long maxFileSize=storageProperties.getMaxFileSize();
        if(maxFileSize!=null && command.getFileSize()>maxFileSize)
        {
            throw new BizException(ImageAssetErrorCode.IMAGE_SIZE_EXCEEDED);
        }

    }

    // 校验文件扩展名。
    public void validateFileExt(ImageUploadCommand command) {
        String fileExt=command.getFileExt();
        if(fileExt==null || !DEFAULT_ALLOWED_EXTENSIONS.contains(fileExt.toLowerCase(Locale.ROOT))){
            throw new BizException(ImageAssetErrorCode.IMAGE_FORMAT_UNSUPPORTED);
        }
    }
    //!DEFAULT_ALLOWED_EXTENSIONS.contains(fileExt.toLowerCase(Locale.ROOT))是一个条件表达式，用于检查文件扩展名是否在允许的扩展名列表中。它首先将文件扩展名转换为小写字母（使用toLowerCase(Locale.ROOT)），然后检查该扩展名是否存在于DEFAULT_ALLOWED_EXTENSIONS集合中。如果文件扩展名不在允许的列表中，则条件表达式返回true，表示文件类型不受支持，进而抛出一个BizException异常，错误代码为IMAGE_FORMAT_UNSUPPORTED。

    // 校验 MIME 类型。
    public void validateMimeType(ImageUploadCommand command) {
        String mimeType = command.getMimeType();
        if(mimeType==null || !DEFAULT_ALLOWED_MIME_TYPES.contains(mimeType.toLowerCase(Locale.ROOT))){
            throw new BizException(ImageAssetErrorCode.IMAGE_MIME_INVALID);
//!DEFAULT_ALLOWED_MIME_TYPES.contains(mimeType.toLowerCase(Locale.ROOT))是一个条件表达式，用于检查MIME类型是否在允许的MIME类型列表中。它首先将MIME类型转换为小写字母（使用toLowerCase(Locale.ROOT)），然后检查该MIME类型是否存在于DEFAULT_ALLOWED_MIME_TYPES集合中。如果MIME类型不在允许的列表中，则条件表达式返回true，表示MIME类型无效，进而抛出一个BizException异常，错误代码为IMAGE_MIME_INVALID。
        }
    }

    // 校验图片是否可解析。
    public void validateImageReadable(ImageUploadCommand command) {

        InputStream  inputStream = command.getInputStream();
        BufferedInputStream bufferedInputStream = inputStream instanceof BufferedInputStream //检查输入流是否已经是一个BufferedInputStream实例。
        ? (BufferedInputStream) inputStream //如果输入流已经是一个BufferedInputStream实例，则直接使用它。
        : new BufferedInputStream(inputStream);//确保输入流是一个BufferedInputStream，以便支持mark和reset方法。
        command.setInputStream(bufferedInputStream);//将包装后的BufferedInputStream设置回命令对象，以便后续的校验方法使用。
        try {
            BufferedImage image = ImageIO.read(bufferedInputStream);//使用ImageIO.read方法尝试从输入流中读取图像数据，并将其转换为BufferedImage对象。
            if (image == null||image.getWidth() <= 0 || image.getHeight() <= 0) {//如果读取图像失败（即image为null），则表示输入流中的数据不是有效的图像格式，或者图像的宽度或高度为0，则抛出一个BizException异常，错误代码为IMAGE_FORMAT_UNSUPPORTED。
                throw new BizException(ImageAssetErrorCode.IMAGE_DECODE_FAILED);
            }
        } catch (IOException e) {
            throw new BizException(ImageAssetErrorCode.IMAGE_DECODE_FAILED);
        }finally {
            try {
                bufferedInputStream.reset();//重置输入流，以便后续的校验方法可以重新读取输入流中的数据。
            } catch (IOException e) {
                throw new BizException(ImageAssetErrorCode.IMAGE_DECODE_FAILED);
            }
        }
    }
}
