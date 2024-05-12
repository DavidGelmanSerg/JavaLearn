package output.printers;

import output.printers.exceptions.WritingFileFailureException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FilePrinter implements OutputPrinter {
    private final String filePath;

    public FilePrinter(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
    public void print(String value) {
        File file = new File(filePath);
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
