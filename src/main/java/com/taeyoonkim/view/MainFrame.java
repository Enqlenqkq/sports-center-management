package com.taeyoonkim.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;
import com.taeyoonkim.model.MemberDTO;
import com.taeyoonkim.model.LessonDTO;

public class MainFrame extends JFrame {
    private JPanel cards;
    private CardLayout cardLayout;
    
    // 네비게이션 버튼
    private JButton btnNavMember;
    private JButton btnNavLesson;
    
    // Member Panel Components
    private JTable memberTable;
    private DefaultTableModel memberTableModel;
    private JTextField searchField;
    private JButton btnSearch;
    private JButton btnAddMember;
    private JButton btnResetMember;

    // Lesson Panel Components
    private JTable lessonTable;
    private DefaultTableModel lessonTableModel;
    private JButton btnAddLesson;
    private JButton btnResetLesson;

    public MainFrame() {
        setTitle("스포츠센터 회원 관리 프로그램");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        
        // 최상단 네비게이션 패널
        JPanel topNavPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnNavMember = new JButton("회원 관리");
        btnNavLesson = new JButton("센터 관리");
        topNavPanel.add(btnNavMember);
        topNavPanel.add(btnNavLesson);
        add(topNavPanel, BorderLayout.NORTH);
        
        // 카드 레이아웃 생성
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        
        // 1. 회원 관리 패널 구성
        JPanel memberPanel = new JPanel(new BorderLayout());
        JPanel memberTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(15);
        btnSearch = new JButton("검색");
        btnResetMember = new JButton("새로고침");
        btnAddMember = new JButton("회원 등록");
        
        memberTopPanel.add(new JLabel("이름/연락처 검색:"));
        memberTopPanel.add(searchField);
        memberTopPanel.add(btnSearch);
        memberTopPanel.add(btnResetMember);
        memberTopPanel.add(btnAddMember);
        memberPanel.add(memberTopPanel, BorderLayout.NORTH);
        
        String[] memberColumns = {"ID", "이름", "연락처", "성별", "생년월일", "가입일"};
        memberTableModel = new DefaultTableModel(memberColumns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        memberTable = new JTable(memberTableModel);
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JPanel memberTableWrapper = new JPanel(new BorderLayout());
        memberTableWrapper.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        memberTableWrapper.add(new JScrollPane(memberTable), BorderLayout.CENTER);
        memberPanel.add(memberTableWrapper, BorderLayout.CENTER);
        
        // 2. 센터(강좌) 관리 패널 구성
        JPanel lessonPanel = new JPanel(new BorderLayout());
        JPanel lessonTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAddLesson = new JButton("강좌 등록");
        btnResetLesson = new JButton("새로고침");
        
        lessonTopPanel.add(btnAddLesson);
        lessonTopPanel.add(btnResetLesson);
        lessonPanel.add(lessonTopPanel, BorderLayout.NORTH);
        
        String[] lessonColumns = {"ID", "강좌명", "요일", "시간", "강사명", "정원", "수강료"};
        lessonTableModel = new DefaultTableModel(lessonColumns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        lessonTable = new JTable(lessonTableModel);
        lessonTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JPanel lessonTableWrapper = new JPanel(new BorderLayout());
        lessonTableWrapper.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        lessonTableWrapper.add(new JScrollPane(lessonTable), BorderLayout.CENTER);
        lessonPanel.add(lessonTableWrapper, BorderLayout.CENTER);
        
        // 카드 패널에 추가
        cards.add(memberPanel, "MEMBER");
        cards.add(lessonPanel, "LESSON");
        
        add(cards, BorderLayout.CENTER);
    }
    
    // 뷰 전환 메서드
    public void showMemberPanel() { cardLayout.show(cards, "MEMBER"); }
    public void showLessonPanel() { cardLayout.show(cards, "LESSON"); }

    // 데이터 디스플레이
    public void displayMembers(List<MemberDTO> members) {
        memberTableModel.setRowCount(0);
        for (MemberDTO m : members) {
            memberTableModel.addRow(new Object[]{
                m.getId(), m.getName(), m.getPhone(), m.getGender(), m.getBirthDate(), m.getCreatedAt()
            });
        }
    }
    
    public void displayLessons(List<LessonDTO> lessons) {
        lessonTableModel.setRowCount(0);
        for (LessonDTO l : lessons) {
            lessonTableModel.addRow(new Object[]{
                l.getId(), l.getName(), l.getDayOfWeek(), l.getTime(), l.getInstructorName(), l.getCapacity(), l.getPrice()
            });
        }
    }

    // Getters for selections and text
    public String getSearchKeyword() { return searchField.getText().trim(); }
    public void clearSearchField() { searchField.setText(""); }
    
    public int getSelectedMemberId() {
        int row = memberTable.getSelectedRow();
        if (row >= 0) return (Integer) memberTableModel.getValueAt(row, 0);
        return -1;
    }
    
    public int getSelectedLessonId() {
        int row = lessonTable.getSelectedRow();
        if (row >= 0) return (Integer) lessonTableModel.getValueAt(row, 0);
        return -1;
    }
    
    // Listeners (Navigation)
    public void addNavMemberListener(ActionListener listener) { btnNavMember.addActionListener(listener); }
    public void addNavLessonListener(ActionListener listener) { btnNavLesson.addActionListener(listener); }
    
    // Listeners (Member)
    public void addSearchListener(ActionListener listener) { btnSearch.addActionListener(listener); }
    public void addResetMemberListener(ActionListener listener) { btnResetMember.addActionListener(listener); }
    public void addAddMemberListener(ActionListener listener) { btnAddMember.addActionListener(listener); }
    public void addMemberTableDoubleClickListener(MouseAdapter adapter) { memberTable.addMouseListener(adapter); }
    
    // Listeners (Lesson)
    public void addAddLessonListener(ActionListener listener) { btnAddLesson.addActionListener(listener); }
    public void addResetLessonListener(ActionListener listener) { btnResetLesson.addActionListener(listener); }
    public void addLessonTableDoubleClickListener(MouseAdapter adapter) { lessonTable.addMouseListener(adapter); }
}
