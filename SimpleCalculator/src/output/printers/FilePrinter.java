package output.printers;

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
            //Я не понимаю, как правильно обрабатывать подобные исключения в случае реализации интерфейсов.
            /*
                 Метод в моем интерфейсе не выбрасывает подобное исключение, но при попытке записи в файл или
                 его создании я должен их обрабатывать. Как Правильно работать с этими исключениями внутри классов?
             */
            ConsolePrinter printer = new ConsolePrinter();
            printer.print("Не удалось выполнить запись в файл");
        }
    }
}
