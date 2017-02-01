import sun.plugin.dom.css.Rect;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by Vadim on 01.02.17.
 */
public class VisualiserWindow extends JFrame implements Runnable {
    private ArrayList<ObjectWithClass> points;
    private ArrayList<SeparatingPlane> separatingPlanes;
    private int Width = 500, Height = 500;
    private int PlaneAxe1 = 0, PlaneAxe2 = 1;
    private int x0 = 100, y0 = 100;
    private DecisionTree decisionTree;

    public void setPlaneAxes(int axe1, int axe2) {
        PlaneAxe1 = axe1;
        PlaneAxe2 = axe2;
    }

    public void setPoints(ArrayList<ObjectWithClass> _points) {
        points = _points;
    }

    public void setSeparatingPlanes(ArrayList<SeparatingPlane> _separatingPlanes) {
        separatingPlanes = _separatingPlanes;
    }

    public void setDecisionTree(DecisionTree _decisionTree) {
        decisionTree = _decisionTree;
    }

    private void drawPoints() {
        Graphics g = getGraphics();
        for (int i = 0; i < points.size(); ++i) {
            g.setColor(points.get(i).getClassNumber() == 0 ? Color.RED :
                    points.get(i).getClassNumber() == 1 ? Color.blue :
                            points.get(i).getClassNumber() == 2 ? Color.MAGENTA :
                                    points.get(i).getClassNumber() == 3 ? Color.cyan :
                                            points.get(i).getClassNumber() == 4 ? Color.green : Color.orange
            );
            FeaturesVector coordinates = points.get(i).getFeaturesVector();
            g.drawOval(
                    (int) (Width * coordinates.getFeature(PlaneAxe1)) - 1,
                    (int) (Height * coordinates.getFeature(PlaneAxe2)) - 1,
                    2,
                    2
            );
        }
    }

    private void drawSeparatingPlanes() {
        Graphics g = getGraphics();
        g.setColor(Color.BLACK);
        for (int i = 0; i < separatingPlanes.size(); ++i) {
            if (separatingPlanes.get(i).getDimentionNumber() == PlaneAxe1) {
                int x = (int) (separatingPlanes.get(i).getTresholdValue() * Width);
                g.drawLine(x, 0, x, Height);
            }
            if (separatingPlanes.get(i).getDimentionNumber() == PlaneAxe2) {
                int y = (int) (separatingPlanes.get(i).getTresholdValue() * Height);
                g.drawLine(0, y, Width, y);
            }
        }
    }

    private boolean equalsColor(Color c1, Color c2) {
        if (c1.getGreen() != c2.getGreen()) return false;
        if (c1.getRed() != c2.getRed()) return false;
        if (c1.getBlue() != c2.getBlue()) return false;
        return true;
    }

    private Rectangle getRect(int x, int y) {
        int x1 = 0, y1 = 0, x2 = Width, y2 = Height;
        for (int i = 0; i < separatingPlanes.size(); ++i) {
            if (separatingPlanes.get(i).getDimentionNumber() == PlaneAxe1) {
                int curX = (int) (separatingPlanes.get(i).getTresholdValue() * Width);
                if (curX == x) return new Rectangle(0, 0, 0, 0);
                if (x < curX && curX < x2) x2 = curX;
                if (x1 < curX && curX < x) x1 = curX;
            }
            if (separatingPlanes.get(i).getDimentionNumber() == PlaneAxe2) {
                int curY = (int) (separatingPlanes.get(i).getTresholdValue() * Height);
                if (curY == y) return new Rectangle(0, 0, 0, 0);
                if (y < curY && curY < y2) y2 = curY;
                if (y1 < curY && curY < y) y1 = curY;
            }
        }
        return new Rectangle(x1, y1, x2 - x1, y2 - y1);
    }

    private void paintAreas() {
        Graphics g = getGraphics();
        for (int i = 0; i < points.size(); ++i) {
            int x = (int) (points.get(i).getFeaturesVector().getFeature(PlaneAxe1) * Width);
            int y = (int) (points.get(i).getFeaturesVector().getFeature(PlaneAxe2) * Height);
            int currentClass = decisionTree.getClassByObject(points.get(i).getFeaturesVector());
            if (currentClass == 0) {
                g.setColor(new Color(131, 41, 23));
                Rectangle rect = getRect(x, y);
                g.fillRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
                continue;
            }
            if (currentClass == 1) {
                g.setColor(new Color(0, 0, 113));
                Rectangle rect = getRect(x, y);
                g.fillRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
                continue;
            }
            if (currentClass == 2) {
                g.setColor(new Color(118, 1, 101));
                Rectangle rect = getRect(x, y);
                g.fillRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
                continue;
            }
            if (currentClass == 3) {
                g.setColor(new Color(2, 84, 113));
                Rectangle rect = getRect(x, y);
                g.fillRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
                continue;
            }
            if (currentClass == 4) {
                g.setColor(new Color(0, 113, 41));
                Rectangle rect = getRect(x, y);
                g.fillRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
                continue;
            }
            g.setColor(new Color(130, 73, 5));
            Rectangle rect = getRect(x, y);
            g.fillRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
        }
    }

    VisualiserWindow(int width, int height) throws InterruptedException {
        super("tree visualisation");
        Width = width;
        Height = height;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(x0, y0, Width, Height);
        setVisible(true);
    }

    @Override
    public void run() {
        while (true) {
            paintAreas();
            drawPoints();
            drawSeparatingPlanes();
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
