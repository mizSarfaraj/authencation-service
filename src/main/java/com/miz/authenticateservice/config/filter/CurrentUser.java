package com.miz.authenticateservice.config.filter;

import com.miz.authenticateservice.entity.Users;
import com.miz.authenticateservice.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CurrentUser {
    private final UsersRepository usersRepository;

    private final ThreadLocal<String> email = ThreadLocal.withInitial(() -> "");
    private final ThreadLocal<Long> userId = ThreadLocal.withInitial(() -> null);
    private final ThreadLocal<String> accessToken = ThreadLocal.withInitial(() -> "");

    public CurrentUser(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setUserId(Long userId) {
        this.userId.set(userId);
    }

    public void setAccessToken(String token) {
        this.accessToken.set(token);
    }

    public String getEmail() {
        return this.email.get();
    }

    public Long getUserId() {
        if (Objects.isNull(this.userId.get())) {
            Users users = usersRepository.findByEmail(this.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            this.setUserId(users.getUserId());
            return this.userId.get();
        }
        return this.userId.get();
    }

    public String getAccessToken() {
        return this.accessToken.get();
    }
}
