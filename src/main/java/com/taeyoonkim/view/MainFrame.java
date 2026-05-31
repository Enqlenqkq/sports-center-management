package com.taeyoonkim.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;
import com.taeyoonkim.model.MemberDTO;

public class MainFrame extends JFrame {
    private JTable memberTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton btnSearch;
    private JButton btnAddMember;
    private JButton btnManageLesson;
    private JButton btnReset;

    public MainFrame() {
        setTitle("스포츠센터 회원 관리 프로그램");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(15);
        btnSearch = new JButton("검색");
        btnReset = new JButton("새로고침");
        btnAddMember = new JButton("회원 등록");
        btnManageLesson = new JButton("강좌 관리");
        
        topPanel.add(new JLabel("이름/연락처 검색:"));
        topPanel.add(searchField);
        topPanel.add(btnSearch);
        topPanel.add(btnReset);
        topPanel.add(btnAddMember);
        topPanel.add(btnManageLesson);
        add(topPanel, BorderLayout.NORTH);
        
        String[] columns = {"ID", "이름", "연락처", "성별", "생년월일", "가입일"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        memberTable = new JTable(tableModel);
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(memberTable), BorderLayout.CENTER);
    }

    public void displayMembers(List<MemberDTO> members) {
        tableModel.setRowCount(0);
        for (MemberDTO m : members) {
            tableModel.addRow(new Object[]{
                m.getId(), m.getName(), m.getPhone(), m.getGender(), m.getBirthDate(), m.getCreatedAt()
            });
        }
    }

    public String getSearchKeyword() { return searchField.getText().trim(); }
    public void clearSearchField() { searchField.setText(""); }
    public int getSelectedMemberId() {
        int row = memberTable.getSelectedRow();
        if (row >= 0) return (Integer) tableModel.getValueAt(row, 0);
        return -1;
    }
    
    public void addSearchListener(ActionListener listener) { btnSearch.addActionListener(listener); }
    public void addResetListener(ActionListener listener) { btnReset.addActionListener(listener); }
    public void addAddMemberListener(ActionListener listener) { btnAddMember.addActionListener(listener); }
    public void addManageLessonListener(ActionListener listener) { btnManageLesson.addActionListener(listener); }
    public void addTableDoubleClickListener(MouseAdapter adapter) { memberTable.addMouseListener(adapter); }
}
