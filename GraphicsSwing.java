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

class ColorSeed {
    public int x, y;
    public Color color;

    public ColorSeed(int x, int y, Color color){
        this.x = x;
        this.y = y;
        this.color = color;
    }
}
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
    private ArrayList<ArrayList<ColorSeed>> color;
    // list of transformation matrix
    private ArrayList<ArrayList<double[]>> circleMove;
    private ArrayList<ArrayList<double[]>> drawBabyMove;
    private ArrayList<ArrayList<double[]>> drawBabyFaceMove;
    private ArrayList<ArrayList<double[]>> drawMamaHandMove;
    private ArrayList<ArrayList<double[]>> drawHiFiveMove;
    private ArrayList<ArrayList<double[]>> drawMamaFaceMove;
    private ArrayList<ArrayList<double[]>> drawHandsMove;

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
        createBabyMove();
        createBabyFaceMove();
        createMamaHandMove();
        createHifiveMove();
        createMamaFaceMove();
        createHandMove();
        createFrameColor();
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

            if ( currentFrame > 66) currentFrame = 0;
            // if ( currentFrame > 66) return;
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

        BufferedImage buffer = drawFrame();

        buffer = paintColor(buffer);

        g2d.drawImage(buffer, 0, 0, null);
    }

    /* public method for testing with other class */
    public void testColoring(Graphics2D g2d){
        currentFrame = 3;
        g2d.setColor(Color.WHITE);
        plot(g2d, 0, 0, 600);
        g2d.setColor(Color.BLACK);

        BufferedImage buffer = drawFrame();

        buffer = paintColor(buffer);

        g2d.drawImage(buffer, 0, 0, null);
    }
    public void testTransformation(Graphics2D g){
        AffineTransform old = g.getTransform();
        Point2D origin = new Point2D.Double(300, 300);
        Point2D screenPos;
        AffineTransform transform;
        /* Change Here */
        int f = drawHandsMove.size();
        double transmatrix[];

        for (int i=0; i<f; ++i){
            if ( i == 12 || i == 13) {
                /* Change Here */
                if ( drawHandsMove.get(i) == null ) continue;
                /* Change Here */
                transmatrix = getFrameTransform(drawHandsMove, i);
                if (transmatrix == null) continue;
                transform = new AffineTransform(transmatrix);
                g.setTransform(transform);
                screenPos = transform.transform(origin, null);

                System.out.println(Arrays.toString(transmatrix));
                System.out.println(""+i+" -----painted at (" + screenPos.getX()+", "+screenPos.getY()+")");
                /* Change Here */
                drawhands(g);
            }
        }

        /* Change Here */
        f = drawBabyMove.size();
        for (int i=0; i<f; ++i){
            if ( i == 13 ) {
                /* Change Here */
                if ( drawBabyMove.get(i) == null ) continue;
                /* Change Here */
                transmatrix = getFrameTransform(drawBabyMove, i);
                if (transmatrix == null) continue;
                transform = new AffineTransform(transmatrix);
                g.setTransform(transform);
                screenPos = transform.transform(origin, null);

                System.out.println(Arrays.toString(transmatrix));
                System.out.println(""+i+" painted at (" + screenPos.getX()+", "+screenPos.getY()+")");
                /* Change Here */
                drawBaby(g);
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
        // drawBabyFace(g_buf);
        // drawMamaHand(g_buf);
        // drawHiFive(g_buf);
        // drawMamaFace(g_buf, 2);
        drawhands(g_buf);
        // drawManHead(g_buf);
        // drawManBody(g_buf);


        g.drawImage(test_buffer, 0, 0, null);
    }
    private void drawBall(Graphics2D g){
        // drawCircle(g, 300, 300, 30);
        g.drawOval(300, 300, 30,30);
    }

    /* BELOW THIS
     * Drawing Stuff
     */
    private BufferedImage drawFrame(){
        BufferedImage buffer = new BufferedImage(SIZE+1, SIZE+1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buffer.createGraphics();
        g.setColor(Color.WHITE);
        plot(g, 0, 0, 600);

        AffineTransform old = g.getTransform();

        g.setColor(Color.BLACK);
        /*
        if (currentFrame < circleMove.size()
            && circleMove.get(currentFrame) != null){
            g.setTransform(new AffineTransform(
                getFrameTransform(circleMove, currentFrame)
            ));
            drawBall(g);
            g.setTransform(old);
        }
        */
        if ( currentFrame < drawBabyMove.size() 
            && drawBabyMove.get(currentFrame) != null ){
            g.setTransform(new AffineTransform(
                getFrameTransform(drawBabyMove, currentFrame)
            ));
            drawBaby(g);
            g.setTransform(old);
        }
        if ( currentFrame < drawBabyFaceMove.size()
            && drawBabyFaceMove.get(currentFrame) != null ){
            g.setTransform(new AffineTransform(
                getFrameTransform(drawBabyFaceMove, currentFrame)
            ));
            drawBabyFace(g);
            g.setTransform(old);
        }
        if ( currentFrame < drawMamaHandMove.size()
            && drawMamaHandMove.get(currentFrame) != null ){
            g.setTransform(new AffineTransform(
                getFrameTransform(drawMamaHandMove, currentFrame)
            ));
            drawMamaHand(g);
            g.setTransform(old);
        }
        if ( currentFrame < drawHiFiveMove.size()
            && drawHiFiveMove.get(currentFrame) != null ){
            g.setTransform(new AffineTransform(
                getFrameTransform(drawHiFiveMove, currentFrame)
            ));
            drawHiFive(g);
            g.setTransform(old);
        }
        if ( currentFrame < drawMamaFaceMove.size() 
            && drawMamaFaceMove.get(currentFrame) != null){
            g.setTransform(new AffineTransform(
                getFrameTransform(drawMamaFaceMove, currentFrame)
            ));
            drawMamaFace(g, (currentFrame > 53)?2:1);
            g.setTransform(old);
        }
        if ( currentFrame < drawHandsMove.size() 
            && drawHandsMove.get(currentFrame) != null){
            g.setTransform(new AffineTransform(
                getFrameTransform(drawHandsMove, currentFrame)
            ));
            drawhands(g);
            g.setTransform(old);
        }

        // drawBabyFace(g);

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
            245, 220
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
        drawLine(g, 196, 283, 275, 226);
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
        drawLine(g, 280, 230, 274, 226);
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
        // face shape
        int x_face[] = {183, 178, 178, 187, 204, 223, 247, 270, 302, 330, 
                    357, 378, 391, 399, 405, 408, 414, 418, 423, 431, 
                    436, 438, 434, 428, 426, 434, 439, 440, 438, 434, 
                    425, 417, 407, 392, 376, 355, 334, 311, 283, 256, 
                    248, 244, 239, 233, 224, 211, 199, 194, 191, 197, 
                    206, 205};
        int y_face[] = {299, 264, 236, 206, 181, 166, 155, 145, 142, 146, 
                    153, 163, 170, 185, 202, 213, 208, 203, 202, 209, 
                    220, 237, 251, 256, 259, 271, 282, 292, 316, 336, 
                    354, 368, 381, 392, 401, 408, 413, 415, 408, 397, 
                    389, 380, 371, 372, 371, 365, 356, 346, 336, 331, 
                    331, 325};
        drawPolygon(g, x_face, y_face);
        drawLine(g, 205, 325, 219, 342);
        drawLine(g, 239, 371, 234, 360);
        drawLine(g, 409, 213, 412, 226);
        drawLine(g, 419, 244, 426, 259);

        // dummy
        int x_d[] = {343, 335, 319, 317, 323, 335, 353, 369, 389, 391, 
                    389, 383, 380, 369, 363, 355, 346, 346, 344};
        int y_d[] = {386, 386, 372, 353, 339, 325, 321, 325, 338, 
                    348, 360, 368, 360, 352, 350, 347, 350, 358, 
                    373};
        drawPolygon(g, x_d, y_d);
        int x_d2[] = {343, 345, 354, 363, 370, 377, 383, 383, 380, 
                    369, 363, 355, 346, 346, 344};
        int y_d2[] = {386, 392, 396, 396, 392, 387, 376, 368, 360, 352,  
                    350, 347, 350, 358, 373};
        drawPolygon(g, x_d2, y_d2);
        drawCircle(g, 362, 372, 10);

        // eyelash
        int x_el1[] = {292, 274, 260, 250, 247, 251, 259, 247, 242, 243,  
                    253, 276};
        int y_el1[] = {286, 296, 310, 325, 334, 339, 343, 340, 334, 319,  
                    300, 288};
        drawPolygon(g, x_el1, y_el1);
        drawLine(g, 276, 345, 299, 332);
        int x_el2[] = {328, 335, 352, 372, 391, 393, 394, 390, 391, 388,  
                    363, 345};
        int y_el2[] = {265, 254, 243, 242, 247, 255, 268, 280,  
                    264, 252, 249, 254};
        drawPolygon(g, x_el2, y_el2);
        drawLine(g, 360, 300, 384, 287);
         
        // eye
        int x_e1[] ={253, 259, 270, 284, 293, 296, 295, 288, 279, 267, 257};
        int y_e1[] ={320, 333, 340, 340, 330, 319, 306, 298, 293, 302,
                    311};
        drawPolygon(g, x_e1, y_e1);
        drawCircle(g, 273, 317, 9);
        drawCircle(g, 278, 302, 7);
        
        int x_e2[] = {341, 341, 344, 354, 364, 369, 380, 382, 381, 373, 363};
        int y_e2[] = {257, 270, 281, 291, 294, 295, 284, 273, 262, 249, 249};
        drawPolygon(g, x_e2, y_e2);
        drawCircle(g, 362, 270, 9);
        drawCircle(g, 365, 255, 6);

        // eye brown
        int x_eb1[] = {263, 240, 248};
        int y_eb1[] = {269, 300, 279};
        drawPolygon(g, x_eb1, y_eb1);
        int x_eb2[] = {324, 336, 359, 339, 331};
        int y_eb2[] = {235, 228, 234, 232, 234};
        drawPolygon(g, x_eb2, y_eb2);

        // hair
        drawCurve(g,
            255, 196,
            248, 217,
            264, 233,
            286, 232
        );
        drawLine(g, 286, 232, 273, 206);

        // cheek
        drawLine(g, 275, 369, 277, 382);
        drawLine(g, 286, 354, 293, 389);
        drawLine(g, 296, 357, 299, 372);
        drawLine(g, 396, 308, 401, 327);
        drawLine(g, 408, 296, 414, 330);
        drawLine(g, 423, 296, 423, 312); 

        drawLine(g, 247, 387, 200, 450);
        drawLine(g, 199, 590, 200, 450);
        drawLine(g, 284, 451, 288, 481);
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

    private void drawHiFive(Graphics2D g){
        // Mama Hand
        int x_mama[] = {62, 64, 81, 120, 170, 233, 259, 233, 200, 179, 191, 206,
                        237, 262, 304, 319, 337, 350, 355, 373, 390, 389, 385, 381,
                        400, 417, 429, 462, 487, 522, 548, 554, 537, 507, 477, 456,
                        418, 373, 367, 334, 291, 240, 224, 208, 180, 129, 75, 37, 35,
                        69, 132, 175, 193, 147, 92};
        int y_mama[] = {133, 113, 107, 146, 203, 262, 251, 182, 100, 45, 33, 38,
                        97, 163, 238, 244, 232, 150, 92, 46, 58, 100, 163, 244,
                        300, 312, 318, 295, 276, 260, 257, 284, 303, 331, 354,
                        392, 445, 457, 386, 346, 335, 385, 430, 411, 358, 328,
                        293, 261, 238, 250, 286, 309, 294, 230, 165};
        drawPolygon(g, x_mama, y_mama);
        drawCurve(g,
            429, 318,
            402, 318,
            380, 342,
            388, 380
        );
        drawCurve(g,
            345, 282,
            306, 281,
            230, 316,
            208, 353
        );

 
        // Baby Hand
        int x_baby[] = {138, 169, 202, 222, 214, 224, 240, 291, 334, 367, 373, 359,
                        334, 294, 290, 282, 275};
        int y_baby[] = {599, 545, 512, 498, 465, 430, 385, 335, 346, 386, 457, 498,
                        515, 526, 544, 574, 595};
        drawPolygon(g, x_baby, y_baby);
        drawCurve(g,
            232, 501,
            253, 498,
            274, 508,
            287, 528
        );
    }

    private void drawMamaFace(Graphics2D g, int a){
        // hair
        int x_hair[] = {356, 351, 355, 358, 402, 415, 437, 455, 455, 460, 467, 473,
                        520, 493, 476, 462, 447, 453, 483, 507, 522, 553, 560, 574,
                        585, 595, 584, 570, 563, 539, 511, 478, 448, 436, 422, 377,
                        324, 289, 267, 255, 233, 214, 198, 188, 180, 174, 175, 179,
                        184, 193, 195, 195, 191, 192, 194, 201, 209, 221, 235, 249,
                        270, 291, 315, 335};
        int y_hair[] = {155, 203, 238, 245, 256, 241, 220, 201, 223, 252, 284, 310,
                        352, 378, 383, 381, 399, 406, 466, 508, 538, 412, 359, 312,
                        266, 233, 184, 133, 108, 76, 54, 43, 40, 37, 27, 24, 34, 43,
                        53, 68, 100, 127, 158, 172, 187, 212, 232, 249, 266, 284, 289,
                        277, 259, 241, 224, 212, 204, 195, 188, 186, 183, 179, 173,
                        167};
        drawPolygon(g, x_hair, y_hair);
        drawCurve(g,
            380, 270,
            362, 223,
            343, 147,
            380, 92
        );
        drawCurve(g,
            465, 152,
            465, 175,
            460, 190,
            455, 201
        );
        // face
        int x_face[] = {240, 226, 215, 203, 199, 195, 197, 203, 207, 215, 221, 231,
                        240, 245, 254, 260, 267, 273, 285, 304, 328, 353, 376, 398,
                        414, 424, 429, 440, 462, 476, 493, 520, 473, 467, 460, 455,
                        455, 437, 415, 402, 358, 355, 351, 356, 335, 315, 291, 270,
                        249};
        int y_face[] = {185, 213, 237, 264, 277, 289, 298, 313, 323, 342, 358, 376,
                        392, 401, 414, 424, 432, 436, 438, 439, 438, 436, 432, 428,
                        425, 422, 417, 405, 381, 383, 378, 352, 310, 284, 252, 223,
                        201, 220, 241, 256, 245, 238, 203, 155, 167, 173, 179, 183,
                        186};
        drawPolygon(g, x_face, y_face);
        drawLine(g, 212, 246, 206, 235);
        drawLine(g, 207, 219, 206, 235);
        drawLine(g, 207, 219, 214, 199);
        drawLine(g, 501, 332, 462, 381);
        // eye brown
        int x_eb[] = {270, 301, 337, 367, 360, 331, 294};
        int y_eb[] = {144, 146, 155, 165, 160, 140, 139};
        drawPolygon(g, x_eb, y_eb);
        int x_eb2[] = {421, 443, 469, 497, 486, 466, 453};
        int y_eb2[] = {206, 219, 239, 271, 250, 224, 210};
        drawPolygon(g, x_eb2, y_eb2);
        
        if ( a == 1 ){
            // EYE 1
            // eye lash
            int x_el[] = {403, 419, 435, 457, 469, 481, 473, 463, 469, 472, 464, 446,
                        425};
            int y_el[] = {237, 237, 241, 254, 265, 285, 288, 293, 288, 280, 266, 252,
                        242};
            drawPolygon(g, x_el, y_el);
            int x_el2[] = {343, 332, 312, 295, 275, 252, 246, 259, 260, 262, 263, 264,
                        277, 294, 308, 326};
            int y_el2[] = {205, 188, 175, 169, 167, 171, 174, 174, 180, 191, 179, 174,
                        173, 173, 177, 188};
            drawPolygon(g, x_el2, y_el2);

            // eye ball
            int x_ebl[] = {294, 279, 276, 275, 278, 284, 296, 311, 320, 323, 322, 308};
            int y_ebl[] = {173, 184, 189, 203, 215, 220, 222, 216, 210, 194, 186, 177};
            drawPolygon(g, x_ebl, y_ebl);
            int x_ebl2[] = {442, 430, 422, 424, 430, 446, 461, 465, 466, 464, 446};
            int y_ebl2[] = {251, 260, 269, 284, 294, 294, 287, 274, 269, 266, 252};

            drawPolygon(g, x_ebl2, y_ebl2);
            drawLine(g, 260, 200, 266, 210);
            drawLine(g, 280, 220, 266, 210);
            drawLine(g, 280, 220, 300, 227);

            drawLine(g, 411, 279, 414, 285);
            drawLine(g, 428, 297, 414, 285);
            drawLine(g, 428, 297, 442, 303);
            drawLine(g, 454, 299, 442, 303);

            // inner
            int x_ei[] = {436, 449, 450, 436};
            int y_ei[] = {271, 265, 280, 284};
            drawPolygon(g, x_ei, y_ei);
            int x_ew[] = {465, 466, 461, 452, 452, 456};
            int y_ew[] = {274, 266, 263, 265, 267, 274};
            drawPolygon(g, x_ew, y_ew);

            int x_ei2[] = {301, 290, 290, 295, 304, 306};
            int y_ei2[] = {189, 194, 207, 210, 206, 200};
            drawPolygon(g, x_ei2, y_ei2);
            int x_ew2[] = {320, 307, 314, 321, 322};
            int y_ew2[] = {187, 190, 201, 198, 189};
            drawPolygon(g, x_ew2, y_ew2);
        }else{
            int x_e[] = {326, 292, 252, 246, 267, 259, 255, 267, 274, 283, 299, 317};
            int y_e[] = {229, 207, 198, 197, 195, 188, 181, 190, 195, 196, 204, 217};
            drawPolygon(g, x_e, y_e);

            int x_e2[] = {394, 435, 466, 482, 470, 474, 440, 427};
            int y_e2[] = {263, 270, 298, 306, 302, 308, 280, 274};
            drawPolygon(g, x_e2, y_e2);
        }

        // nose
        int x_n[] = {340, 335, 318, 316, 330};
        int y_n[] = {280, 292, 317, 307, 294};
        drawPolygon(g, x_n, y_n);


        // mount
        drawCurve(g,
            263, 337,
            268, 348,
            285, 361,
            302, 366
        );
        drawCurve(g,
            312, 372,
            322, 376,
            340, 380,
            356, 380
        );


        drawLine(g, 367, 433, 340, 464);
        drawLine(g, 272, 468, 363, 464);
        drawLine(g, 404, 426, 363, 464);

        drawLine(g, 107, 332, 207, 328);

        drawLine(g, 0, 392, 107, 332);
        drawLine(g, 219, 352, 217, 412);
        drawLine(g, 272, 468, 217, 412);
        drawLine(g, 232, 380, 226, 424);

    }
    private void drawhands(Graphics2D g) {
        drawPolygon(g,
            new int[]{388, 404, 448, 483, 500, 534, 482, 434, 413, 407, 403},
            new int[]{185, 172, 132, 95, 79, 110, 145, 180, 195, 202, 190} 
        );
        drawPolygon(g,
            new int[]{555, 415, 416, 437, 573, 562},
            new int[]{23, 163, 179, 181, 82, 54} 
        );
        drawPolygon(g,
            new int[]{388, 385, 379, 375, 370, 365, 360, 355, 350, 346, 340, 339, 344, 348, 345, 338, 332, 325, 319, 312, 314, 323, 331, 337, 347, 342, 337, 326, 319, 319, 327, 330, 335, 338, 351, 351, 347, 343, 340, 333, 324, 324, 330, 334, 348, 359, 353, 346, 338, 333, 331, 339, 350, 366, 384, 400, 407},
            new int[]{185, 186, 186, 186, 185, 185, 186, 188, 190, 194, 199, 207, 207, 205, 210, 215, 219, 225, 227, 238, 242, 234, 228, 225, 220, 225, 229, 237, 247, 251, 244, 242, 238, 237, 228, 230, 233, 236, 237, 240, 248, 253, 250, 244, 243, 233, 239, 244, 244, 246, 251, 250, 248, 240, 232, 207, 200}
        );

        drawPolygon(g,
            new int[]{345, 345, 346, 347, 348, 350, 352, 354, 357},
            new int[]{210, 213, 216, 219, 222, 225, 227, 230, 233}
        );
        drawPolygon(g,
            new int[]{367, 368, 369, 370, 371, 373, 374, 375, 377},
            new int[]{199, 198, 198, 197, 197, 195, 194, 193, 191}
        );
        drawPolygon(g,
            new int[]{364, 365, 367, 368, 370, 371, 374, 376, 378, 380},
            new int[]{230, 227, 225, 224, 222, 220, 219, 217, 216, 215}
        );
    }
    
    private void drawManHead(Graphics2D g){
        int x_head[] = {191, 130, 120, 91, 72, 65, 92, 130, 157, 177, 206, 262, 276, 280, 291, 283, 287, 272, 281, 263, 232};
        int y_head[] = {148, 171, 181, 207, 249, 283, 344, 372, 381, 394, 401, 407, 389, 338, 318, 292, 261, 224, 209, 187, 165};
        drawPolygon(g, x_head, y_head);

        drawLine(g, 260, 360, 251, 372);
        drawLine(g, 280, 338, 274, 348);
        drawLine(g, 249, 334, 258, 329);
        drawLine(g, 249, 306, 258, 329);
        drawLine(g, 208, 364, 190, 358);
        drawLine(g, 180, 340, 190, 358);
        drawLine(g, 147, 376, 145, 364);
        drawLine(g, 135, 360, 120, 340); 
        drawLine(g, 120, 340, 120, 316);
        drawLine(g, 120, 316, 138, 315);
        drawLine(g, 138, 315, 145, 298);
        drawLine(g, 145, 298, 134, 292);
        drawLine(g, 140, 280, 134, 292);
        drawLine(g, 140, 280, 127, 270);
        drawLine(g, 181, 227, 127, 270);
        drawLine(g, 181, 227, 235, 213);
        drawLine(g, 272, 224, 235, 213);

        drawPolygon(g,
            new int[]{220, 186, 201, 221},
            new int[]{290, 306, 314, 306}
        );
        drawPolygon(g,
            new int[]{253, 280, 274, 260},
            new int[]{275, 271, 286, 290}
        );

        drawLine(g, 206, 295, 212, 309);
        drawLine(g, 214, 292, 219, 307);
        drawLine(g, 263, 274, 266, 288);
        drawLine(g, 269, 273, 272, 287);

    }
    private void drawManBody(Graphics2D g){
        int x_b[] = {117, 104, 189, 202, 190, 192, 216, 224, 258, 2, 8, 36, 109};
        int y_b[] = {429, 460, 492, 469, 449, 443, 472, 499, 595, 591, 559, 473, 428};
        drawPolygon(g, x_b, y_b);
    
        drawLine(g, 146, 375, 109, 438);
        drawLine(g, 183, 467, 216, 400);

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
    private double[] makeDefaultMatrix(){
        return new double[]{1, 0, 0, 1, 0, 0};
    }
    private double[] getFrameTransform(
        ArrayList<ArrayList<double[]>> alist,
        int frame
        ){
        // store the result of multiplied matrix
        double result[] = {1, 0, 0, 1, 0, 0};

        if ( frame > alist.size()-1 ){
            // System.out.println("" +"getFrameTransform(): ArrayList out of bound ("+frame+":"+alist.size()+")");
            return null;
        }

        if ( alist.get(frame) == null ){
            // System.out.println("" +"getFrameTransform(): null transform at frame:"+frame);
            return null;
        }

        // list of all matrix at $frame frame
        ArrayList<double[]> transforms = alist.get(frame);

        // multiply all matrix in the list
        for (double m[]: transforms){
            // System.out.println("Multiply with"+ Arrays.toString(m));
            if ( m == null ) return null;
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
     * Coloring Algorithm
     */
    private BufferedImage paintColor(BufferedImage buff){
        // set of all color at frame
        if ( currentFrame >= color.size() ){
            return buff;
        }
        ArrayList<ColorSeed> cs = color.get(currentFrame);
        
        if ( cs == null ){
            System.out.printf("frame %d cs is null\n", currentFrame);
            return buff;
        }

        for (ColorSeed s: cs){
            buff = floodFill(buff, s.x, s.y, new Color(buff.getRGB(s.x, s.y)), s.color);
        }
        return buff;
        
    }
    private BufferedImage floodFill(BufferedImage m, int x, int y,
        Color targetColor, Color replacement_color){

        Graphics2D g2d = m.createGraphics();
        Queue<Point> q = new LinkedList<>();
        
        if (m.getRGB(x, y) == targetColor.getRGB()){
            g2d.setColor(replacement_color);
            plot(g2d, x, y);
            q.add(new Point(x, y));
        }

        while (!q.isEmpty()){
            Point p = q.poll();
            // s
            if (p.y < 600 && m.getRGB(p.x, p.y+1) == targetColor.getRGB()){
                g2d.setColor(replacement_color);
                plot(g2d, p.x, p.y+1);
                q.add(new Point(p.x, p.y+1));
            }
            // n
            if (p.y > 0 && m.getRGB(p.x, p.y-1) == targetColor.getRGB()){
                g2d.setColor(replacement_color);
                plot(g2d, p.x, p.y-1);
                q.add(new Point(p.x, p.y-1));
            }
            // w
            if (p.x > 0 && m.getRGB(p.x-1, p.y) == targetColor.getRGB()){
                g2d.setColor(replacement_color);
                plot(g2d, p.x-1, p.y);
                q.add(new Point(p.x-1, p.y));
            }
            // e
            if (p.x < 600 && m.getRGB(p.x+1, p.y) == targetColor.getRGB()){
                g2d.setColor(replacement_color);
                plot(g2d, p.x+1, p.y);
                q.add(new Point(p.x+1, p.y));
            }
        }
        return m;

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

    /* Color */
    private void createFrameColor(){
        color = new ArrayList<>();
        int frame = 0; 
        color.add(new ArrayList<>()); // frame 0

        color.add(new ArrayList<>()); // frame 1
        color.get(frame+1).add(new ColorSeed(2, 2, new Color(150, 179, 242)));
        color.get(frame+1).add(new ColorSeed(150, 30, new Color(230, 67, 105)));

        color.add(new ArrayList<>()); // frame 1
        color.get(frame+2).add(new ColorSeed(2, 2, new Color(150, 179, 242)));
        color.get(frame+2).add(new ColorSeed(150, 30, new Color(230, 67, 105)));
    }

    /* Animation */
    private void createBabyMove(){
        drawBabyMove = new ArrayList<>();
        int begin = 0; 

        for (int i=0; i<begin; ++i) drawBabyMove.add(null);

        //
        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin).add(makeScaleMatrix(4, 4));
        drawBabyMove.get(begin).add(makeTranslationMatrix(-250, -125));

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+1).add(makeScaleMatrix(4, 4));
        drawBabyMove.get(begin+1).add(makeTranslationMatrix(-250, -125));

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+2).add(getFrameTransform(drawBabyMove, begin+1));

        //
        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+3).add(makeScaleMatrix(3.5, 3.5));
        drawBabyMove.get(begin+3).add(makeTranslationMatrix(-237, -120)); 

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+4).add(getFrameTransform(drawBabyMove, begin+3));

        // 
        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+5).add(makeScaleMatrix(3, 3));
        drawBabyMove.get(begin+5).add(makeTranslationMatrix(-220, -115));

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+6).add(getFrameTransform(drawBabyMove, begin+5));

        //
        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+7).add(makeScaleMatrix(2.5, 2.5));
        drawBabyMove.get(begin+7).add(makeTranslationMatrix(-197, -103));

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+8).add(getFrameTransform(drawBabyMove, begin+7));

        //
        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+9).add(makeScaleMatrix(2, 2));
        drawBabyMove.get(begin+9).add(makeTranslationMatrix(-160, -90));

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+10).add(getFrameTransform(drawBabyMove, begin+9));

        //
        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+11).add(makeScaleMatrix(1.5, 1.5));
        drawBabyMove.get(begin+11).add(makeTranslationMatrix(-105, -60));

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+12).add(getFrameTransform(drawBabyMove, begin+11));

        //
        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+13).add(makeDefaultMatrix());

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+14).add(getFrameTransform(drawBabyMove, begin+13));

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+15).add(getFrameTransform(drawBabyMove, begin+13));

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+16).add(getFrameTransform(drawBabyMove, begin+13));

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+17).add(getFrameTransform(drawBabyMove, begin+13));

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+18).add(getFrameTransform(drawBabyMove, begin+13));
    }

    private void createBabyFaceMove(){
        drawBabyFaceMove = new ArrayList<>();
        int begin = 19;

        for (int i=0; i<begin; ++i) drawBabyFaceMove.add(null);

        //
        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin).add(makeRotationMatrix(-20)); 
        drawBabyFaceMove.get(begin).add(makeTranslationMatrix(130, -140));

        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+1).add(getFrameTransform(drawBabyFaceMove, begin));

        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+2).add(getFrameTransform(drawBabyFaceMove, begin));
        
        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+3).add(getFrameTransform(drawBabyFaceMove, begin));

        // 
        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+4).add(makeTranslationMatrix(10, 0));

        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+5).add(getFrameTransform(drawBabyFaceMove, begin+4));

        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+6).add(getFrameTransform(drawBabyFaceMove, begin+4));

        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+7).add(getFrameTransform(drawBabyFaceMove, begin+4));
        
        // 
        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+8).add(makeScaleMatrix(1.2, 1.2));
        drawBabyFaceMove.get(begin+8).add(makeTranslationMatrix(-90, -25));

        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+9).add(getFrameTransform(drawBabyFaceMove, begin+8));

        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+10).add(getFrameTransform(drawBabyFaceMove, begin+8));

        // 
        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+11).add(makeScaleMatrix(1.2, 1.2));
        drawBabyFaceMove.get(begin+11).add(makeTranslationMatrix(-150, 0));

        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+12).add(getFrameTransform(drawBabyFaceMove, begin+11));

        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+13).add(getFrameTransform(drawBabyFaceMove, begin+11));
        
        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+14).add(getFrameTransform(drawBabyFaceMove, begin+11));

        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+15).add(getFrameTransform(drawBabyFaceMove, begin+11));

        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+16).add(getFrameTransform(drawBabyFaceMove, begin+11));

        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+17).add(getFrameTransform(drawBabyFaceMove, begin+11));

        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+18).add(getFrameTransform(drawBabyFaceMove, begin+11));

        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+19).add(getFrameTransform(drawBabyFaceMove, begin+11));

        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+20).add(getFrameTransform(drawBabyFaceMove, begin+11));

        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+21).add(getFrameTransform(drawBabyFaceMove, begin+11));

        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin+22).add(getFrameTransform(drawBabyFaceMove, begin+11));
    }

    private void createMamaHandMove(){
        drawMamaHandMove = new ArrayList<>();
        int begin = 19;

        for (int i=0; i<begin; ++i) drawMamaHandMove.add(null);

        // 19 - 22
        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.add(new ArrayList<>());

        // 
        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.get(begin+4).add(makeTranslationMatrix(20, 3));

        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.get(begin+5).add(getFrameTransform(drawMamaHandMove, begin+4));

        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.get(begin+6).add(getFrameTransform(drawMamaHandMove, begin+4));

        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.get(begin+7).add(getFrameTransform(drawMamaHandMove, begin+4));

        // 
        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.get(begin+8).add(makeTranslationMatrix(30, 6));

        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.get(begin+9).add(getFrameTransform(drawMamaHandMove, begin+8));

        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.get(begin+10).add(getFrameTransform(drawMamaHandMove, begin+8));

        //
        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.get(begin+11).add(makeTranslationMatrix(90, 10));
        
        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.get(begin+12).add(getFrameTransform(drawMamaHandMove, begin+11));

        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.get(begin+13).add(getFrameTransform(drawMamaHandMove, begin+11));

        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.get(begin+14).add(getFrameTransform(drawMamaHandMove, begin+11));

        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.get(begin+15).add(getFrameTransform(drawMamaHandMove, begin+11));

        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.get(begin+16).add(getFrameTransform(drawMamaHandMove, begin+11));

        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.get(begin+17).add(getFrameTransform(drawMamaHandMove, begin+11));

        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.get(begin+18).add(getFrameTransform(drawMamaHandMove, begin+11));

        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.get(begin+19).add(getFrameTransform(drawMamaHandMove, begin+11));

        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.get(begin+20).add(getFrameTransform(drawMamaHandMove, begin+11));
        
        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.get(begin+21).add(getFrameTransform(drawMamaHandMove, begin+11));

        drawMamaHandMove.add(new ArrayList<>());
        drawMamaHandMove.get(begin+22).add(getFrameTransform(drawMamaHandMove, begin+11));
    }

    private void createHifiveMove(){
        drawHiFiveMove = new ArrayList<>();
        int begin = 41;

        for (int i=0; i<begin; ++i) drawHiFiveMove.add(null);

        drawHiFiveMove.add(new ArrayList<>());

        //
        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+1).add(makeTranslationMatrix(-20, 0));

        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+2).add(getFrameTransform(drawHiFiveMove, begin+1));

        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+3).add(getFrameTransform(drawHiFiveMove, begin+1));

        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+4).add(getFrameTransform(drawHiFiveMove, begin+1));

        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+5).add(getFrameTransform(drawHiFiveMove, begin+1));
        //
        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+6).add(makeTranslationMatrix(-60, 10));

        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+7).add(getFrameTransform(drawHiFiveMove, begin+6));

        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+8).add(getFrameTransform(drawHiFiveMove, begin+6));
        //
        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+9).add(makeTranslationMatrix(-90, 50));

        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+10).add(getFrameTransform(drawHiFiveMove, begin+9));

        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+11).add(getFrameTransform(drawHiFiveMove, begin+9));

        //
        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+12).add(makeRotationMatrix(20)); 
        drawHiFiveMove.get(begin+12).add(makeTranslationMatrix(-370, 150));

        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+13).add(getFrameTransform(drawHiFiveMove, begin+12));

        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+14).add(getFrameTransform(drawHiFiveMove, begin+12));

        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+15).add(getFrameTransform(drawHiFiveMove, begin+12));
        
        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+16).add(getFrameTransform(drawHiFiveMove, begin+12));

        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+17).add(getFrameTransform(drawHiFiveMove, begin+12));

        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+18).add(getFrameTransform(drawHiFiveMove, begin+12));

        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+19).add(getFrameTransform(drawHiFiveMove, begin+12));

        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+20).add(getFrameTransform(drawHiFiveMove, begin+12));

        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+21).add(getFrameTransform(drawHiFiveMove, begin+12));

        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+22).add(getFrameTransform(drawHiFiveMove, begin+12));

        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+23).add(getFrameTransform(drawHiFiveMove, begin+12));
        
        drawHiFiveMove.add(new ArrayList<>());
        drawHiFiveMove.get(begin+24).add(getFrameTransform(drawHiFiveMove, begin+12));
    }

    private void createMamaFaceMove(){
        drawMamaFaceMove = new ArrayList<>();
        int begin = 41;

        for (int i=0; i<begin; ++i) drawMamaFaceMove.add(null);

        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.add(new ArrayList<>());

        //
        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.get(begin+7).add(makeTranslationMatrix(30, 0));

        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.get(begin+8).add(getFrameTransform(drawMamaFaceMove, begin+7));

        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.get(begin+9).add(getFrameTransform(drawMamaFaceMove, begin+7));

        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.get(begin+10).add(getFrameTransform(drawMamaFaceMove, begin+7));

        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.get(begin+11).add(getFrameTransform(drawMamaFaceMove, begin+7));

        //
        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.get(begin+12).add(makeRotationMatrix(-8));
        drawMamaFaceMove.get(begin+12).add(makeTranslationMatrix(130, -40));

        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.get(begin+13).add(getFrameTransform(drawMamaFaceMove, begin+12));

        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.get(begin+14).add(getFrameTransform(drawMamaFaceMove, begin+12));

        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.get(begin+15).add(getFrameTransform(drawMamaFaceMove, begin+12));

        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.get(begin+16).add(getFrameTransform(drawMamaFaceMove, begin+12));

        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.get(begin+17).add(getFrameTransform(drawMamaFaceMove, begin+12));

        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.get(begin+18).add(getFrameTransform(drawMamaFaceMove, begin+12));

        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.get(begin+19).add(getFrameTransform(drawMamaFaceMove, begin+12));

        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.get(begin+20).add(getFrameTransform(drawMamaFaceMove, begin+12));

        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.get(begin+21).add(getFrameTransform(drawMamaFaceMove, begin+12));

        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.get(begin+22).add(getFrameTransform(drawMamaFaceMove, begin+12));

        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.get(begin+23).add(getFrameTransform(drawMamaFaceMove, begin+12));

        drawMamaFaceMove.add(new ArrayList<>());
        drawMamaFaceMove.get(begin+24).add(getFrameTransform(drawMamaFaceMove, begin+12));
    }
    
    private void createHandMove(){
        drawHandsMove = new ArrayList<>();
        int begin = 0;

        //
        drawHandsMove.add(new ArrayList<>());
        drawHandsMove.get(begin).add(makeScaleMatrix(4, 4));
        drawHandsMove.get(begin).add(makeTranslationMatrix(-250, -150));
        
        drawHandsMove.add(new ArrayList<>());
        drawHandsMove.get(begin+1).add(getFrameTransform(drawHandsMove, begin));

        drawHandsMove.add(new ArrayList<>());
        drawHandsMove.get(begin+2).add(getFrameTransform(drawHandsMove, begin));

        //
        drawHandsMove.add(new ArrayList<>());
        drawHandsMove.get(begin+3).add(makeScaleMatrix(3.5, 3.5));
        drawHandsMove.get(begin+3).add(makeTranslationMatrix(-240, -150));

        drawHandsMove.add(new ArrayList<>());
        drawHandsMove.get(begin+4).add(getFrameTransform(drawHandsMove, begin+3));

        //
        drawHandsMove.add(new ArrayList<>());
        drawHandsMove.get(begin+5).add(makeScaleMatrix(3, 3));
        drawHandsMove.get(begin+5).add(makeTranslationMatrix(-230, -145));

        drawHandsMove.add(new ArrayList<>());
        drawHandsMove.get(begin+6).add(getFrameTransform(drawHandsMove, begin+5));

        // 
        drawHandsMove.add(new ArrayList<>());
        drawHandsMove.get(begin+7).add(makeScaleMatrix(2.5, 2.5));
        drawHandsMove.get(begin+7).add(makeTranslationMatrix(-220, -135));

        drawHandsMove.add(new ArrayList<>());
        drawHandsMove.get(begin+8).add(getFrameTransform(drawHandsMove, begin+7));

        // 
        drawHandsMove.add(new ArrayList<>());
        drawHandsMove.get(begin+9).add(makeScaleMatrix(2.5, 2.5));
        drawHandsMove.get(begin+9).add(makeTranslationMatrix(-210, -135));

        drawHandsMove.add(new ArrayList<>());
        drawHandsMove.get(begin+10).add(getFrameTransform(drawHandsMove, begin+9));

        // 
        drawHandsMove.add(new ArrayList<>());
        drawHandsMove.get(begin+11).add(makeScaleMatrix(2.5, 2.5));
        drawHandsMove.get(begin+11).add(makeTranslationMatrix(-200, -135));

        drawHandsMove.add(new ArrayList<>());
        drawHandsMove.get(begin+12).add(getFrameTransform(drawHandsMove, begin+11));

        // 
        drawHandsMove.add(new ArrayList<>());
        drawHandsMove.get(begin+13).add(makeScaleMatrix(2, 2));
        drawHandsMove.get(begin+13).add(makeTranslationMatrix(-170, -125));

        drawHandsMove.add(new ArrayList<>());
        drawHandsMove.get(begin+14).add(getFrameTransform(drawHandsMove, begin+13));

        drawHandsMove.add(new ArrayList<>());
        drawHandsMove.get(begin+15).add(getFrameTransform(drawHandsMove, begin+13));

        drawHandsMove.add(new ArrayList<>());
        drawHandsMove.get(begin+16).add(getFrameTransform(drawHandsMove, begin+13));

        drawHandsMove.add(new ArrayList<>());
        drawHandsMove.get(begin+17).add(getFrameTransform(drawHandsMove, begin+13));

        drawHandsMove.add(new ArrayList<>());
        drawHandsMove.get(begin+18).add(getFrameTransform(drawHandsMove, begin+13));
        
    }
    private double sin(double x) { return Math.sin(x); }
    private double cos(double x) { return Math.cos(x); }
}