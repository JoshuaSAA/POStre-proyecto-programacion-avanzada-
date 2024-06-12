package com.example.patata;

public class Table {
    private int id;
    private int tableNumber;
    private String status;

    // Constructores, getters y setters


    public Table(int id, int tableNumber, String status) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.status = status;
    }

    public Table() {

    }

    public int getId() {
        return id;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}