/*
 * Computer Graphics: Assignment1
 * Author : 66050033 Kittikun Jaroomkluea
 * Author : 6605
 * 
 * Note : 
 *      - To jump between each section of code
 *      search "BELOW THIS"
 */

import javax.swing.*;
import java.awt.*;
import java.lang.*;
import java.util.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class GraphicsSwing 
    extends JPanel implements Runnable
{
    // Frame and Timer
    private int currentFrame = 0; 
    private double lastSecond = 0;
    private double lastTime = System.currentTimeMillis();
    private double currentTime, elapsedTime;
    private double totalSecond = 0;
    private double frameTimer = 0;
    // in 12-frame per second.
    // a single frame will last 1000.0/12 (~0.83 sec) (~83.33ms)
    private double frameDuration = 1000.0 / 12; 

    // color 
    private ArrayList<HashMap<Integer, Color>> color;
    // list of transformation matrix
    private ArrayList<ArrayList<double[]>> circleMove;

    public static void main(String args[]){
        GraphicsSwing m = new GraphicsSwing();
        JFrame f = new JFrame();
        f.add(m);
        f.setTitle("What if I reborn");
        f.setSize(600, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        (new Thread(m)).start();
        
    }
    public GraphicsSwing(){
        createTranformMatrix();
    }

    @Override
    public void run(){
        while (true) {
            currentTime = System.currentTimeMillis();
            elapsedTime = currentTime - lastTime;
            lastTime = currentTime;
            totalSecond += elapsedTime / 1000;

            // move to next frame after pass frameDuration
            frameTimer += elapsedTime;
            if ( frameTimer >= frameDuration ){
                frameTimer -= frameDuration;
                currentFrame += 1;
                // System.out.println("Frame Updated: "+ currentFrame);
            }
            if ( totalSecond - lastSecond > 1 ){
                lastSecond = totalSecond;
            }
            // debug frame and timer
            debug_frame_timer();

            if ( currentFrame > 8 ) currentFrame = 0;

            repaint();

        }
    }

    private void debug_frame_timer(){
        System.out.printf("Timer: %.2f sec, frame(s): %d\n", totalSecond, currentFrame);
    }


    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 600, 600);
        g2d.setColor(Color.BLACK);


        g2d.setTransform(new AffineTransform(
            getFrameTransform(circleMove, currentFrame)
        ));
        drawBall(g2d);
    }
    public void testTransformation(Graphics2D g){
        AffineTransform old = g.getTransform();
        Point2D origin = new Point2D.Double(300, 300);
        Point2D screenPos;
        AffineTransform transform;
        int f = circleMove.size();

        for (int i=0; i<f; ++i){
            transform = new AffineTransform(getFrameTransform(circleMove, i));
            g.setTransform(transform);
            screenPos = transform.transform(origin, null);
            System.out.println(""+i+" painted at (" + screenPos.getX()+", "+screenPos.getY()+")");
            drawBall(g);
        }
    }
    /* BELOW THIS
     * Drawing Stuff
     */
    private void drawBall(Graphics2D g){
        drawCircle(g, 300, 300, 30);
    }


    /* BELOW THIS
     * an implementation of Transformation matrix
     * including
     *  - multiplication
     *  - retrive matrix form ArrayList
     *  - a function that return Transformation Matrix
     */

    private double[] makeRotationMatrix(double angle){
        angle = Math.toRadians(angle);
        return new double[]{cos(angle), -sin(angle), sin(angle), cos(angle), 0, 0};
    }
    private double[] makeScaleMatrix(double xm, double ym){
        return new double[]{xm, 0, 0, ym, 0, 0};
    } 
    private double[] makeTranslationMatrix(double add_x, double add_y){
        return new double[]{1, 0, 0, 1, add_x, add_y};
    }
    private double[] getFrameTransform(
        ArrayList<ArrayList<double[]>> alist,
        int frame
        ){
        // store the result of multiplied matrix
        double result[] = {1, 0, 0, 1, 0, 0};

        if ( frame > alist.size()-1 ){
            System.out.println(""
                +"getFrameTransform(): ArrayList out of bound ("+frame+":"+alist.size()+")");
            return result;
        }

        // list of all matrix at $frame frame
        ArrayList<double[]> transforms = alist.get(frame);

        if ( transforms == null ){
            System.out.println(""
                +"getFrameTransform(): null transform at frame:"+frame);
            return result;
        }


        // multiply all matrix in the list
        for (double m[]: transforms){
            // System.out.println("Multiply with"+ Arrays.toString(m));
            result = multiplyTransform(result, m);
        }
        return result; 
    }
    private double[] multiplyTransform(double A[], double B[]){
        double R[] = new double[6];

        R[0] = A[0] * B[0] + A[2] * B[1] + A[4] * 0;
        R[2] = A[0] * B[2] + A[2] * B[3] + A[4] * 0;
        R[4] = A[0] * B[4] + A[2] * B[5] + A[4] * 1;

        R[1] = A[1] * B[0] + A[3] * B[1] + A[5] * 0;
        R[3] = A[1] * B[2] + A[3] * B[3] + A[5] * 0;
        R[5] = A[1] * B[4] + A[3] * B[5] + A[5] * 1;
        return R;
    }
    
    /* BELOW THIS
     * an implementation of drawing algorithm
     */
    // beier curve
    private void drawCurve(Graphics2D g,
        int x1, int y1,
        int x2, int y2,
        int x3, int y3,
        int x4, int y4 ){

        for (double t=0; t<=1; t+=0.001){
            int x = (int) (
                    Math.pow(1-t, 3) * x1
                    + 3 * t * Math.pow(1-t, 2) * x2
                    + 3 * Math.pow(t, 2) * (1-t) * x3
                    + Math.pow(t, 3) * x4 );
            int y = (int) (
                    Math.pow(1-t, 3) * y1
                    + 3 * t * Math.pow(1-t, 2) * y2
                    + 3 * Math.pow(t, 2) * (1-t) * y3
                    + Math.pow(t, 3) * y4 );
            plot(g, x, y);
        }
    }

    // bresenham line 
    private void drawLine(Graphics2D g, int x1, int y1, int x2, int y2){
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int sx = (x1<x2)? 1:-1;
        int sy = (y1<y2)? 1:-1;
        boolean isSwap = false;

        if (dy > dx){
            int temp;
            temp = dx; dx = dy; dy = temp;
            isSwap = true;
        }
        int D = 2* dy - dx;
        int x = x1;
        int y = y1;

        for (int i=1; i<=dx; ++i){
            plot(g, x, y);
            if ( D >= 0){
                if (isSwap) x += sx;
                else y += sy;
                D -= 2*dx;
            }
            if (isSwap) y += sy;
            else x += sx;
            
            D += 2*dy;
        }
    }

    // midpoint circle algorithm
    private void drawCircle(Graphics2D g, int xc,int yc, int r){
        int x = 0;
        int y = r;
        int Dx = 2*x;
        int Dy = 2*y;
        int D = 1-r;

        while ( x <= y ){
            plot(g, x+xc, y+yc);
            plot(g, -x+xc, y+yc);
            plot(g, x+xc, -y+yc);
            plot(g, -x+xc, -y+yc);

            plot(g, y+xc, x+yc);
            plot(g, -y+xc, x+yc);
            plot(g, y+xc, -x+yc);
            plot(g, -y+xc, -x+yc);

            x = x + 1;
            Dx = Dx + 2;
            if ( D >= 0 ){
                y = y - 1;
                Dy = Dy - 2;
                D = D - Dy;
            }
            D = D + Dx + 1;
        }
    }

    private void plot(Graphics2D g, int x, int y){
        g.fillRect(x, y, 1, 1);
    }

    // draw ellipse
    private void drawEllipse(Graphics2D g, int xc, int yc,
        int a, int b){
        int a2 = a*a;
        int b2 = b*b;
        int twoA2 = 2*a2;
        int twoB2 = 2*b2;

        // Region 1
        int x = 0;
        int y = b;
        int D = Math.round(b2 - a2*b + a2/4);
        int Dx = 0;
        int Dy = twoA2*y;

        while ( Dx <= Dy ){
            plot(g, x+xc, y+yc);
            plot(g, -x+xc, y+yc);
            plot(g, x+xc, -y+yc);
            plot(g, -x+xc, -y+yc);

            x = x + 1;
            Dx = Dx + twoB2;
            D = D + Dx + b2;

            if ( D >= 0 ){
                y = y - 1;
                Dy = Dy - twoA2;
                D = D - Dy;
            }
        }

        // Region 2
        x = a;
        y = 0;
        D = Math.round(a2 - b2*a + b2/4);
        Dx = twoB2*x;
        Dy = 0;
        while ( Dx >= Dy ){
            plot(g, x+xc, y+yc);
            plot(g, -x+xc, y+yc);
            plot(g, x+xc, -y+yc);
            plot(g, -x+xc, -y+yc);

            y = y + 1;
            Dy = Dy + twoA2;
            D = D + Dy + a2;

            if ( D >= 0 ){
                x = x-1;
                Dx = Dx - twoB2;
                D = D - Dx;
            }
        }

    }
    /* BELOW THIS
     *
     */
    private void createTranformMatrix(){
        circleMove = new ArrayList<>();

        // frame 0
        circleMove.add(new ArrayList<>());
        circleMove.get(0).add(makeScaleMatrix(2, 2));
        circleMove.get(0).add(makeTranslationMatrix(-150, -150));

        // frame 1
        circleMove.add(new ArrayList<>());
        circleMove.get(1).add(makeScaleMatrix(3, 1));
        circleMove.get(1).add(makeTranslationMatrix(-200, 30));

        circleMove.add(new ArrayList<>());
        circleMove.get(2).add(makeScaleMatrix(1.5, 2.5));
        circleMove.get(2).add(makeTranslationMatrix(-100, -200));

        circleMove.add(new ArrayList<>());
        circleMove.get(3).add(makeScaleMatrix(1.5, 2.5));
        circleMove.get(3).add(makeTranslationMatrix(-100, -220));

        circleMove.add(new ArrayList<>());
        circleMove.get(4).add(makeScaleMatrix(2, 2));
        circleMove.get(4).add(makeTranslationMatrix(-150, -230));

        circleMove.add(new ArrayList<>());
        circleMove.get(5).add(makeScaleMatrix(2, 2));
        circleMove.get(5).add(makeTranslationMatrix(-150, -250));

        circleMove.add(new ArrayList<>());
        circleMove.get(6).add(makeScaleMatrix(2, 2));
        circleMove.get(6).add(makeTranslationMatrix(-150, -220));

        circleMove.add(new ArrayList<>());
        circleMove.get(7).add(makeScaleMatrix(2, 2));
        circleMove.get(7).add(makeTranslationMatrix(-150, -200));
        
        circleMove.add(new ArrayList<>());
        circleMove.get(8).add(makeScaleMatrix(2, 2));
        circleMove.get(8).add(makeTranslationMatrix(-150, -175));
    }

    /* BELOW THIS
     * Public method for testing
     */
    
    public ArrayList<ArrayList<double[]>> getTransformMatrix(){
        return this.circleMove;
    }
    public double[] public_getFrameTransform(
        ArrayList<ArrayList<double[]>> alist,
        int frame){
        return getFrameTransform(alist, frame);
    }

    private double sin(double x) { return Math.sin(x); }
    private double cos(double x) { return Math.cos(x); }
}