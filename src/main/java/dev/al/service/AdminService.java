package dev.al.service;
import dev.al.model.User;
import java.util.List;
import java.util.Optional;

public interface AdminService {
    List<User> findAllUsers();
    Optional<User> findUserById(Long id);
    User createUser(User user);
    boolean deleteUser(Long id);

    void assignRole(Long userId, String role);
}