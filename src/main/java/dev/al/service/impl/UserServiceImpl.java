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

    @Override
    //Metode e cila nxjerr listen e gjithe perdoruesve te programit
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    //Metode e cila gjen nje perdorues te programit nga ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    //Metode e cila ruan nje llogari perdoruesi
    public User save(User user) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(encoder.encode(user.getPassword())); // hash password
        }
        return userRepository.save(user);
    }

    @Override
    //Metode e cila fshin nje llogari perdoruesi
    public boolean delete(Long id) {
        int deletedCount = userRepository.deleteByIdCustom(id);
        return deletedCount > 0;
    }

    @Override
    //Metode e cila verifikon informacionin para nje log-in
    public boolean validateLogin(String username, String password) {
        return userRepository.findByUsername(username)
                .map(user -> encoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    @Override
    //Metode e cila ruan nje llogari te re me ane te metodes ndihmese save()
    public void registerUser(User user) {
        save(user);
    }

    @Override
    //Metode e cila ben log-in te perdoruesit
    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> encoder.matches(password, user.getPassword()));
    }
}