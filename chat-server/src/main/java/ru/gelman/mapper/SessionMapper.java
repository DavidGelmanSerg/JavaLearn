package ru.gelman.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.gelman.dto.SessionData;
import ru.gelman.entity.ChatSession;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SessionMapper {
    SessionData toSessionDto(ChatSession session);
}
