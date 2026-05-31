package com.taeyoonkim.model;

import java.sql.Date;

public class EnrollmentDTO {
    private int id;
    private int memberId;
    private int lessonId;
    private Date startDate;
    private Date endDate;
    private Date paymentDate;
    
    // JOIN을 통해 화면에 강좌 이름을 쉽게 표출하기 위한 확장 필드
    private String lessonName;

    public EnrollmentDTO() {
    }

    public EnrollmentDTO(int id, int memberId, int lessonId, Date startDate, Date endDate, Date paymentDate) {
        this.id = id;
        this.memberId = memberId;
        this.lessonId = lessonId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.paymentDate = paymentDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }
}
