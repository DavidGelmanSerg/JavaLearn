package output.streams;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileStream implements IStream {
    private String filePath;

    public FileStream(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void print(double value) {
        try (PrintWriter p = new PrintWriter(filePath)) {
            p.println(value);
        } catch (FileNotFoundException e) {
            System.out.println("Файл, который вы указали, не существует!");
            System.exit(0);
        }
    }
}
