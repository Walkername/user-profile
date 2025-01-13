package ru.walkername.user_profile.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.walkername.user_profile.dto.RatingsResponse;
import ru.walkername.user_profile.dto.UserDetails;
import ru.walkername.user_profile.models.Rating;
import ru.walkername.user_profile.models.User;
import ru.walkername.user_profile.repositories.UsersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UsersService {

    private final UsersRepository usersRepository;

    private final String RATING_SERVICE_API;

    @Autowired
    public UsersService(
            UsersRepository usersRepository,
            @Value("${rating.service.url}") String RATING_SERVICE_API) {
        this.usersRepository = usersRepository;
        this.RATING_SERVICE_API = RATING_SERVICE_API;
    }

    public User findOne(int id) {
        Optional<User> user = usersRepository.findById(id);
        return user.orElse(null);
    }

    public List<User> getAll() {
        return usersRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    public List<UserDetails> getUsersByMovie(int id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = RATING_SERVICE_API + "/ratings/movie/" + id;

        RatingsResponse ratingsResponse = restTemplate.getForObject(url, RatingsResponse.class);
        List<Rating> ratings = Objects.requireNonNull(ratingsResponse).getRatings();
        List<Integer> userIds = new ArrayList<>();
        if (ratings != null) {
            for (Rating rating : ratings) {
                userIds.add(rating.getUserId());
            }
        }

        List<UserDetails> usersByMovie = new ArrayList<>();
        List<User> users = usersRepository.findAllById(userIds);
        // TODO: improve algorithm, because this will be very slow with big amount of data
        if (ratings != null) {
            for (Rating rating : ratings) {
                for (User user : users) {
                    if (rating.getUserId() == user.getId()) {
                        UserDetails userDetails = new UserDetails(user, rating);
                        usersByMovie.add(userDetails);
                    }
                }
            }
        }

        return usersByMovie;
    }

    @Transactional
    public void save(User user) {
        usersRepository.save(user);
    }

    @Transactional
    public void delete(int id) {
        usersRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, User user) {
        user.setId(id);
        usersRepository.save(user);
    }
}
