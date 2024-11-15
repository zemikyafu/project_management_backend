package org.project_management.application.services;

public interface AuthService {
    public String generateHash(String password);
    public boolean verifyPassword(String password, String hash);
}
