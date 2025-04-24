package io.github.borisgermanov.services;

import io.github.borisgermanov.data.UserPermission;
import io.github.borisgermanov.data.UserPermissionRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserPermissionService {

    private final UserPermissionRepository repository;

    public UserPermissionService(UserPermissionRepository repository) {
        this.repository = repository;
    }

    public Optional<UserPermission> get(Long id) {
        return repository.findById(id);
    }

    public UserPermission save(UserPermission entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<UserPermission> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<UserPermission> list(Pageable pageable, Specification<UserPermission> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
