package model;

public class Staff {
    private int idStaff;
    private int accountId;
    private String position;
    private double salary;

    public Staff() {
    }

    public Staff(int idStaff, int accountId, String position, double salary) {
        this.idStaff = idStaff;
        this.accountId = accountId;
        this.position = position;
        this.salary = salary;
    }

    // Getters and Setters
}
