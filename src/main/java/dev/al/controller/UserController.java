package dev.al.controller;
import dev.al.constants.Roles;
import dev.al.model.User;
import dev.al.service.AdminService;
import dev.al.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AdminService adminService;

    public UserController(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    // Mapping e cila therret metoden login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        boolean valid = userService.validateLogin(username, password);
        return valid
                ? ResponseEntity.ok("Hyrje ne llogari e suksesshme!")
                : ResponseEntity.status(401).body("Ndodhi nje gabim. Provojeni perseri.");
    }

    // Mapping e cila therret metoden signup
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity.ok("Krijimi i llogarise i suksesshem. Mire se erdhe: " + user.getRole());
    }

    // Mapping e dila therret metoden signupDoctor
    @PostMapping("/signup/doctor")
    public ResponseEntity<String> signupDoctor(@RequestBody User user) {
        if (user.getEmail() == null || !user.getEmail().endsWith("@hospitalname.com")) {
            return ResponseEntity.badRequest().body("Email duhet te jete email stafi (p.sh., user@hospitalname.com)");
        }
        if (!Roles.DOCTOR.equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.badRequest().body("Roli per te aksesuar kete faqe duhet te jete DOCTOR.");
        }
        userService.registerUser(user);
        return ResponseEntity.ok("Llogaria e doktorit u ruajt me sukses.");
    }

    //Mapping e cila therret metoden signupAdmin
    @PostMapping("/signup/admin")
    public ResponseEntity<String> signupAdmin(@RequestBody User user) {
        if (!Roles.ADMIN.equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.badRequest().body("Role must be ADMIN for this endpoint.");
        }
        adminService.createUser(user);   // creates admin via AdminService
        return ResponseEntity.ok("Admin created successfully.");
    }
//Mapping e cila therret metodet getAllUsers
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }
//Mapping e cila therret metoden getUserById
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
//Mapping e cila therret metoden updateUser
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.findById(id)
                .map(existing -> {
                    user.setId(id);
                    User updated = userService.save(user);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }
//Mapping e cila therret metoden deleteUser
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.delete(id);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}