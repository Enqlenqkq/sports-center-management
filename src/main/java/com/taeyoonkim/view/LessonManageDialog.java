package com.taeyoonkim.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import com.taeyoonkim.model.LessonDTO;

public class LessonManageDialog extends JDialog {
    private JTextField txtName, txtInstructor, txtCapacity, txtPrice;
    private JCheckBox[] chkDays;
    private JComboBox<String> comboTime;
    
    private String[] dayString = {"월", "화", "수", "목", "금", "토", "일"};
    private String[] timeString = {"06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", 
                                   "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00"};
    
    private JButton btnSave, btnDelete, btnCancel;
    private Integer lessonId; // null = add, not null = edit

    public LessonManageDialog(Frame parent, LessonDTO lesson) {
        super(parent, lesson == null ? "강좌 등록" : "강좌 수정", true);
        setSize(420, 400); // 체크박스 나열을 위해 가로 길이를 살짝 넓힘
        setLocationRelativeTo(parent);
        initUI();
        if (lesson != null) {
            setFormData(lesson);
        } else {
            btnDelete.setVisible(false); // 신규 등록 시 삭제 버튼 숨김
        }
    }

    private void initUI() {
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        formPanel.add(new JLabel("강좌명:"));
        txtName = new JTextField();
        formPanel.add(txtName);
        
        formPanel.add(new JLabel("요일:"));
        JPanel daysPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        chkDays = new JCheckBox[7];
        for (int i = 0; i < 7; i++) {
            chkDays[i] = new JCheckBox(dayString[i]);
            daysPanel.add(chkDays[i]);
        }
        formPanel.add(daysPanel);
        
        formPanel.add(new JLabel("시간:"));
        comboTime = new JComboBox<>(timeString);
        formPanel.add(comboTime);
        
        formPanel.add(new JLabel("강사명:"));
        txtInstructor = new JTextField();
        formPanel.add(txtInstructor);
        
        formPanel.add(new JLabel("정원:"));
        txtCapacity = new JTextField();
        formPanel.add(txtCapacity);
        
        formPanel.add(new JLabel("수강료:"));
        txtPrice = new JTextField();
        formPanel.add(txtPrice);
        
        add(formPanel, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel();
        btnSave = new JButton("저장");
        btnDelete = new JButton("삭제");
        btnCancel = new JButton("취소");
        
        btnCancel.addActionListener(e -> dispose());
        
        btnPanel.add(btnSave);
        btnPanel.add(btnDelete);
        btnPanel.add(btnCancel);
        
        add(btnPanel, BorderLayout.SOUTH);
    }

    public void setFormData(LessonDTO l) {
        this.lessonId = l.getId();
        txtName.setText(l.getName());
        
        // "월, 수, 금" 형태의 문자열을 파싱하여 체크박스 체크
        String days = l.getDayOfWeek();
        if (days != null && !days.isEmpty()) {
            for (int i = 0; i < 7; i++) {
                chkDays[i].setSelected(days.contains(dayString[i]));
            }
        }
        
        comboTime.setSelectedItem(l.getTime());
        txtInstructor.setText(l.getInstructorName());
        txtCapacity.setText(String.valueOf(l.getCapacity()));
        txtPrice.setText(String.valueOf(l.getPrice()));
    }

    public LessonDTO getFormData() {
        String name = txtName.getText().trim();
        if(name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "강좌명을 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        
        // 체크된 요일들을 순회하며 ", " 콤마로 연결
        StringBuilder selectedDays = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            if (chkDays[i].isSelected()) {
                if (selectedDays.length() > 0) selectedDays.append(", ");
                selectedDays.append(dayString[i]);
            }
        }
        if (selectedDays.length() == 0) {
            JOptionPane.showMessageDialog(this, "최소 1개 이상의 요일을 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        LessonDTO l = new LessonDTO();
        if (lessonId != null) l.setId(lessonId);
        l.setName(name);
        l.setDayOfWeek(selectedDays.toString());
        l.setTime((String) comboTime.getSelectedItem());
        l.setInstructorName(txtInstructor.getText().trim());
        try {
            l.setCapacity(txtCapacity.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtCapacity.getText().trim()));
            l.setPrice(txtPrice.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtPrice.getText().trim()));
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "정원과 수강료는 숫자여야 합니다.", "경고", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return l;
    }

    public Integer getLessonId() { return lessonId; }
    
    public void addSaveListener(ActionListener listener) { btnSave.addActionListener(listener); }
    public void addDeleteListener(ActionListener listener) { btnDelete.addActionListener(listener); }
}
