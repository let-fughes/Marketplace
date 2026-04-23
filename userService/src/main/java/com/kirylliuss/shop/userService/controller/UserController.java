package com.kirylliuss.shop.userService.controller;

import com.kirylliuss.shop.userService.dto.request.UserRequest;
import com.kirylliuss.shop.userService.dto.response.ProfilePhotoUrlResponse;
import com.kirylliuss.shop.userService.dto.response.UserResponse;
import com.kirylliuss.shop.userService.service.ProfileImageService;
import com.kirylliuss.shop.userService.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/api/users")
@Tag(name = "Users", description = "User's API")
@Validated
public class UserController {

    private final UserService userService;
    private final ProfileImageService profileImageService;

    @GetMapping
    @Operation(
            summary = "Get all users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved users"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User got successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        UserResponse userResponse = userService.getUserById(id);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/profile")
    @Operation(
            summary = "Get user by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User got successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<UserResponse> getProfile(Principal principal){
        String login = principal.getName();
        UserResponse response = userService.getUserData(login);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // ... твои предыдущие методы ...

    @PostMapping(value = "/profile/avatar", consumes = "multipart/form-data")
    @Operation(
            summary = "Upload user avatar",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Avatar uploaded successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid file"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<ProfilePhotoUrlResponse> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            Principal principal) {

        String login = principal.getName();

        String photoUrl = profileImageService.uploadImage(file, login);

        ProfilePhotoUrlResponse response = new ProfilePhotoUrlResponse();
        response.setUrl(photoUrl);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search user by email or phone",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User got successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<UserResponse> findUser(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone) {

        if (email != null) return ResponseEntity.ok(userService.getUserByEmail(email));
        if (phone != null) return ResponseEntity.ok(userService.getUserByPhoneNumber(phone));

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/getProfilePhotoUrl/{login}")
    @Operation(
            summary = "Get user by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User got successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<ProfilePhotoUrlResponse> getProfilePhotoUrl(@PathVariable String login){
        ProfilePhotoUrlResponse response = new ProfilePhotoUrlResponse();
        response.setUrl(profileImageService.getProfilePhoto(login));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    @Operation(
            summary = "Create new user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "409", description = "Email already exists"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse createdUser = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{login}")
    @Operation(
            summary = "Update user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "409", description = "Email already exists"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String login, @Valid @RequestBody UserRequest userRequest) {
        UserResponse updatedUser = userService.updateUser(login, userRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{login}")
    @Operation(
            summary = "Delete user",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        userService.deleteByLogin(login);
        return ResponseEntity.noContent().build();
    }
}
