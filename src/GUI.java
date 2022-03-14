import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import static java.awt.Color.BLACK;
import static java.awt.Color.GREEN;
import static java.lang.Math.*;

public class GUI {

    private static JFrame frame = new JFrame("Pythagoras Tree");
    private static ArrayList<int[]> linesToDraw = new ArrayList<>();
    private static JSlider sldAngle, sldDelta, sldIncline, sldStop, sldSize;
    private static JCheckBox antialiasing;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame.setSize(850, 800);
            frame.getContentPane().setBackground(BLACK);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(false);

            setSliders();

            antialiasing = new JCheckBox("antialiasing", true);

            antialiasing.setBounds(10, 10, 100, 15);

            JPanel panel = new JPanel(null);
            frame.add(panel);

            frame.add(sldAngle);
            frame.add(sldSize);
            frame.add(sldDelta);
            frame.add(sldIncline);
            frame.add(sldStop);
            frame.add(antialiasing);

            createLines();
            drawLines();
            frame.setVisible(true);
        });
    }

    private static void drawLines(){
        Container pane = frame.getContentPane();
        pane.add(new JComponent() {
            public void paintComponent(Graphics g) {
                Graphics2D graphics = (Graphics2D) g;
                int h = frame.getHeight() - 200;
                int w = frame.getWidth() - 20;

                if(antialiasing.isSelected()) {
                    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                }

                for (int[] line : linesToDraw) {
                    graphics.setStroke(new BasicStroke(line[4]));
                    graphics.setColor(GREEN);
                    if (line[5] != 1){
                        graphics.setColor(new Color(97, 26,9));
                    }
                    Line2D lin = new Line2D.Float(line[0] + w / 2, h - line[1], line[2] + w / 2, h - line[3]);
                    graphics.draw(lin);
                }
            }
        });
    }

    private static void createLines() {
        int[] lastLine = new int[] {0, 0, 0, sldSize.getValue(), 5, 0};
        linesToDraw.clear();
        linesToDraw.add(lastLine);
        int step = 2;
        createLine(step, lastLine, 90.0 + sldIncline.getValue(), sldSize.getValue());
        createLine(step, lastLine, 90.0 - sldAngle.getValue() * 2 + sldIncline.getValue(), sldSize.getValue());
    }

    private static void createLine(int step, int[] last, double alpha, double lastSize){
        int[] line = new int[6];
        line[0] = last[2];
        line[1] = last[3];

        int x, y;

        x = (int) (lastSize * sldDelta.getValue() / 10.0 * cos(Math.toRadians(alpha + sldAngle.getValue())));
        y = (int) (lastSize * sldDelta.getValue() / 10.0 * sin(Math.toRadians(alpha + sldAngle.getValue())));

        line[2] = last[2] + x;
        line[3] = last[3] + y;
        lastSize = sqrt(x * x + y * y);

        line[4] = setWidth(step);
        line[5] = setColor(step);

        if (lastSize >= sldStop.getValue()){
            linesToDraw.add(line);
            step += 1;
            createLine(step, line, alpha + sldAngle.getValue() + sldIncline.getValue(), lastSize);
            createLine(step, line, alpha - sldAngle.getValue() + sldIncline.getValue(), lastSize);
        }
    }

    private static int setWidth(int step){
        int width = 0;

        if (step == 2){
            width = 4;
        }
        if (step == 3){
            width = 3;
        }
        if (3 < step && step < 8){
            width = 2;
        }
        if (step > 7){
            width = 1;
        }
        return width;
    }

    private static int setColor(int step){
        int color = 0;

        if (step < 5){
            color = 0;
        }
        if (step > 4){
            color = 1;
        }
        return color;
    }

    private static void setSliders() {
        ChangeListener update = e -> {
            frame.repaint();
            createLines();
            drawLines();
        };
        sldAngle = new JSlider(JSlider.HORIZONTAL, 0, 90, 45);
        sldAngle.setMinorTickSpacing(2);
        sldAngle.setMajorTickSpacing(10);
        sldAngle.setPaintTicks(true);
        sldAngle.setPaintLabels(true);
        sldAngle.setBounds(10, 710, 200, 50);
        sldAngle.setBackground(BLACK);
        sldAngle.addChangeListener(update);

        sldIncline = new JSlider(JSlider.HORIZONTAL, -90, 90, 0);
        sldIncline.setMinorTickSpacing(10);
        sldIncline.setMajorTickSpacing(30);
        sldIncline.setPaintTicks(true);
        sldIncline.setPaintLabels(true);
        sldIncline.setBounds(215, 710, 130, 50);
        sldIncline.setBackground(BLACK);
        sldIncline.addChangeListener(update);

        sldStop = new JSlider(JSlider.HORIZONTAL, 1, 30, 8);
        sldStop.setMinorTickSpacing(2);
        sldStop.setMajorTickSpacing(10);
        sldStop.setPaintTicks(true);
        sldStop.setPaintLabels(true);
        sldStop.setBounds(350, 710, 100, 50);
        sldStop.setBackground(BLACK);
        sldStop.addChangeListener(update);

        sldSize = new JSlider(JSlider.HORIZONTAL, 50, 190, 130);
        sldSize.setMinorTickSpacing(5);
        sldSize.setMajorTickSpacing(20);
        sldSize.setPaintTicks(true);
        sldSize.setPaintLabels(true);
        sldSize.setBounds(450, 710, 200, 50);
        sldSize.setBackground(BLACK);
        sldSize.addChangeListener(update);

        sldDelta = new JSlider(JSlider.HORIZONTAL, 1, 8, 8);
        sldDelta.setMinorTickSpacing(1);
        sldDelta.setMajorTickSpacing(1);
        sldDelta.setPaintTicks(true);
        sldDelta.setPaintLabels(true);
        sldDelta.setBounds(660, 710, 150, 50);
        sldDelta.setBackground(BLACK);
        sldDelta.addChangeListener(update);
    }
}
