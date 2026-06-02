package com.taeyoonkim.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;
import com.taeyoonkim.model.LessonDTO;
import com.taeyoonkim.model.EnrollmentDTO;
import java.time.LocalDate;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

public class EnrollmentFormDialog extends JDialog {
    private JComboBox<LessonItem> cbLessons;
    private DatePicker dpStartDate, dpEndDate, dpPaymentDate;
    private JButton btnSave, btnCancel;
    private int memberId;

    public EnrollmentFormDialog(JDialog parent, int memberId, List<LessonDTO> lessons) {
        super(parent, "수강 등록", true);
        this.memberId = memberId;
        setSize(400, 250);
        setLocationRelativeTo(parent);
        initUI(lessons);
    }

    private DatePicker createConfiguredDatePicker() {
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        settings.setAllowKeyboardEditing(true);
        return new DatePicker(settings);
    }

    private void initUI(List<LessonDTO> lessons) {
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        formPanel.add(new JLabel("강좌 선택:"));
        cbLessons = new JComboBox<>();
        for (LessonDTO l : lessons) {
            cbLessons.addItem(new LessonItem(l.getId(), l.getName()));
        }
        formPanel.add(cbLessons);
        
        formPanel.add(new JLabel("시작일:"));
        dpStartDate = createConfiguredDatePicker();
        dpStartDate.setDateToToday();
        formPanel.add(dpStartDate);
        
        formPanel.add(new JLabel("만료일:"));
        dpEndDate = createConfiguredDatePicker();
        dpEndDate.setDate(LocalDate.now().plusMonths(1).minusDays(1));
        formPanel.add(dpEndDate);
        
        formPanel.add(new JLabel("결제일:"));
        dpPaymentDate = createConfiguredDatePicker();
        dpPaymentDate.setDateToToday();
        formPanel.add(dpPaymentDate);
        
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
        
        LocalDate start = dpStartDate.getDate();
        LocalDate end = dpEndDate.getDate();
        LocalDate payment = dpPaymentDate.getDate();
        
        if (start == null || end == null || payment == null) {
            JOptionPane.showMessageDialog(this, "모든 날짜를 올바르게 선택해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        
        EnrollmentDTO e = new EnrollmentDTO();
        e.setMemberId(memberId);
        e.setLessonId(selected.getId());
        e.setStartDate(Date.valueOf(start));
        e.setEndDate(Date.valueOf(end));
        e.setPaymentDate(Date.valueOf(payment));
        return e;
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
