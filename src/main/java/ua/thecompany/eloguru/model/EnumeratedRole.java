package ua.thecompany.eloguru.model;

public enum EnumeratedRole {
    ADMIN("admin"),
    TEACHER("teacher"),
    STUDENT("student");

    final private String role;

    EnumeratedRole(String s) {
        this.role = s;
    }

    @Override
    public String toString() {
        return role;
    }
}
