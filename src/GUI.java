import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Line2D;

import javax.swing.JOptionPane;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import java.util.ArrayList;

import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;
import static java.lang.StrictMath.sqrt;


class Main {

   private static ArrayList<int[]> linesToDraw = new ArrayList<>();
   private static int size = 100;
   private static int stop = 15;
   private static Double delta = (double) 0.5;
   private static Double angle = (double) 20;

    public static void main(String[] args) {

        createLines();

        drawLines(linesToDraw);
    }

    private static void createLines(){
        int[] lastLine = new int[] {0, 0, 0, size};
        linesToDraw.add(lastLine);
        int step = 1;
        createLine(step, lastLine, 1);
        createLine(step, lastLine, -1);
    }

    private static void createLine(int step, int[] last, int side){
        int[] line = new int[4];
        line[0] = last[2];
        line[1] = last[3];
        int x = (int)(size * sin(StrictMath.toRadians(angle * step)) / ((step + 1) * delta) * side);
        int y = (int)(size * cos(StrictMath.toRadians(angle * step)) / ((step + 1) * delta));
        line[2] = last[2] + x;
        line[3] = last[3] + y;

        if (sqrt(x * x + y * y) >= stop){
            linesToDraw.add(line);
            step += 1;
            createLine(step, line, side);
            createLine(step, line, side * -1);
        }
    }

    private static void drawLines(ArrayList<int[]> listOfLines){
        Runnable r = () -> {
            LineComponent lineComponent = new LineComponent(400,400);

            for (int[] line : listOfLines) {
                lineComponent.addLine(line[0], line[1], line[2], line[3]);
            }

            JOptionPane.showMessageDialog(null, lineComponent);
        };

        SwingUtilities.invokeLater(r);
    }

}

class LineComponent extends JComponent {

    private ArrayList<Line2D.Double> lines;

    LineComponent(int width, int height) {
        super();
        setPreferredSize(new Dimension(width, height));
        lines = new ArrayList<>();
    }

    void addLine(int x1, int y1, int x2, int y2) {
        int height = (int) getPreferredSize().getHeight();
        int width = (int) getPreferredSize().getWidth();
        Line2D.Double line = new Line2D.Double(x1 + width / 2, height -  y1, x2 + width / 2, height - y2);

        lines.add(line);
        repaint();
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.black);

        for (Line2D.Double line : lines) {
            g.drawLine(
                    (int)line.getX1(),
                    (int)line.getY1(),
                    (int)line.getX2(),
                    (int)line.getY2()
            );
        }
    }
}