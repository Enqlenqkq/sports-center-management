package com.taeyoonkim;

import com.taeyoonkim.controller.MainController;
import com.taeyoonkim.model.MemberDAOImpl;
import com.taeyoonkim.model.LessonDAOImpl;
import com.taeyoonkim.view.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        // Look and Feel 설정 (시스템 기본 룩 사용)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            // 메인 컨트롤러에 메인 프레임과 모델(DAO) 주입
            new MainController(mainFrame, new MemberDAOImpl(), new LessonDAOImpl());
            mainFrame.setVisible(true);
        });
    }
}