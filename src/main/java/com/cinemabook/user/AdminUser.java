package com.cinemabook.user;

/**
 * Admin account — full system access.
 * Demonstrates: Inheritance, Polymorphism (method overriding)
 */
public class AdminUser extends UserAccount {

    public AdminUser(String userId, String name, String email,
                     String password, String phone) {
        super(userId, name, email, password, phone);
    }

    @Override
    public String getRole() { return "ADMIN"; }

    @Override
    public boolean canManageContent() { return true; }

    @Override
    public String getDashboardUrl() { return "/"; }

    @Override
    public String getWelcomeMessage() {
        return "Welcome, Admin " + name + ". You have full system access.";
    }
}
