package dev.al.service.impl;
import dev.al.model.User;
import dev.al.repository.UserRepository;
import dev.al.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override//Metode qe nxjerr listen e perdoruesve te programit
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override//Metode e cila gjen nje perdorues nga id
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override//Metode ndihmese per ruajtjen e informacionit te nje perdoruesi te ri
    public User save(User user) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(encoder.encode(user.getPassword())); // hash password
        }
        return userRepository.save(user);
    }

    @Override//Metode e cila fshin nje perdorues nga sistemi
    public boolean delete(Long id) {
        int deletedCount = userRepository.deleteByIdCustom(id);
        return deletedCount > 0;
    }

    @Override//Metode e cila kontrollon nese informacioni per futjen ne llogari eshte i sakte
    public boolean validateLogin(String username, String password) {
        return userRepository.findByUsername(username)
                .map(user -> encoder.matches(password, user.getPassword()))
                .orElse(false);
    }


    @Override//Metode e cila regjistron nje perdorues te ri
    public void registerUser(User user) {
        save(user);
    }

    @Override//Metode per hyrjen ne llogari me ane te emrit dhe passwordit
    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> encoder.matches(password, user.getPassword()));
    }

}