package com.taeyoonkim.model;

public class LessonDTO {
    private int id;
    private String name;
    private String dayOfWeek;
    private String time;
    private String instructorName;
    private int capacity;
    private int price;

    public LessonDTO() {
    }

    public LessonDTO(int id, String name, String dayOfWeek, String time, String instructorName, int capacity, int price) {
        this.id = id;
        this.name = name;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this.instructorName = instructorName;
        this.capacity = capacity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
