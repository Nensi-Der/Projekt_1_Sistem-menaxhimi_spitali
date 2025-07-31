package dev.al.service.impl;
import dev.al.constants.Roles;
import dev.al.model.User;
import dev.al.repository.UserRepository;
import dev.al.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    //Metode per nxjerrjen ne ekran te listes se perdoruesve
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    //Metode qe gjen nje perdorues nga ID
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    //Metode qe krijon nje llogari perdoruesi
    public User createUser(User user) {
               return userRepository.save(user);
    }

    @Override
    //Metode qe fshin llogarine e nje perdoruesi
    public boolean deleteUser(Long id) {
        int deletedCount = userRepository.deleteByIdCustom(id);
        return deletedCount > 0;
    }

    @Override
    //Metode qe vendos rolin e nje perdoruesi
    public void assignRole(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Perdoruesi nuk u gjet"));
        if (!role.equals(Roles.ADMIN) && !role.equals(Roles.DOCTOR) && !role.equals(Roles.PATIENT)) {
            throw new IllegalArgumentException("Rol i pavlefshem!");
        }
        user.setRole(role);
        userRepository.save(user);
    }
}