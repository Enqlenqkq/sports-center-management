package com.taeyoonkim.model;

import java.util.List;

public interface IEnrollmentDAO {
    boolean insertEnrollment(EnrollmentDTO enrollment);
    List<EnrollmentDTO> getEnrollmentsByMemberId(int memberId);
    boolean deleteEnrollment(int id);
    int getEnrollmentCountByLessonId(int lessonId);
    boolean isAlreadyEnrolled(int memberId, int lessonId);
}
