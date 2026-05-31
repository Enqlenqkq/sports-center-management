package com.taeyoonkim.controller;

import com.taeyoonkim.model.ILessonDAO;
import com.taeyoonkim.model.LessonDTO;
import com.taeyoonkim.view.LessonManageDialog;
import javax.swing.*;
import java.util.List;

public class LessonController {
    private LessonManageDialog view;
    private ILessonDAO lessonDAO;

    public LessonController(LessonManageDialog view, ILessonDAO lessonDAO) {
        this.view = view;
        this.lessonDAO = lessonDAO;
        initController();
        loadLessons();
    }

    private void initController() {
        view.addSaveListener(e -> {
            LessonDTO dto = view.getFormData();
            if (dto != null) {
                boolean success;
                if (dto.getId() == 0) {
                    success = lessonDAO.insertLesson(dto);
                } else {
                    success = lessonDAO.updateLesson(dto);
                }
                if (success) {
                    JOptionPane.showMessageDialog(view, "저장 완료");
                    loadLessons();
                } else {
                    JOptionPane.showMessageDialog(view, "저장 실패");
                }
            }
        });
        
        view.addDeleteListener(e -> {
            Integer id = view.getSelectedLessonId();
            if (id == null) {
                JOptionPane.showMessageDialog(view, "삭제할 강좌를 선택하세요.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(view, "정말 삭제하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (lessonDAO.deleteLesson(id)) {
                    JOptionPane.showMessageDialog(view, "삭제 완료");
                    loadLessons();
                } else {
                    JOptionPane.showMessageDialog(view, "수강 중인 회원이 있어 해당 강좌를 삭제할 수 없습니다.");
                }
            }
        });
    }

    private void loadLessons() {
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
}
