package com.taeyoonkim.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import com.taeyoonkim.model.MemberDTO;
import com.taeyoonkim.model.EnrollmentDTO;

public class MemberDetailDialog extends JDialog {
    private MemberDTO member;
    private JLabel lblInfo;
    private JButton btnEditInfo;
    private JButton btnDeleteMember;
    private JButton btnAddEnrollment;
    private JButton btnCancelEnrollment;
    private JTable enrollmentTable;
    private DefaultTableModel tableModel;

    public MemberDetailDialog(Frame parent, MemberDTO member) {
        super(parent, "회원 상세 정보", true);
        this.member = member;
        setSize(600, 500);
        setLocationRelativeTo(parent);
        initUI();
        displayMemberInfo(member);
    }

    private void initUI() {
        setLayout(new BorderLayout());
        
        // 상단: 회원 기본 정보 및 정보수정/탈퇴 버튼
        JPanel topPanel = new JPanel(new BorderLayout());
        lblInfo = new JLabel();
        lblInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(lblInfo, BorderLayout.CENTER);
        
        JPanel topBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnEditInfo = new JButton("정보 수정");
        btnDeleteMember = new JButton("회원 삭제");
        topBtnPanel.add(btnEditInfo);
        topBtnPanel.add(btnDeleteMember);
        topPanel.add(topBtnPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        
        // 중앙: 수강 내역
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("수강 내역"));
        
        String[] columns = {"수강 ID", "강좌명", "시작일", "만료일", "결제일"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        enrollmentTable = new JTable(tableModel);
        centerPanel.add(new JScrollPane(enrollmentTable), BorderLayout.CENTER);
        
        JPanel centerBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAddEnrollment = new JButton("수강 등록");
        btnCancelEnrollment = new JButton("수강 취소");
        centerBtnPanel.add(btnAddEnrollment);
        centerBtnPanel.add(btnCancelEnrollment);
        centerPanel.add(centerBtnPanel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
    }

    public void displayMemberInfo(MemberDTO m) {
        this.member = m;
        String info = String.format("<html><b>ID:</b> %d &nbsp; <b>이름:</b> %s &nbsp; <b>연락처:</b> %s<br>" +
                "<b>성별:</b> %s &nbsp; <b>생년월일:</b> %s<br>" +
                "<b>가입일:</b> %s<br><b>비고:</b> %s</html>",
                m.getId(), m.getName(), m.getPhone(), 
                m.getGender() == null ? "" : m.getGender(), 
                m.getBirthDate() == null ? "" : m.getBirthDate().toString(),
                m.getCreatedAt().toString(),
                m.getNotes() == null ? "" : m.getNotes());
        lblInfo.setText(info);
    }

    public void displayEnrollments(List<EnrollmentDTO> enrollments) {
        tableModel.setRowCount(0);
        for (EnrollmentDTO e : enrollments) {
            tableModel.addRow(new Object[]{
                e.getId(), e.getLessonName(), e.getStartDate(), e.getEndDate(), e.getPaymentDate()
            });
        }
    }
    
    public int getSelectedEnrollmentId() {
        int row = enrollmentTable.getSelectedRow();
        if (row >= 0) {
            return (Integer) tableModel.getValueAt(row, 0);
        }
        return -1;
    }
    
    public MemberDTO getMember() { return member; }

    public void addEditInfoListener(ActionListener listener) { btnEditInfo.addActionListener(listener); }
    public void addDeleteMemberListener(ActionListener listener) { btnDeleteMember.addActionListener(listener); }
    public void addAddEnrollmentListener(ActionListener listener) { btnAddEnrollment.addActionListener(listener); }
    public void addCancelEnrollmentListener(ActionListener listener) { btnCancelEnrollment.addActionListener(listener); }
}
