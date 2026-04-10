package com.kirylliuss.shop.userService.mapper;

import com.kirylliuss.shop.userService.dto.request.UserRequest;
import com.kirylliuss.shop.userService.dto.response.UserResponse;
import com.kirylliuss.shop.userService.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {CardMapper.class})
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cards", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toUser(UserRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cards", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateUserFromUserRequest(@MappingTarget User user, UserRequest request);
}
