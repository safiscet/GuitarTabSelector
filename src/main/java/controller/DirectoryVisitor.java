package controller;

import interfaces.GuitarTabCollector;
import model.GuitarTab;
import model.GuitarTabConfiguration;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by Stefan Fritsch on 27.05.2017.
 */
public class DirectoryVisitor implements FileVisitor<Path> {

    private GuitarTabCollector guitarTabCollector;
    private Path root;
    private Collection<Path> excludedPaths;
    private GuitarTab currentTab;

    DirectoryVisitor(GuitarTabConfiguration config, GuitarTabCollector guitarTabCollector) {
        this.guitarTabCollector = guitarTabCollector;
        root = Paths.get(config.getRootPath());
        excludedPaths = config.getExcludedPaths().stream()
                .map(p -> Paths.get(p))
                .collect(Collectors.toSet());
    }

    void startVisiting() {
        try {
            Files.walkFileTree(root, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        for(Path excluded : excludedPaths) {
            if(Files.isSameFile(dir, excluded)) {
                return FileVisitResult.SKIP_SUBTREE;
            }
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        String name = com.google.common.io.Files.getNameWithoutExtension(file.toString());
        String format = com.google.common.io.Files.getFileExtension(file.toString());
        String path = file.getParent().toString();
        guitarTabCollector.notifyNewGuitarTab(name, path, format);

        return FileVisitResult.CONTINUE;
    }



    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        System.out.println("Failed for file: " + file + " with exception " + exc);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        guitarTabCollector.notifyNewDirectory();
        return FileVisitResult.CONTINUE;
    }
}
