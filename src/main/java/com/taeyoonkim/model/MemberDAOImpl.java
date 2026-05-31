package com.taeyoonkim.model;

import com.taeyoonkim.util.DBConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAOImpl implements IMemberDAO {
    @Override
    public boolean insertMember(MemberDTO member) {
        String sql = "INSERT INTO members (name, phone, gender, birth_date, notes) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getPhone());
            pstmt.setString(3, member.getGender());
            pstmt.setDate(4, member.getBirthDate());
            pstmt.setString(5, member.getNotes());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<MemberDTO> getAllMembers() {
        List<MemberDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM members ORDER BY id DESC";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(extractMemberFromResultSet(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<MemberDTO> searchMembers(String keyword) {
        List<MemberDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE name LIKE ? OR phone LIKE ? ORDER BY id DESC";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractMemberFromResultSet(rs));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean updateMember(MemberDTO member) {
        String sql = "UPDATE members SET name=?, phone=?, gender=?, birth_date=?, notes=? WHERE id=?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getPhone());
            pstmt.setString(3, member.getGender());
            pstmt.setDate(4, member.getBirthDate());
            pstmt.setString(5, member.getNotes());
            pstmt.setInt(6, member.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteMember(int id) {
        String sql = "DELETE FROM members WHERE id=?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private MemberDTO extractMemberFromResultSet(ResultSet rs) throws SQLException {
        MemberDTO member = new MemberDTO();
        member.setId(rs.getInt("id"));
        member.setName(rs.getString("name"));
        member.setPhone(rs.getString("phone"));
        member.setGender(rs.getString("gender"));
        member.setBirthDate(rs.getDate("birth_date"));
        member.setCreatedAt(rs.getTimestamp("created_at"));
        member.setNotes(rs.getString("notes"));
        return member;
    }
}
