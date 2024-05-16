package dev.alexcoss.universitygenerator.util;

import dev.alexcoss.universitygenerator.util.exception.FileReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

public class FileReader {

    public <T> T fileRead(String filePath, Function<BufferedReader, T> function) {
        Path path = Path.of(filePath);
        Path fileName = path.getFileName();

        boolean fileNotExists = Files.notExists(path);
        if (fileNotExists) {
            throw new FileReadException("File not found " + fileName);
        }

        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            return function.apply(bufferedReader);
        } catch (IOException e) {
            throw new FileReadException("Unable to read file " + fileName, e);
        }
    }
}
