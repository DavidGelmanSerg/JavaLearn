package output.streams;

public class ConsoleStream implements IStream {
    public void print(double value) {
        System.out.println(value);
    }
}
