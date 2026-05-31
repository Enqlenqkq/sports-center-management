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

    public MainController(MainFrame view, IMemberDAO memberDAO) {
        this.view = view;
        this.memberDAO = memberDAO;
        initController();
        loadMembers();
    }

    private void initController() {
        view.addSearchListener(e -> searchMembers(view.getSearchKeyword()));
        view.addResetListener(e -> {
            view.clearSearchField();
            loadMembers();
        });
        
        view.addAddMemberListener(e -> {
            MemberFormDialog dialog = new MemberFormDialog(view, null);
            new MemberController(dialog, memberDAO, this);
            dialog.setVisible(true);
        });
        
        view.addManageLessonListener(e -> {
            LessonManageDialog dialog = new LessonManageDialog(view);
            new LessonController(dialog, new LessonDAOImpl());
            dialog.setVisible(true);
        });
        
        view.addTableDoubleClickListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openMemberDetail();
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
        
        // Find member from DB
        MemberDTO member = null;
        for (MemberDTO m : memberDAO.getAllMembers()) {
            if (m.getId() == memberId) { member = m; break; }
        }
        if (member != null) {
            MemberDetailDialog dialog = new MemberDetailDialog(view, member);
            new EnrollmentController(dialog, new EnrollmentDAOImpl(), new LessonDAOImpl(), memberDAO, this);
            dialog.setVisible(true);
        }
    }
}
