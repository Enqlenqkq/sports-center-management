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
    
    // UI components for info
    private JLabel lblNameVal, lblBirthVal, lblPhoneVal, lblCreatedAtVal;
    private JTextArea txtNotes;

    private JButton btnEditInfo;
    private JButton btnDeleteMember;
    private JButton btnSaveNotes; // 메모 저장용 버튼
    private JButton btnAddEnrollment;
    private JButton btnCancelEnrollment;
    private JTable enrollmentTable;
    private DefaultTableModel tableModel;

    public MemberDetailDialog(Frame parent, MemberDTO member) {
        super(parent, "회원 상세 정보", true);
        this.member = member;
        setSize(750, 550);
        setLocationRelativeTo(parent);
        initUI();
        displayMemberInfo(member);
    }

    private JLabel createTitleLabel(String text) {
        JLabel lbl = new JLabel("  " + text, SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(new Color(235, 235, 235)); // 옅은 회색
        lbl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return lbl;
    }

    private JLabel createValueLabel() {
        JLabel lbl = new JLabel();
        lbl.setOpaque(true);
        lbl.setBackground(Color.WHITE);
        lbl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(0, 10, 0, 0)
        ));
        return lbl;
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        
        // 상단 컨테이너 (좌: 회원정보 표, 우: 비고/저장)
        JPanel topContainer = new JPanel(new GridLayout(1, 2, 15, 0));
        topContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));

        // 좌측: 회원 정보 (표 형태)
        JPanel infoPanel = new JPanel(new GridLayout(4, 2));
        
        lblNameVal = createValueLabel();
        lblBirthVal = createValueLabel();
        lblPhoneVal = createValueLabel();
        lblCreatedAtVal = createValueLabel();
        
        infoPanel.add(createTitleLabel("이름")); infoPanel.add(lblNameVal);
        infoPanel.add(createTitleLabel("생년월일")); infoPanel.add(lblBirthVal);
        infoPanel.add(createTitleLabel("핸드폰번호")); infoPanel.add(lblPhoneVal);
        infoPanel.add(createTitleLabel("가입일")); infoPanel.add(lblCreatedAtVal);

        // 우측: 메모 및 저장 버튼
        JPanel notesPanel = new JPanel(new BorderLayout(5, 5));
        
        JPanel notesTitlePanel = new JPanel(new BorderLayout());
        JLabel lblNotes = new JLabel("  메모  ", SwingConstants.CENTER);
        lblNotes.setOpaque(true);
        lblNotes.setBackground(new Color(235, 235, 235));
        lblNotes.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        notesTitlePanel.add(lblNotes, BorderLayout.WEST);
        
        txtNotes = new JTextArea(4, 20);
        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);
        txtNotes.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        notesTitlePanel.add(new JScrollPane(txtNotes), BorderLayout.CENTER);
        
        JPanel notesBtnPanel = new JPanel(new BorderLayout());
        btnSaveNotes = new JButton("저장");
        btnSaveNotes.setPreferredSize(new Dimension(60, 0));
        notesBtnPanel.add(btnSaveNotes, BorderLayout.CENTER);
        notesBtnPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        notesTitlePanel.add(notesBtnPanel, BorderLayout.EAST);
        
        notesPanel.add(notesTitlePanel, BorderLayout.CENTER);

        topContainer.add(infoPanel);
        topContainer.add(notesPanel);

        // 상단 버튼 패널 (정보수정 / 회원삭제)
        JPanel topBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnEditInfo = new JButton("정보 수정");
        btnDeleteMember = new JButton("회원 삭제");
        topBtnPanel.add(btnEditInfo);
        topBtnPanel.add(btnDeleteMember);

        JPanel northWrapper = new JPanel(new BorderLayout());
        northWrapper.add(topContainer, BorderLayout.CENTER);
        northWrapper.add(topBtnPanel, BorderLayout.SOUTH);
        
        add(northWrapper, BorderLayout.NORTH);
        
        // 중앙: 수강 내역
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 15, 10, 15),
            BorderFactory.createTitledBorder("수강 내역")
        ));
        
        String[] columns = {"수강 ID", "강좌명", "시작일", "만료일", "결제일"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        enrollmentTable = new JTable(tableModel);
        
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tableWrapper.add(new JScrollPane(enrollmentTable), BorderLayout.CENTER);
        centerPanel.add(tableWrapper, BorderLayout.CENTER);
        
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
        
        // 성별 색상 표시 (여: 빨강, 남: 파랑)
        if (m.getGender() != null && "여".equals(m.getGender())) {
             lblNameVal.setText("<html>" + m.getName() + " [<font color='red'>" + m.getGender() + "</font>]</html>");
        } else if (m.getGender() != null && "남".equals(m.getGender())) {
             lblNameVal.setText("<html>" + m.getName() + " [<font color='blue'>" + m.getGender() + "</font>]</html>");
        } else {
             lblNameVal.setText(m.getName());
        }
        
        // 생년월일 "YYYY년 MM월 DD일" 포맷
        String birthStr = "";
        if (m.getBirthDate() != null) {
            String b = m.getBirthDate().toString();
            if (b.length() == 10) {
                birthStr = b.substring(0, 4) + "년 " + b.substring(5, 7) + "월 " + b.substring(8, 10) + "일";
            } else {
                birthStr = b;
            }
        }
        lblBirthVal.setText(birthStr);
        lblPhoneVal.setText(m.getPhone());
        lblCreatedAtVal.setText(m.getCreatedAt() != null ? m.getCreatedAt().toString() : "");
        txtNotes.setText(m.getNotes() == null ? "" : m.getNotes());
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
    
    public String getNotesText() { return txtNotes.getText(); }

    public void addEditInfoListener(ActionListener listener) { btnEditInfo.addActionListener(listener); }
    public void addDeleteMemberListener(ActionListener listener) { btnDeleteMember.addActionListener(listener); }
    public void addSaveNotesListener(ActionListener listener) { btnSaveNotes.addActionListener(listener); }
    public void addAddEnrollmentListener(ActionListener listener) { btnAddEnrollment.addActionListener(listener); }
    public void addCancelEnrollmentListener(ActionListener listener) { btnCancelEnrollment.addActionListener(listener); }
}
