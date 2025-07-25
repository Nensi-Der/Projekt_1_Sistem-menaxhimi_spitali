package dev.al.controller;
import dev.al.model.User;
import dev.al.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
//Mapping per login ne sistem
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        boolean valid = userService.validateLogin(username, password);
        return valid
                ? ResponseEntity.ok("Hyrje ne llogari e suksesshme!")
                : ResponseEntity.status(401).body("Ndodhi nje gabim. Provojeni perseri.");
    }
//Mapping per krijim llogarie
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity.ok("Krijimi i llogaris i suksesshem. Mireseerdhe:  " + user.getRole());
    }
//Mapping per krijimin e nje llogarie doktori
    @PostMapping("/signup/doctor")
    public ResponseEntity<String> signupDoctor(@RequestBody User user) {
        if (user.getEmail() == null || !user.getEmail().endsWith("@hospitalname.com")) {
            return ResponseEntity.badRequest().body("Email duhet te jete email stafi (p.sh., user@hospitalname.com)");
        }

        if (!"DOCTOR".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.badRequest().body("Role must be DOCTOR for this endpoint.");
        }

        userService.registerUser(user);
        return ResponseEntity.ok("Doctor signed up successfully.");
    }
//Mapping per tregimin e listes se perdoruesve te programit
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }
//Mapping per te kerkuar nje perdorues me ane te id
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
//Mapping per te bere update infomacionet e perdoruesit
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.findById(id)
                .map(existingUser -> {
                    user.setId(id);
                    User updated = userService.save(user);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }
//Mapping per te fshire nje perdorues nga db e sistemit
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.delete(id);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}