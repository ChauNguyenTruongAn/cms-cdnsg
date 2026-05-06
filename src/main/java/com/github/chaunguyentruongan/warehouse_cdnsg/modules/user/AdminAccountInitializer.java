package com.github.chaunguyentruongan.warehouse_cdnsg.modules.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.github.chaunguyentruongan.warehouse_cdnsg.modules.user.dto.UserCreateDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminAccountInitializer implements ApplicationRunner {

    private static final String ADMIN_ROLE_NAME = "ADMIN";

    private final UserRepository userRepository;
    private final RoleRepo roleRepo;
    private final UserService userService;
    private final PermissionRepo permissionRepo;

    @Value("${app.admin.email:admin@warehouse-cdnsg.local}")
    private String adminEmail;

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.password:admin123}")
    private String adminPassword;

    @Value("${app.admin.full-name:System Administrator}")
    private String adminFullName;

    @Value("${app.admin.school-id:ADMIN}")
    private String adminSchoolId;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            log.info("AdminAccountInitializer: existing users detected, skipping initial admin creation.");
            return;
        }

        Role adminRole = roleRepo.findByName(ADMIN_ROLE_NAME)
                .orElseGet(() -> roleRepo.save(new Role(null, ADMIN_ROLE_NAME)));

        Permission create = permissionRepo.findByName("material_create")
                .orElseGet(() -> permissionRepo.save(new Permission(null, "material_create")));

        Permission delete = permissionRepo.findByName("material_delete")
                .orElseGet(() -> permissionRepo.save(new Permission(null, "material_delete")));

        Set<Permission> permissions = new HashSet<>();
        permissions.add(create);
        permissions.add(delete);

        UserCreateDTO request = new UserCreateDTO(
                adminSchoolId,
                adminFullName,
                adminUsername,
                adminPassword,
                adminEmail,
                adminRole.getId(),
                List.of());

        userService.createUser(request);
        log.info("AdminAccountInitializer: created initial admin account with email '{}'.", adminEmail);
    }
}
