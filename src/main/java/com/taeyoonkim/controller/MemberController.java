package com.taeyoonkim.controller;

import com.taeyoonkim.model.IMemberDAO;
import com.taeyoonkim.model.MemberDTO;
import com.taeyoonkim.view.MemberFormDialog;
import javax.swing.JOptionPane;

public class MemberController {
    private MemberFormDialog view;
    private IMemberDAO memberDAO;
    private MainController mainController;

    public MemberController(MemberFormDialog view, IMemberDAO memberDAO, MainController mainController) {
        this.view = view;
        this.memberDAO = memberDAO;
        this.mainController = mainController;
        initController();
    }

    private void initController() {
        view.addSaveListener(e -> {
            MemberDTO dto = view.getFormData();
            if (dto != null) {
                boolean success;
                if (dto.getId() == 0) {
                    success = memberDAO.insertMember(dto);
                } else {
                    success = memberDAO.updateMember(dto);
                }
                
                if (success) {
                    JOptionPane.showMessageDialog(view, "저장 완료");
                    view.dispose();
                    mainController.loadMembers();
                } else {
                    JOptionPane.showMessageDialog(view, "저장 실패");
                }
            }
        });
    }
}
