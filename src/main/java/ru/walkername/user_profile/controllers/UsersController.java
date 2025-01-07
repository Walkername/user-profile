package ru.walkername.user_profile.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.walkername.user_profile.dto.UserDTO;
import ru.walkername.user_profile.dto.UserDetails;
import ru.walkername.user_profile.models.User;
import ru.walkername.user_profile.services.UsersService;
import ru.walkername.user_profile.util.UserErrorResponse;
import ru.walkername.user_profile.util.UserNotCreatedException;
import ru.walkername.user_profile.util.UserValidator;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UsersController {

    private final UsersService usersService;
    private final UserValidator userValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public UsersController(UsersService usersService, UserValidator userValidator, ModelMapper modelMapper) {
        this.usersService = usersService;
        this.userValidator = userValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public User getUser(
            @PathVariable("id") int id
    ) {
        return usersService.findOne(id);
    }

    @GetMapping()
    public List<User> index() {
        return usersService.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(
            @RequestBody @Valid UserDTO userDTO,
            BindingResult bindingResult
    ) {
        User user = validateUser(userDTO, bindingResult);
        usersService.save(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<HttpStatus> update(
            @PathVariable("id") int id,
            @RequestBody @Valid UserDTO userDTO,
            BindingResult bindingResult
    ) {
        User user = validateUser(userDTO, bindingResult);
        usersService.update(id, user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(
            @PathVariable("id") int id
    ) {
        usersService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/movie/{id}")
    public List<UserDetails> getUsersByMovie(
            @PathVariable("id") int id
    ) {
        return usersService.getUsersByMovie(id);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotCreatedException ex) {
        UserErrorResponse response = new UserErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private User validateUser(UserDTO userDTO, BindingResult bindingResult) {
        User user = convertToUser(userDTO);
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }

            throw new UserNotCreatedException(errorMsg.toString());
        }

        return user;
    }

    private UserDTO convertToUserDTO(User user) {
        if (user == null) {
            return null;
        }
        return modelMapper.map(user, UserDTO.class);
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
