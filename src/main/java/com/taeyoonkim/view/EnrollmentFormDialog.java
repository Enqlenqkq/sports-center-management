package com.taeyoonkim.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;
import com.taeyoonkim.model.LessonDTO;
import com.taeyoonkim.model.EnrollmentDTO;
import java.time.LocalDate;

public class EnrollmentFormDialog extends JDialog {
    private JComboBox<LessonItem> cbLessons;
    private JTextField txtStartDate, txtEndDate, txtPaymentDate;
    private JButton btnSave, btnCancel;
    private int memberId;

    public EnrollmentFormDialog(JDialog parent, int memberId, List<LessonDTO> lessons) {
        super(parent, "수강 등록", true);
        this.memberId = memberId;
        setSize(350, 250);
        setLocationRelativeTo(parent);
        initUI(lessons);
    }

    private void initUI(List<LessonDTO> lessons) {
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        formPanel.add(new JLabel("강좌 선택:"));
        cbLessons = new JComboBox<>();
        for (LessonDTO l : lessons) {
            cbLessons.addItem(new LessonItem(l.getId(), l.getName()));
        }
        formPanel.add(cbLessons);
        
        formPanel.add(new JLabel("시작일(YYYY-MM-DD):"));
        txtStartDate = new JTextField(LocalDate.now().toString());
        formPanel.add(txtStartDate);
        
        formPanel.add(new JLabel("만료일(YYYY-MM-DD):"));
        LocalDate end = LocalDate.now().plusMonths(1).minusDays(1);
        txtEndDate = new JTextField(end.toString());
        formPanel.add(txtEndDate);
        
        formPanel.add(new JLabel("결제일(YYYY-MM-DD):"));
        txtPaymentDate = new JTextField(LocalDate.now().toString());
        formPanel.add(txtPaymentDate);
        
        add(formPanel, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel();
        btnSave = new JButton("등록");
        btnCancel = new JButton("취소");
        btnCancel.addActionListener(e -> dispose());
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);
    }

    public EnrollmentDTO getFormData() {
        LessonItem selected = (LessonItem) cbLessons.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "등록할 강좌가 없습니다.", "경고", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            EnrollmentDTO e = new EnrollmentDTO();
            e.setMemberId(memberId);
            e.setLessonId(selected.getId());
            e.setStartDate(Date.valueOf(txtStartDate.getText().trim()));
            e.setEndDate(Date.valueOf(txtEndDate.getText().trim()));
            e.setPaymentDate(Date.valueOf(txtPaymentDate.getText().trim()));
            return e;
        } catch(IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "날짜 형식이 올바르지 않습니다.", "경고", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    public void addSaveListener(ActionListener listener) { btnSave.addActionListener(listener); }
    
    // 콤보박스에 강좌 객체를 담기 위한 래퍼 클래스
    class LessonItem {
        private int id;
        private String name;
        public LessonItem(int id, String name) { this.id = id; this.name = name; }
        public int getId() { return id; }
        @Override
        public String toString() { return name; }
    }
}
