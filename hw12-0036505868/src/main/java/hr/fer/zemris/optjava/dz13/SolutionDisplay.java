package hr.fer.zemris.optjava.dz13;

import javax.swing.*;
import java.awt.*;

public class SolutionDisplay extends JComponent {

    private Map map;
    private AntState stateToDraw = null;

    public SolutionDisplay(Map map) {
        this.map = map;
    }

    public void resetMap(Map map) {
        this.map = map;
    }

    public void drawState(AntState antState) {
        this.stateToDraw = antState;
        if (antState != null) {
            map.set(antState.row, antState.col, false);
        }

        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        Dimension dim = getSize();
        Insets ins = getInsets();
        Rectangle r = new Rectangle(ins.left, ins.top,
                dim.width-ins.left-ins.right, dim.height-ins.top-ins.bottom);

        g2d.setColor(Color.WHITE);
        g2d.fillRect(r.x, r.y, r.width, r.height);
        
        drawMap(g2d, r);
        
        if (stateToDraw != null) drawAnt(g2d, r, stateToDraw);
    }

    private void drawAnt(Graphics2D g2d, Rectangle r, AntState stateToDraw) {
        int startY = r.y + stateToDraw.row * r.height / map.getRows() + 1;
        int endY = r.y + (stateToDraw.row+1) * r.height / map.getRows() - 1;
        int startX = r.x + stateToDraw.col * r.width / map.getCols() + 1;
        int endX = r.x + (stateToDraw.col+1) * r.width / map.getCols() - 1;

        int pixPerSquare = r.height / map.getRows();

        g2d.setColor(Color.MAGENTA);
        g2d.fillOval(startX, startY, endX - startX, endY - startY);
    }

    private void drawMap(Graphics2D g2d, Rectangle r) {
        for (int i = 0; i < map.getRows(); i++) {
            int startY = r.y + i * r.height / map.getRows() + 1;
            int endY = r.y + (i+1) * r.height / map.getRows() - 1;

            for (int j = 0; j < map.getCols(); j++) {
                int startX = r.x + j * r.width / map.getCols() + 1;
                int endX = r.x + (j + 1) * r.width / map.getCols() - 1;

                g2d.setColor((map.get(i, j)) ? Color.CYAN : Color.YELLOW);
                g2d.fillRect(startX, startY, endX - startX, endY - startY);
            }
        }
    }

}
