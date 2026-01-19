package ru.gelman.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.gelman.entity.ChatUser;
import ru.gelman.repository.UserRepository;
import ru.gelman.service.exception.UserAlreadyExistsException;
import ru.gelman.service.exception.UserNotFoundException;

@Slf4j
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;

    public ChatUser createUser(String name, String password) {
        ChatUser user = ChatUser.createNew(name, password);
        if (repository.has(user)) {
            log.warn("user with name {} already exists", user.getName());
            throw new UserAlreadyExistsException(user);
        }
        log.debug("saving new user: {}", user);
        user = repository.save(user);
        log.debug("successfully saved user: {}", user);
        return user;
    }

    public ChatUser getUser(int id) {
        if (!repository.has(id)) {
            log.warn("user not found. id: {}", id);
            throw new UserNotFoundException(id);
        }
        return repository.getUser(id);
    }
}
