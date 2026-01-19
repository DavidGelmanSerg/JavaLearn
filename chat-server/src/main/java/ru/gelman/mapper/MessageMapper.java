package ru.gelman.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.gelman.dto.MessageData;
import ru.gelman.entity.message.ChatMessage;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageMapper {
    MessageData toMessageDto(ChatMessage message);
}
