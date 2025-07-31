package dev.al.controller;
import dev.al.model.User;
import dev.al.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
//Mapping qe te con tek faqja e ADMIN
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
//Mapping e cila aktivion metoden getAllUsers
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.findAllUsers();
    }
//Mapping e cila aktivizon metoden getUserById
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return adminService.findUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
//Mapping e cila aktivizon meoden createUser
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return adminService.createUser(user);
    }
//Mapping e cila aktivizon metoden deleteUser
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = adminService.deleteUser(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
//Mapping e cila aktivizon metoden assignRole
    @PutMapping("/users/{id}/role")
    public ResponseEntity<Void> assignRole(@PathVariable Long id, @RequestParam String role) {
        try {
            adminService.assignRole(id, role);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}