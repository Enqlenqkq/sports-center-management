package com.taeyoonkim.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import com.taeyoonkim.model.LessonDTO;

public class LessonManageDialog extends JDialog {
    private JTable lessonTable;
    private DefaultTableModel tableModel;
    private JTextField txtName, txtDay, txtTime, txtInstructor, txtCapacity, txtPrice;
    private JButton btnSave, btnDelete;
    private Integer selectedLessonId;

    public LessonManageDialog(Frame parent) {
        super(parent, "센터 강좌 관리", true);
        setSize(700, 500);
        setLocationRelativeTo(parent);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        
        // 왼쪽: 테이블
        String[] columns = {"ID", "강좌명", "요일", "시간", "강사명", "정원", "수강료"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        lessonTable = new JTable(tableModel);
        lessonTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lessonTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && lessonTable.getSelectedRow() != -1) {
                fillFormFromSelectedRow();
            }
        });
        
        add(new JScrollPane(lessonTable), BorderLayout.CENTER);
        
        // 오른쪽: 폼
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setPreferredSize(new Dimension(250, 0));
        
        formPanel.add(new JLabel("강좌명:"));
        txtName = new JTextField();
        formPanel.add(txtName);
        
        formPanel.add(new JLabel("요일:"));
        txtDay = new JTextField();
        formPanel.add(txtDay);
        
        formPanel.add(new JLabel("시간:"));
        txtTime = new JTextField();
        formPanel.add(txtTime);
        
        formPanel.add(new JLabel("강사명:"));
        txtInstructor = new JTextField();
        formPanel.add(txtInstructor);
        
        formPanel.add(new JLabel("정원:"));
        txtCapacity = new JTextField();
        formPanel.add(txtCapacity);
        
        formPanel.add(new JLabel("수강료:"));
        txtPrice = new JTextField();
        formPanel.add(txtPrice);
        
        btnSave = new JButton("저장(수정/추가)");
        btnDelete = new JButton("삭제");
        
        JButton btnClear = new JButton("폼 비우기");
        btnClear.addActionListener(e -> clearForm());
        
        formPanel.add(btnSave);
        formPanel.add(btnDelete);
        formPanel.add(btnClear);
        
        add(formPanel, BorderLayout.EAST);
    }

    private void fillFormFromSelectedRow() {
        int row = lessonTable.getSelectedRow();
        selectedLessonId = (Integer) tableModel.getValueAt(row, 0);
        txtName.setText((String) tableModel.getValueAt(row, 1));
        txtDay.setText((String) tableModel.getValueAt(row, 2));
        txtTime.setText((String) tableModel.getValueAt(row, 3));
        txtInstructor.setText((String) tableModel.getValueAt(row, 4));
        txtCapacity.setText(tableModel.getValueAt(row, 5).toString());
        txtPrice.setText(tableModel.getValueAt(row, 6).toString());
    }
    
    private void clearForm() {
        selectedLessonId = null;
        lessonTable.clearSelection();
        txtName.setText("");
        txtDay.setText("");
        txtTime.setText("");
        txtInstructor.setText("");
        txtCapacity.setText("");
        txtPrice.setText("");
    }

    public void displayLessons(List<LessonDTO> lessons) {
        tableModel.setRowCount(0);
        for (LessonDTO l : lessons) {
            tableModel.addRow(new Object[]{
                l.getId(), l.getName(), l.getDayOfWeek(), l.getTime(), l.getInstructorName(), l.getCapacity(), l.getPrice()
            });
        }
        clearForm();
    }

    public LessonDTO getFormData() {
        String name = txtName.getText().trim();
        if(name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "강좌명을 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        LessonDTO l = new LessonDTO();
        if (selectedLessonId != null) l.setId(selectedLessonId);
        l.setName(name);
        l.setDayOfWeek(txtDay.getText().trim());
        l.setTime(txtTime.getText().trim());
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
    
    public Integer getSelectedLessonId() { return selectedLessonId; }

    public void addSaveListener(ActionListener listener) { btnSave.addActionListener(listener); }
    public void addDeleteListener(ActionListener listener) { btnDelete.addActionListener(listener); }
}
