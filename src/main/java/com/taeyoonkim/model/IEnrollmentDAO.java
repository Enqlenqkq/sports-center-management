package com.taeyoonkim.model;

import java.util.List;

public interface IEnrollmentDAO {
    boolean insertEnrollment(EnrollmentDTO enrollment);
    List<EnrollmentDTO> getEnrollmentsByMemberId(int memberId);
    boolean deleteEnrollment(int id);
}
