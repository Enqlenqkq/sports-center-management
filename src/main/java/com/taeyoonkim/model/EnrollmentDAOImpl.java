package com.taeyoonkim.model;

import com.taeyoonkim.util.DBConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAOImpl implements IEnrollmentDAO {
    @Override
    public boolean insertEnrollment(EnrollmentDTO enrollment) {
        String sql = "INSERT INTO enrollments (member_id, lesson_id, start_date, end_date, payment_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, enrollment.getMemberId());
            pstmt.setInt(2, enrollment.getLessonId());
            pstmt.setDate(3, enrollment.getStartDate());
            pstmt.setDate(4, enrollment.getEndDate());
            pstmt.setDate(5, enrollment.getPaymentDate());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<EnrollmentDTO> getEnrollmentsByMemberId(int memberId) {
        List<EnrollmentDTO> list = new ArrayList<>();
        // 화면에 보여주기 위해 강좌 테이블과 조인하여 lesson_name 추가 조회
        String sql = "SELECT e.*, l.name AS lesson_name FROM enrollments e " +
                     "JOIN lessons l ON e.lesson_id = l.id " +
                     "WHERE e.member_id = ? ORDER BY e.id DESC";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    EnrollmentDTO dto = new EnrollmentDTO();
                    dto.setId(rs.getInt("id"));
                    dto.setMemberId(rs.getInt("member_id"));
                    dto.setLessonId(rs.getInt("lesson_id"));
                    dto.setStartDate(rs.getDate("start_date"));
                    dto.setEndDate(rs.getDate("end_date"));
                    dto.setPaymentDate(rs.getDate("payment_date"));
                    // 조인된 필드 세팅
                    dto.setLessonName(rs.getString("lesson_name"));
                    list.add(dto);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean deleteEnrollment(int id) {
        String sql = "DELETE FROM enrollments WHERE id=?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
