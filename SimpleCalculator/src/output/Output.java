package output;

import output.streams.IStream;

public class Output {
    private IStream outputStream;

    public Output(IStream stream) {
        outputStream = stream;
    }

    public void print(double value) {
        outputStream.print(value);
    }

    public void setOutputStream(IStream stream) {
        outputStream = stream;
    }
}
