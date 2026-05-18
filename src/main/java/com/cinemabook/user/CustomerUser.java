package com.cinemabook.user;

/**
 * Customer account — booking and viewing only.
 * Demonstrates: Inheritance, Polymorphism (method overriding)
 */
public class CustomerUser extends UserAccount {

    public CustomerUser(String userId, String name, String email,
                        String password, String phone) {
        super(userId, name, email, password, phone);
    }

    @Override
    public String getRole() { return "CUSTOMER"; }

    @Override
    public boolean canManageContent() { return false; }

    @Override
    public String getDashboardUrl() { return "/movies"; }

    @Override
    public String getWelcomeMessage() {
        return "Welcome back, " + name + ". Ready to book your next movie?";
    }
}
