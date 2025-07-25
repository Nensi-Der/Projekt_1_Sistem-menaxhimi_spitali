package dev.al.service;
import dev.al.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    Optional<User> findById(Long id);
    User save(User user);
    boolean delete(Long id);
    Optional<User> login(String username, String password);
    boolean validateLogin(String username, String password);
    void registerUser(User user);

}