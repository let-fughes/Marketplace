package com.kirylliuss.shop.userService.service;

import com.kirylliuss.shop.userService.dto.request.UserRequest;
import com.kirylliuss.shop.userService.dto.response.UserResponse;
import com.kirylliuss.shop.userService.exceptions.UserNotFoundException;
import com.kirylliuss.shop.userService.mapper.CardMapper;
import com.kirylliuss.shop.userService.mapper.UserMapper;
import com.kirylliuss.shop.userService.model.User;
import com.kirylliuss.shop.userService.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Validated
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CardMapper cardMapper;

    @Cacheable(value = "usersList", key = "'allUsers'")
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toUserResponseWithCards)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "userByPhone", key = "#phoneNumber")
    @Transactional(readOnly = true)
    public UserResponse getUserByPhoneNumber(String phoneNumber) throws UserNotFoundException{
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UserNotFoundException("Phone: " + phoneNumber));
        return toUserResponseWithCards(user);
    }

    public UserResponse getUserData(String login){
        User user = userRepository.findByLogin(login).orElseThrow(() -> new RuntimeException("User not found!"));
        return userMapper.toUserResponse(user);
    }

    @Cacheable(value = "usersById", key = "#id")
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return toUserResponseWithCards(user);
    }

    @Cacheable(value = "usersByEmail", key = "#email")
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new UserNotFoundException(email));
        return toUserResponseWithCards(user);
    }

    @Caching(evict = {@CacheEvict(value = "usersList", key = "'allUsers'")})
    @Transactional
    public UserResponse createUser(@Valid UserRequest userRequest) throws ValidationException {
        User user = userMapper.toUser(userRequest);
        User savedUser = userRepository.save(user);
        return toUserResponseWithCards(savedUser);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "users", allEntries = true),
                    @CacheEvict(value = "usersList", key = "'allUsers'"),
            })
    @Transactional
    public UserResponse updateUser(String login, @Valid UserRequest userRequest)
            throws ValidationException {
        User existingUser =
                userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException(login));

        userMapper.updateUserFromUserRequest(existingUser, userRequest);
        User updatedUser = userRepository.save(existingUser);

        return toUserResponseWithCards(updatedUser);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "usersList", key = "'allUsers'"),
                    @CacheEvict(value = "usersById", key = "#id"),
                    @CacheEvict(value = "usersByEmail", allEntries = true)
            })
    @Transactional
    public void deleteByLogin(String login) {
        if (!userRepository.existsByLogin(login)) {
            throw new UserNotFoundException(login);
        }
        userRepository.deleteByLogin(login);
    }

    private UserResponse toUserResponseWithCards(User user) {
        return userMapper.toUserResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByIdIn(List<Long> ids) throws UserNotFoundException {
        List<User> users = userRepository.findByIdIn(ids);
        return users.stream().map(this::toUserResponseWithCards).collect(Collectors.toList());
    }
}
