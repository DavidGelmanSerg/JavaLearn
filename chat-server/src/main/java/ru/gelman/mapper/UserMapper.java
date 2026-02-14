package ru.gelman.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.gelman.dto.UserData;
import ru.gelman.entity.ChatUser;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserData toUserDto(ChatUser user);
}
