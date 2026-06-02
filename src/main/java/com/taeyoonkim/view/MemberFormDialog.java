package com.taeyoonkim.view;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.time.LocalDate;
import com.taeyoonkim.model.MemberDTO;
import com.github.lgooddatepicker.components.DatePicker;

public class MemberFormDialog extends JDialog {
    private JTextField txtName;
    private JFormattedTextField txtPhone;
    private ButtonGroup genderButtonGroup;
    private JRadioButton rdoMale, rdoFemale;
    private DatePicker birthDatePicker;
    private JTextArea txtNotes;
    private JButton btnSave, btnCancel;
    private Integer memberId; 

    public MemberFormDialog(Frame parent, MemberDTO member) {
        super(parent, member == null ? "회원 등록" : "회원 수정", true);
        setSize(400, 400);
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
        try {
            MaskFormatter phoneFormatter = new MaskFormatter("010-####-####");
            phoneFormatter.setPlaceholderCharacter('_');
            txtPhone = new JFormattedTextField(phoneFormatter);
            txtPhone.setColumns(15);
            formPanel.add(txtPhone);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        formPanel.add(new JLabel("성별:"));
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        genderButtonGroup = new ButtonGroup();
        rdoMale = new JRadioButton("남");
        rdoFemale = new JRadioButton("여");
        genderButtonGroup.add(rdoMale);
        genderButtonGroup.add(rdoFemale);
        genderPanel.add(rdoMale);
        genderPanel.add(rdoFemale);
        formPanel.add(genderPanel);

        formPanel.add(new JLabel("생년월일:"));
        com.github.lgooddatepicker.components.DatePickerSettings dateSettings = new com.github.lgooddatepicker.components.DatePickerSettings();
        dateSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        dateSettings.setAllowKeyboardEditing(true);
        birthDatePicker = new DatePicker(dateSettings);
        formPanel.add(birthDatePicker);

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
        
        if ("남".equals(m.getGender())) {
            rdoMale.setSelected(true);
        } else if ("여".equals(m.getGender())) {
            rdoFemale.setSelected(true);
        }

        if (m.getBirthDate() != null) {
            birthDatePicker.setDate(m.getBirthDate().toLocalDate());
        }
        txtNotes.setText(m.getNotes());
    }

    public MemberDTO getFormData() {
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        if (name.isEmpty() || phone.isEmpty() || phone.contains("_")) {
            JOptionPane.showMessageDialog(this, "이름과 연락처를 올바르게 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        MemberDTO m = new MemberDTO();
        if (memberId != null) m.setId(memberId);
        m.setName(name);
        m.setPhone(phone);
        
        if (rdoMale.isSelected()) m.setGender("남");
        else if (rdoFemale.isSelected()) m.setGender("여");
        else m.setGender("");

        LocalDate selectedDate = birthDatePicker.getDate();
        if (selectedDate != null) {
            m.setBirthDate(Date.valueOf(selectedDate));
        }
        m.setNotes(txtNotes.getText().trim());
        return m;
    }

    public void addSaveListener(ActionListener listener) {
        btnSave.addActionListener(listener);
    }
}
