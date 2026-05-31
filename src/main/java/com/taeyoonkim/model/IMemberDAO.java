package com.taeyoonkim.model;

import java.util.List;

public interface IMemberDAO {
    boolean insertMember(MemberDTO member);
    List<MemberDTO> getAllMembers();
    List<MemberDTO> searchMembers(String keyword);
    boolean updateMember(MemberDTO member);
    boolean deleteMember(int id);
}
