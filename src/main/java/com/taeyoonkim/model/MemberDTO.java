package com.taeyoonkim.model;

import java.sql.Date;
import java.sql.Timestamp;

public class MemberDTO {
    private int id;
    private String name;
    private String phone;
    private String gender;
    private Date birthDate;
    private Timestamp createdAt;
    private String notes;

    // 기본 생성자
    public MemberDTO() {
    }

    // 전체 필드 생성자
    public MemberDTO(int id, String name, String phone, String gender, Date birthDate, Timestamp createdAt, String notes) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.birthDate = birthDate;
        this.createdAt = createdAt;
        this.notes = notes;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
