package com.cinemabook.user;

/**
 * Abstract base class for all user account types.
 * Demonstrates: Abstraction, Encapsulation, Information Hiding
 */
public abstract class UserAccount {

    // Encapsulation — protected fields, not public
    protected String userId;
    protected String name;
    protected String email;
    protected String password;
    protected String phone;

    public UserAccount(String userId, String name, String email,
                       String password, String phone) {
        this.userId   = userId;
        this.name     = name;
        this.email    = email;
        this.password = password;
        this.phone    = phone;
    }

    // Abstraction — subclasses must define their role behaviour
    public abstract String getRole();
    public abstract boolean canManageContent();
    public abstract String getDashboardUrl();
    public abstract String getWelcomeMessage();

    // Information Hiding — password never returned raw
    public boolean verifyPassword(String input) {
        return this.password != null && this.password.equals(input);
    }

    // Common concrete method shared by all accounts
    public String getDisplayName() {
        return name + " (" + getRole() + ")";
    }

    // Getters
    public String getUserId()  { return userId; }
    public String getName()    { return name; }
    public String getEmail()   { return email; }
    public String getPhone()   { return phone; }
}
