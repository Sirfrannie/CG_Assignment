import javax.swing.*;
import java.awt.*;
import java.lang.*;
import java.util.*;
import java.awt.geom.AffineTransform;

public class Test extends JPanel
{
    private GraphicsSwing gSwing;
    public static void main(String args[]){
        Test m = new Test();
        JFrame f = new JFrame();
        f.add(m);
        f.setTitle("Test");
        f.setSize(600, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

    }
    public Test(){
        gSwing = new GraphicsSwing();
    }

    @Override
    public void paintComponent(Graphics g1){
        Graphics2D g = (Graphics2D) g1;
        // gSwing.testTransformation(g);
        // gSwing.testDraw(g);
        gSwing.testColoring(g);
    }
}