package com.taeyoonkim.controller;

import com.taeyoonkim.model.*;
import com.taeyoonkim.view.*;
import javax.swing.*;

public class LessonController {
    private LessonManageDialog view;
    private ILessonDAO lessonDAO;
    private MainController mainController;

    public LessonController(LessonManageDialog view, ILessonDAO lessonDAO, MainController mainController) {
        this.view = view;
        this.lessonDAO = lessonDAO;
        this.mainController = mainController;
        initController();
    }

    private void initController() {
        view.addSaveListener(e -> saveLesson());
        view.addDeleteListener(e -> deleteLesson());
    }

    private void saveLesson() {
        LessonDTO l = view.getFormData();
        if (l != null) {
            if (l.getId() == 0) { // 신규 등록
                if (lessonDAO.insertLesson(l)) {
                    JOptionPane.showMessageDialog(view, "강좌 등록 성공");
                    view.dispose();
                    mainController.loadLessons();
                } else {
                    JOptionPane.showMessageDialog(view, "강좌 등록 실패");
                }
            } else { // 수정
                if (lessonDAO.updateLesson(l)) {
                    JOptionPane.showMessageDialog(view, "강좌 수정 성공");
                    view.dispose();
                    mainController.loadLessons();
                } else {
                    JOptionPane.showMessageDialog(view, "강좌 수정 실패");
                }
            }
        }
    }

    private void deleteLesson() {
        Integer id = view.getLessonId();
        if (id != null) {
            int confirm = JOptionPane.showConfirmDialog(view, "정말 이 강좌를 삭제하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (lessonDAO.deleteLesson(id)) {
                    JOptionPane.showMessageDialog(view, "강좌 삭제 성공");
                    view.dispose();
                    mainController.loadLessons();
                } else {
                    JOptionPane.showMessageDialog(view, "강좌 삭제 실패 (수강 중인 회원이 있을 수 있습니다)");
                }
            }
        }
    }
}
