package ru.walkername.user_profile.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.walkername.user_profile.dto.NewRatingDTO;
import ru.walkername.user_profile.dto.UserDTO;
import ru.walkername.user_profile.dto.UserDetails;
import ru.walkername.user_profile.models.User;
import ru.walkername.user_profile.services.UsersService;
import ru.walkername.user_profile.util.UserErrorResponse;
import ru.walkername.user_profile.util.UserNotCreatedException;
import ru.walkername.user_profile.util.UserValidator;
import ru.walkername.user_profile.util.UserWrongAverageRatingException;

import java.util.List;
import java.util.function.Function;

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
        User user = convertToUser(userDTO);
        userValidator.validate(user, bindingResult);
        validateUser(bindingResult, UserNotCreatedException::new);
        usersService.save(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<HttpStatus> update(
            @PathVariable("id") int id,
            @RequestBody @Valid UserDTO userDTO,
            BindingResult bindingResult
    ) {
        // Getting user from userDTO just to validate it: has the same username or not
        User userToValidate = convertToUser(userDTO);
        userValidator.validate(userToValidate, bindingResult);
        validateUser(bindingResult, UserNotCreatedException::new);

        // Getting an existing user and setting fields from dto for them
        User user = usersService.findOne(id);
        if (user != null) {
            modelMapper.map(userDTO, user);
            usersService.save(user);
        }
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

    @PatchMapping("/update-avg-rating/{id}")
    public ResponseEntity<HttpStatus> updateAvgRating(
            @PathVariable("id") int id,
            @RequestBody @Valid NewRatingDTO ratingDTO,
            BindingResult bindingResult
    ) {
        validateUser(bindingResult, UserWrongAverageRatingException::new);
        usersService.updateAverageRating(id, ratingDTO.getRating(), ratingDTO.isUpdate(), ratingDTO.getOldRating());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/top-user")
    public User getTopUser() {
        return usersService.getTopUser();
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotCreatedException ex) {
        UserErrorResponse response = new UserErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserWrongAverageRatingException ex) {
        UserErrorResponse response = new UserErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private void validateUser(BindingResult bindingResult, Function<String, ? extends RuntimeException> exceptionFunction) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }

            throw exceptionFunction.apply(errorMsg.toString());
        }
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
