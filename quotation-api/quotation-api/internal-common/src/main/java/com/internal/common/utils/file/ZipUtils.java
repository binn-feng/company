package com.internal.common.utils.file;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * zip工具类
 *
 * @author every
 */
public class ZipUtils {

    public static byte[] zipFolder(String folderPath) throws IOException {
        Path zipFilePath = Files.createTempFile("tempZip", ".zip");
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFilePath))) {
            Path folder = Paths.get(folderPath);
            Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (attrs.isRegularFile()) {
                        Path targetFile = folder.relativize(file);
                        zos.putNextEntry(new ZipEntry(targetFile.toString()));

                        try (InputStream fis = Files.newInputStream(file)) {
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = fis.read(buffer)) >= 0) {
                                zos.write(buffer, 0, length);
                            }
                        }
                        zos.closeEntry();
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path targetDir = folder.relativize(dir);
                    zos.putNextEntry(new ZipEntry(targetDir.toString() + "/"));
                    zos.closeEntry();
                    return FileVisitResult.CONTINUE;
                }
            });
        }

        // Read the ZIP file content into a byte array
        byte[] zipBytes = Files.readAllBytes(zipFilePath);

        // Clean up the temporary ZIP file
        Files.delete(zipFilePath);

        return zipBytes;
    }
}