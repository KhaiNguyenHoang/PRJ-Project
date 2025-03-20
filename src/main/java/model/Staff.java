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

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getIdStaff() {
        return idStaff;
    }

    public void setIdStaff(int idStaff) {
        this.idStaff = idStaff;
    }
}
