package com.taeyoonkim.controller;

import com.taeyoonkim.model.*;
import com.taeyoonkim.view.*;
import javax.swing.*;
import java.util.List;

public class EnrollmentController {
    private MemberDetailDialog view;
    private IEnrollmentDAO enrollmentDAO;
    private ILessonDAO lessonDAO;
    private IMemberDAO memberDAO;
    private MainController mainController;

    public EnrollmentController(MemberDetailDialog view, IEnrollmentDAO enrollmentDAO, ILessonDAO lessonDAO, IMemberDAO memberDAO, MainController mainController) {
        this.view = view;
        this.enrollmentDAO = enrollmentDAO;
        this.lessonDAO = lessonDAO;
        this.memberDAO = memberDAO;
        this.mainController = mainController;
        initController();
        loadEnrollments();
    }

    private void initController() {
        view.addEditInfoListener(e -> {
            MemberFormDialog form = new MemberFormDialog((JFrame)view.getParent(), view.getMember());
            new MemberController(form, memberDAO, mainController);
            form.setVisible(true);
            // Update detail view after edit
            MemberDTO updated = null;
            for (MemberDTO m : memberDAO.getAllMembers()) {
                if (m.getId() == view.getMember().getId()) { updated = m; break; }
            }
            if (updated != null) view.displayMemberInfo(updated);
        });
        
        view.addDeleteMemberListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(view, "정말 삭제하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if(memberDAO.deleteMember(view.getMember().getId())) {
                    JOptionPane.showMessageDialog(view, "삭제 완료");
                    view.dispose();
                    mainController.loadMembers();
                } else {
                    JOptionPane.showMessageDialog(view, "삭제 실패");
                }
            }
        });
        
        view.addAddEnrollmentListener(e -> {
            List<LessonDTO> lessons = lessonDAO.getAllLessons();
            EnrollmentFormDialog dialog = new EnrollmentFormDialog(view, view.getMember().getId(), lessons);
            dialog.addSaveListener(event -> {
                EnrollmentDTO dto = dialog.getFormData();
                if (dto != null) {
                    if (enrollmentDAO.insertEnrollment(dto)) {
                        JOptionPane.showMessageDialog(dialog, "수강 등록 완료");
                        dialog.dispose();
                        loadEnrollments();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "수강 등록 실패");
                    }
                }
            });
            dialog.setVisible(true);
        });
        
        view.addCancelEnrollmentListener(e -> cancelEnrollment());
    }

    private void loadEnrollments() {
        SwingWorker<List<EnrollmentDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<EnrollmentDTO> doInBackground() {
                return enrollmentDAO.getEnrollmentsByMemberId(view.getMember().getId());
            }
            @Override
            protected void done() {
                try { view.displayEnrollments(get()); } catch (Exception ex) { ex.printStackTrace(); }
            }
        };
        worker.execute();
    }

    private void cancelEnrollment() {
        int enrollId = view.getSelectedEnrollmentId();
        if (enrollId == -1) {
            JOptionPane.showMessageDialog(view, "취소할 수강 내역을 선택하세요.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(view, "정말 취소하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (enrollmentDAO.deleteEnrollment(enrollId)) {
                JOptionPane.showMessageDialog(view, "수강 취소 완료");
                loadEnrollments();
            } else {
                JOptionPane.showMessageDialog(view, "취소 실패");
            }
        }
    }
}
