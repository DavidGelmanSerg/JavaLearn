package ru.gelman.output.printers;

import ru.gelman.exceptions.WritingFileFailureException;
import ru.gelman.exceptions.expression.EmptyExpressionException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FilePrinter implements OutputPrinter {
    private final File file;

    public FilePrinter(File file) {
        this.file = file;
    }

    public void print(String value) {
        if (value.isEmpty()) {
            throw new EmptyExpressionException("Попытка записать в файл пустое значение");
        }
        try (FileWriter writer = new FileWriter(file)) {
            if (!file.exists() && file.createNewFile()) {
                writer.write(value);
            } else {
                writer.write(value);
            }
        } catch (IOException e) {
            var ex = new WritingFileFailureException("Не удалось записать данные в файл");
            ex.initCause(e);
            throw ex;
        }
    }
}
