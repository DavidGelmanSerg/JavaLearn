package ru.gelman.entity.message;

import java.util.Comparator;

public class ChatMessageDateTimeComparator implements Comparator<ChatMessage> {
    @Override
    public int compare(ChatMessage o1, ChatMessage o2) {
        return o1.getCreationDateTime().compareTo(o2.getCreationDateTime());
    }
}
