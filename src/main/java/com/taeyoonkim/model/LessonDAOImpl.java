package com.taeyoonkim.model;

import com.taeyoonkim.util.DBConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LessonDAOImpl implements ILessonDAO {
    @Override
    public boolean insertLesson(LessonDTO lesson) {
        String sql = "INSERT INTO lessons (name, day_of_week, time, instructor_name, capacity, price) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, lesson.getName());
            pstmt.setString(2, lesson.getDayOfWeek());
            pstmt.setString(3, lesson.getTime());
            pstmt.setString(4, lesson.getInstructorName());
            pstmt.setInt(5, lesson.getCapacity());
            pstmt.setInt(6, lesson.getPrice());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<LessonDTO> getAllLessons() {
        List<LessonDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM lessons ORDER BY id ASC";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                LessonDTO lesson = new LessonDTO();
                lesson.setId(rs.getInt("id"));
                lesson.setName(rs.getString("name"));
                lesson.setDayOfWeek(rs.getString("day_of_week"));
                lesson.setTime(rs.getString("time"));
                lesson.setInstructorName(rs.getString("instructor_name"));
                lesson.setCapacity(rs.getInt("capacity"));
                lesson.setPrice(rs.getInt("price"));
                list.add(lesson);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean updateLesson(LessonDTO lesson) {
        String sql = "UPDATE lessons SET name=?, day_of_week=?, time=?, instructor_name=?, capacity=?, price=? WHERE id=?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, lesson.getName());
            pstmt.setString(2, lesson.getDayOfWeek());
            pstmt.setString(3, lesson.getTime());
            pstmt.setString(4, lesson.getInstructorName());
            pstmt.setInt(5, lesson.getCapacity());
            pstmt.setInt(6, lesson.getPrice());
            pstmt.setInt(7, lesson.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteLesson(int id) {
        String sql = "DELETE FROM lessons WHERE id=?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // ON DELETE RESTRICT 제약 조건에 의해 삭제 거부 시 처리
            System.err.println("SQL 오류: " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
