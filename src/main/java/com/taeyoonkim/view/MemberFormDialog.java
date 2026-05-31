package com.taeyoonkim.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Date;
import com.taeyoonkim.model.MemberDTO;

public class MemberFormDialog extends JDialog {
    private JTextField txtName, txtPhone, txtGender, txtBirthDate;
    private JTextArea txtNotes;
    private JButton btnSave, btnCancel;
    private Integer memberId; // null이면 신규 등록, 값이 있으면 수정

    public MemberFormDialog(Frame parent, MemberDTO member) {
        super(parent, member == null ? "회원 등록" : "회원 수정", true);
        setSize(400, 350);
        setLocationRelativeTo(parent);
        initUI();
        if (member != null) {
            setFormData(member);
        }
    }

    private void initUI() {
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        formPanel.add(new JLabel("이름 (필수):"));
        txtName = new JTextField();
        formPanel.add(txtName);
        
        formPanel.add(new JLabel("연락처 (필수):"));
        txtPhone = new JTextField();
        formPanel.add(txtPhone);
        
        formPanel.add(new JLabel("성별:"));
        txtGender = new JTextField();
        formPanel.add(txtGender);
        
        formPanel.add(new JLabel("생년월일 (YYYY-MM-DD):"));
        txtBirthDate = new JTextField();
        formPanel.add(txtBirthDate);
        
        formPanel.add(new JLabel("비고:"));
        txtNotes = new JTextArea(3, 20);
        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(txtNotes), BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel();
        btnSave = new JButton("저장");
        btnCancel = new JButton("취소");
        btnCancel.addActionListener(e -> dispose());
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);
    }

    public void setFormData(MemberDTO m) {
        this.memberId = m.getId();
        txtName.setText(m.getName());
        txtPhone.setText(m.getPhone());
        txtGender.setText(m.getGender());
        if(m.getBirthDate() != null) txtBirthDate.setText(m.getBirthDate().toString());
        txtNotes.setText(m.getNotes());
    }

    public MemberDTO getFormData() {
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "이름과 연락처는 필수 입력 사항입니다.", "경고", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        MemberDTO m = new MemberDTO();
        if (memberId != null) m.setId(memberId);
        m.setName(name);
        m.setPhone(phone);
        m.setGender(txtGender.getText().trim());
        String birth = txtBirthDate.getText().trim();
        if (!birth.isEmpty()) {
            try {
                m.setBirthDate(Date.valueOf(birth));
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "생년월일 형식이 올바르지 않습니다. (YYYY-MM-DD)", "경고", JOptionPane.WARNING_MESSAGE);
                return null;
            }
        }
        m.setNotes(txtNotes.getText().trim());
        return m;
    }

    public void addSaveListener(ActionListener listener) { btnSave.addActionListener(listener); }
}
