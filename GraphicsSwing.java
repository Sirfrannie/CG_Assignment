/*
 * Computer Graphics: Assignment1
 * Author : 66050033 Kittikun Jaroomkluea
 * Author : 66050243 Nisa Chuensuwan 
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
import java.awt.image.BufferedImage;

public class GraphicsSwing 
    extends JPanel implements Runnable
{
    private final int SIZE = 600;
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
        plot(g2d, 0, 0, 600);
        g2d.setColor(Color.BLACK);


        g2d.setTransform(new AffineTransform(
            getFrameTransform(circleMove, currentFrame)
        ));
        drawBall(g2d);
    }
    /* public method for testing with other class */
    public void testTransformation(Graphics2D g){
        AffineTransform old = g.getTransform();
        Point2D origin = new Point2D.Double(300, 300);
        Point2D screenPos;
        AffineTransform transform;
        int f = circleMove.size();
        double transmatrix[];

        for (int i=0; i<f; ++i){
            if ( 1 == 1) {
            transmatrix = getFrameTransform(circleMove, i);
            transform = new AffineTransform(transmatrix);
            System.out.println(Arrays.toString(transmatrix));
            g.setTransform(transform);
            screenPos = transform.transform(origin, null);
            System.out.println(""+i+" painted at (" + screenPos.getX()+", "+screenPos.getY()+")");
            drawBall(g);
            }
        }
    }
    public void testDraw(Graphics2D g){
        BufferedImage test_buffer = new BufferedImage(SIZE+1, SIZE+1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g_buf = test_buffer.createGraphics();
        g_buf.setColor(Color.WHITE);
        plot(g_buf, 0, 0, 600);

        g_buf.setColor(Color.BLACK);


        // drawBaby(g_buf);
        drawBabyFace(g_buf);
        drawMamaHand(g_buf);


        g.drawImage(test_buffer, 0, 0, null);
    }
    private void drawBall(Graphics2D g){
        // drawCircle(g, 300, 300, 30);
        g.drawOval(300, 300, 30,30);
    }

    /* BELOW THIS
     * Drawing Stuff
     */
    private BufferedImage drawFrame(int frame){
        BufferedImage buffer = new BufferedImage(SIZE+1, SIZE+1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buffer.createGraphics();
        g.setColor(Color.WHITE);
        plot(g, 0, 0, 600);

        g.setColor(Color.BLACK);
        drawBabyFace(g);

        return buffer;
    }

    private void drawBaby(Graphics2D g){
        // head
        drawCircle(g, 200, 200, 50);
        // three litte hair
        drawLine(g, 192, 135, 194, 151);
        drawLine(g, 200, 135, 200, 151);
        drawLine(g, 206, 135, 204, 151);
        // ear
        drawCurve(g,
            200, 210,
            190, 220,
            180, 175,
            197, 190 
        );
        // eye
        drawEllipse(g, 225, 190, 4, 6);
        // eyebrown
        drawLine(g, 227, 174, 215, 179);
        // dummy
        drawCurve(g,
            250, 200,
            265, 180,
            264, 230,
            249, 220
        );
        // body
        // back spine
        drawLine(g, 180, 243, 160, 285);
        drawLine(g, 160, 285, 160, 310);
        // butt
        drawLine(g, 160, 310, 155, 330);
        drawLine(g, 155, 330, 155, 335);
        drawLine(g, 155, 335, 160, 360);
        drawLine(g, 160, 360, 190, 362);
        drawLine(g, 190, 362, 215, 357);
        drawCurve(g,
            215, 357,
            206, 340,
            205, 315,
            215, 312
        );
        drawCurve(g,
            215, 312,
            214, 309,
            212, 308,
            200, 310
        );
        drawLine(g, 160, 300, 209, 300);
        // chest
        drawLine(g, 205, 250, 210, 255);
        drawLine(g, 211, 272, 205, 310);
        // rigth arm
        drawLine(g, 190, 265, 275, 215);
        drawLine(g, 196, 283, 275, 227);
        // hand
        drawCurve(g,
            275, 215,
            278, 210,
            280, 202,
            290, 195
        );
        drawLine(g, 290, 195, 300, 197);
        drawCurve(g,
            300, 197,
            302, 200,
            305, 211,
            280, 230
        );
        drawLine(g, 280, 230, 275, 227);
        // Leg
        drawCurve(g,
            215, 312,
            220, 315,
            250, 320,
            260, 335
        );
        drawLine(g, 260, 335, 260, 345);
        drawCurve(g,
            260, 345,
            250, 355,
            240, 360,
            215, 357
        );
        // left arm
        drawLine(g, 214, 270, 230, 318);
        drawLine(g, 209, 300, 212, 310);  
    }
    private void drawBabyFace(Graphics2D g){
        AffineTransform ot = g.getTransform();
        // head shape
        drawPolygon(g,
  /* x axis */new int[]{70, 167, 286, 352, 381, 417, 419, 385, 294, 203, 111,  67,  67, 48},
  /* y axis */new int[]{119, 67,  60, 110, 190, 235, 294, 350, 390, 402, 393, 340, 277, 195} 
        );
        g.setTransform(new AffineTransform(
            multiplyTransform(
                makeRotationMatrix(20), 
                makeTranslationMatrix(-70, 100)
            )
        ));
        // dummy
        drawCircle(g, 210, 280, 65);
        // left(HS) eye
        drawEllipse(g, 123, 170, 45, 52);
        drawEllipse(g, 123, 170, 15, 22);
        drawCircle(g, 127, 132, 15);
        // right(HS) eye
        drawEllipse(g, 296, 170, 45, 52);
        drawEllipse(g, 296, 170, 15, 22);
        drawCircle(g, 300, 132, 15);

        // cheek
        g.setTransform(ot);
        drawLine(g, 107, 289, 102, 320);
        drawLine(g, 133, 283, 128, 319);
        drawLine(g, 162, 282, 156, 309);
        drawLine(g, 312, 231, 312, 253);
        drawLine(g, 334, 216, 330, 262);
        drawLine(g, 361, 217, 359, 244);
        // hair
        drawCurve(g,
            144, 124,
            144, 136,
            155, 151,
            168, 152
        );
        drawLine(g, 182, 98, 196, 140);
        drawCurve(g,
            229, 98,
            236, 106,
            234, 127,
            226, 139
        );
    }
    
    private void drawMamaHand(Graphics2D g){
        // hand
        int xVal[] = {235, 227, 236, 314, 305, 295, 250, 217, 193, 173, 181, 212, 
                    260, 308, 330, 343, 350, 329, 308, 287, 270, 275, 292, 317, 
                    346, 377, 399, 410, 414, 407, 393, 379, 393, 407, 422, 441, 
                    460, 470, 484, 499, 513, 536, 547, 556, 545, 532, 522, 539, 
                    547, 548, 520, 480, 442, 431, 421, 348, 320};

        int yVal[] = {514, 493, 475, 483, 438, 406, 350, 313, 285, 255, 242, 265, 
                    311, 354, 374, 369, 350, 307, 256, 205, 164, 140, 146, 192, 
                    239, 295, 332, 332, 320, 260, 179, 120, 108, 119, 173, 252, 
                    319, 325, 308, 263, 227, 184, 174, 185, 237, 286, 330, 400, 
                    468, 509, 510, 520, 533, 548, 573, 551, 543};
        drawPolygon(g, xVal, yVal);

        // wraist
        int xVal1[] = {433, 421, 431, 442, 480, 520, 548, 558, 560, 591};
        int yVal1[] = {599, 573, 548, 533, 520, 510, 509, 512, 547, 599};
        drawPolygon(g, xVal1, yVal1);


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
     * including
     *  - drawCurve() : Bezier Curve
     *  - drawLine() : Bresenham Line
     *  - drawCircle() : Midpoint Circle
     *  - drawEllipse() : Midpoint Ellipse
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

    private void drawPolygon(Graphics2D g, int xPoly[], int yPoly[]){
        Polygon p = new Polygon(xPoly, yPoly, xPoly.length);
        g.drawPolygon(p);
    }

    private void plot(Graphics2D g, int x, int y){
        g.fillRect(x, y, 1, 1);
    }

    private void plot(Graphics2D g, int x, int y, int size){
        g.fillRect(x, y, size, size);
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
        circleMove.get(1).add(makeTranslationMatrix(-204.5, 30));

        circleMove.add(new ArrayList<>());
        circleMove.get(2).add(makeScaleMatrix(1.5, 2.5));
        circleMove.get(2).add(makeTranslationMatrix(-95, -200));

        circleMove.add(new ArrayList<>());
        circleMove.get(3).add(makeScaleMatrix(1.5, 2.5));
        circleMove.get(3).add(makeTranslationMatrix(-95, -220));

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

        circleMove.add(new ArrayList<>());
        circleMove.get(9).add(makeScaleMatrix(0.25, 0.25));
        //circleMove.get(9).add(makeTranslationMatrix(-300, -300));

    }

    
    private double sin(double x) { return Math.sin(x); }
    private double cos(double x) { return Math.cos(x); }
}