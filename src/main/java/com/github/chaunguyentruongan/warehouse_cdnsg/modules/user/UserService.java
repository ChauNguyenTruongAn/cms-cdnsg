package com.github.chaunguyentruongan.warehouse_cdnsg.modules.user;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.chaunguyentruongan.warehouse_cdnsg.exception.ResourceExistsException;
import com.github.chaunguyentruongan.warehouse_cdnsg.exception.ResourceNotFoundException;
import com.github.chaunguyentruongan.warehouse_cdnsg.modules.user.dto.UserCreateDTO;
import com.github.chaunguyentruongan.warehouse_cdnsg.modules.user.dto.UserUpdatedDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepo roleRepo;
    private final PermissionRepo permissionRepo;
    private final PasswordEncoder passwordEncoder;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("not found user by email: " + email));
    }

    public User findBySchoolID(String schoolID) {
        return userRepository.findBySchoolID(schoolID)
                .orElseThrow(() -> new ResourceNotFoundException("not found user by schoolID: " + schoolID));
    }

    public User createUser(UserCreateDTO request) {

        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user != null) {
            throw new ResourceExistsException("user was existed");
        }

        Role role = roleRepo.findById(request.getRoleID()).orElse(null);

        if (role != null) {
            throw new ResourceNotFoundException("not found role by id: " + request.getRoleID());
        }

        Set<Permission> permissions = getPermissionFromRequest(request.getPermissionIDs());

        user = User.builder().fullName(request.getFullName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .isActive(true)
                .schoolID(request.getSchoolID())
                .role(role)
                .permissions(permissions)
                .build();

        return userRepository.save(user);
    }

    private Set<Permission> getPermissionFromRequest(List<Long> permissions) {

        return permissions.stream()
                .map(permissionID -> permissionRepo.findById(permissionID).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

    }

    public Role createRole(String name) {
        Role role = roleRepo.findByName(name).orElse(null);

        if (role != null) {
            throw new ResourceExistsException("role was existed: " + name);
        }

        role = new Role();
        role.setName(name);

        return roleRepo.save(role);
    }

    public Permission createPermission(String name) {
        Permission permission = permissionRepo.findByName(name).orElse(null);

        if (permission != null) {
            throw new ResourceExistsException("permission was existed: " + name);
        }

        permission = new Permission();
        permission.setName(name);

        return permissionRepo.save(permission);
    }

    public User updatedUser(UserUpdatedDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("not found user by email: " + request.getEmail()));

        Role role = roleRepo.findById(request.getRoleID()).orElse(null);

        user.setEmail(request.getEmail());
        user.setSchoolID(request.getSchoolID());
        user.setFullName(request.getFullName());
        user.setRole(role);
        user.setPermissions(getPermissionFromRequest(request.getPermissionIDs()));
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    public boolean disabledUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("not found user by email: " + email));

        user.setActive(false);

        return true;
    }

    public boolean activeUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("not found user by email: " + email));

        user.setActive(true);

        return true;
    }

    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }

    public User findByEmailOrUsername(String request) {
        User user = userRepository.findByUsername(request).orElse(null);

        if (user == null) {
            user = userRepository.findByEmail(request).orElse(null);
        }

        return user;
    }
}
