package org.kuneberg.qst;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FilesCollector extends SimpleFileVisitor<Path> {
    private List<Path> paths = new ArrayList<>();

    public List<Path> getPaths() {
        return paths;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (file.getFileName().toString().endsWith("xml")) {
            paths.add(file);
        }
        return FileVisitResult.CONTINUE;
    }
}
