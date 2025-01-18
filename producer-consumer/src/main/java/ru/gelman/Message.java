package ru.gelman;

public record Message(long id) {
    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                '}';
    }
}
