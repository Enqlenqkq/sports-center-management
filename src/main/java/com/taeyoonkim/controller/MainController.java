package com.taeyoonkim.controller;

import com.taeyoonkim.model.*;
import com.taeyoonkim.view.*;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MainController {
    private MainFrame view;
    private IMemberDAO memberDAO;
    private ILessonDAO lessonDAO;

    public MainController(MainFrame view, IMemberDAO memberDAO, ILessonDAO lessonDAO) {
        this.view = view;
        this.memberDAO = memberDAO;
        this.lessonDAO = lessonDAO;
        initController();
        loadMembers();
        loadLessons();
    }

    private void initController() {
        // 네비게이션 버튼 (CardLayout 전환)
        view.addNavMemberListener(e -> {
            view.showMemberPanel();
            loadMembers();
        });
        view.addNavLessonListener(e -> {
            view.showLessonPanel();
            loadLessons();
        });

        // 1. 회원 관리 이벤트
        view.addSearchListener(e -> searchMembers(view.getSearchKeyword()));
        view.addResetMemberListener(e -> {
            view.clearSearchField();
            loadMembers();
        });
        view.addAddMemberListener(e -> {
            MemberFormDialog dialog = new MemberFormDialog(view, null);
            new MemberController(dialog, memberDAO, this);
            dialog.setVisible(true);
        });
        view.addMemberTableDoubleClickListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openMemberDetail();
                }
            }
        });
        
        // 2. 강좌 관리 이벤트
        view.addAddLessonListener(e -> {
            LessonManageDialog dialog = new LessonManageDialog(view, null);
            new LessonController(dialog, lessonDAO, this);
            dialog.setVisible(true);
        });
        view.addResetLessonListener(e -> loadLessons());
        view.addLessonTableDoubleClickListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openLessonEdit();
                }
            }
        });
    }

    public void loadMembers() {
        SwingWorker<List<MemberDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<MemberDTO> doInBackground() {
                return memberDAO.getAllMembers();
            }
            @Override
            protected void done() {
                try { view.displayMembers(get()); } catch (Exception ex) { ex.printStackTrace(); }
            }
        };
        worker.execute();
    }

    public void loadLessons() {
        SwingWorker<List<LessonDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<LessonDTO> doInBackground() {
                return lessonDAO.getAllLessons();
            }
            @Override
            protected void done() {
                try { view.displayLessons(get()); } catch (Exception ex) { ex.printStackTrace(); }
            }
        };
        worker.execute();
    }

    private void searchMembers(String keyword) {
        SwingWorker<List<MemberDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<MemberDTO> doInBackground() {
                return memberDAO.searchMembers(keyword);
            }
            @Override
            protected void done() {
                try { view.displayMembers(get()); } catch (Exception ex) { ex.printStackTrace(); }
            }
        };
        worker.execute();
    }

    private void openMemberDetail() {
        int memberId = view.getSelectedMemberId();
        if (memberId == -1) return;
        
        MemberDTO member = null;
        for (MemberDTO m : memberDAO.getAllMembers()) {
            if (m.getId() == memberId) { member = m; break; }
        }
        if (member != null) {
            MemberDetailDialog dialog = new MemberDetailDialog(view, member);
            new EnrollmentController(dialog, new EnrollmentDAOImpl(), lessonDAO, memberDAO, this);
            dialog.setVisible(true);
        }
    }
    
    private void openLessonEdit() {
        int lessonId = view.getSelectedLessonId();
        if (lessonId == -1) return;
        
        LessonDTO lesson = null;
        for (LessonDTO l : lessonDAO.getAllLessons()) {
            if (l.getId() == lessonId) { lesson = l; break; }
        }
        if (lesson != null) {
            LessonManageDialog dialog = new LessonManageDialog(view, lesson);
            new LessonController(dialog, lessonDAO, this);
            dialog.setVisible(true);
        }
    }
}
