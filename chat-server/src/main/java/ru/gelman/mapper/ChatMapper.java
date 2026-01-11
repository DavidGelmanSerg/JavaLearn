package ru.gelman.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.gelman.dto.ChatData;
import ru.gelman.dto.ChatInfoData;
import ru.gelman.entity.chat.Chat;
import ru.gelman.entity.chat.ChatInfo;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatMapper {
    ChatData toChatDto(Chat chat);

    ChatInfoData toChatInfoDto(ChatInfo chatInfo);

    ChatInfo toChatInfo(ChatInfoData chatInfoData);
}
