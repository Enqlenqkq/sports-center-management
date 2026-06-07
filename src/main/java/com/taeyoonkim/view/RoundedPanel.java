package com.taeyoonkim.view;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {
    private int radius; // 모서리 둥근 정도
    private Color backgroundColor;// 배경색
    private Color borderColor; // 외곽선 색상
    private int borderThickness; // 외곽선 두께

    // 생성자: 둥근 정도, 배경색, 외곽선색, 외곽선 두께를 설정
    public RoundedPanel(LayoutManager layout) {
        this.radius = 10;
        this.backgroundColor = UIManager.getColor("Panel.background");
        this.borderColor = Color.gray;
        this.borderThickness = 2;
        this.setLayout(layout);
        setOpaque(false);
    }

    public RoundedPanel(int radius, Color backgroundColor, Color borderColor, int borderThickness) {
        this.radius = radius;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.borderThickness = borderThickness;

        setOpaque(false);
    }

    public RoundedPanel(int radius, Color backgroundColor, Color borderColor, int borderThickness,
            LayoutManager layout) {
        this(radius, backgroundColor, borderColor, borderThickness);
        this.setLayout(layout);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // 안티앨리어싱: 곡선을 부드럽게 렌더링 (계단 현상 방지)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.dispose();
    }

    // 2. 둥근 외곽선 그리기
    @Override
    protected void paintBorder(Graphics g) {
        if (borderThickness > 0) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderThickness)); // 선 두께 설정

            // 선이 패널 밖으로 짤리지 않도록 두께의 절반만큼 안쪽으로 들여서 그림
            int offset = borderThickness / 2;
            g2.drawRoundRect(offset, offset, getWidth() - borderThickness - 1, getHeight() - borderThickness - 1,
                    radius, radius);
            g2.dispose();
        }
    }
}