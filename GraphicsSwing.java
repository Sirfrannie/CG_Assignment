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
    private ArrayList<HashMap<int[], Color>> color;
    // list of transformation matrix
    private ArrayList<ArrayList<double[]>> circleMove;
    private ArrayList<ArrayList<double[]>> drawBabyMove;
    private ArrayList<ArrayList<double[]>> drawBabyFaceMove;
    private ArrayList<ArrayList<double[]>> drawMamaHandMove;
    private ArrayList<ArrayList<double[]>> drawHiFiveMove;
    private ArrayList<ArrayList<double[]>> drawMamaFaceMove;

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

            if ( currentFrame > 32) currentFrame = 0;

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

        g2d.drawImage(buffer, 0, 0, null);
    }

    /* public method for testing with other class */
    public void testTransformation(Graphics2D g){
        AffineTransform old = g.getTransform();
        Point2D origin = new Point2D.Double(300, 300);
        Point2D screenPos;
        AffineTransform transform;
        /* Change Here */
        int f = drawBabyFaceMove.size();
        double transmatrix[];

        for (int i=0; i<f; ++i){
            if ( 1 == 1) {
                /* Change Here */
                if ( drawBabyFaceMove.get(i) == null ) continue;
                /* Change Here */
                transmatrix = getFrameTransform(drawBabyFaceMove, i);
                if (transmatrix == null) continue;
                transform = new AffineTransform(transmatrix);
                g.setTransform(transform);
                screenPos = transform.transform(origin, null);

                System.out.println(Arrays.toString(transmatrix));
                System.out.println(""+i+" painted at (" + screenPos.getX()+", "+screenPos.getY()+")");
                /* Change Here */
                drawBabyFace(g);
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
        // drawMamaHand(g_buf);
        // drawHiFive(g_buf);
        // drawMamaFace(g_buf, 2);


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
        if (currentFrame < circleMove.size()
            && circleMove.get(currentFrame) != null){
            g.setTransform(new AffineTransform(
                getFrameTransform(circleMove, currentFrame)
            ));
            drawBall(g);
            g.setTransform(old);
        }
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
            drawMamaFace(g, 1);
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
    
    private void createBabyMove(){
        drawBabyMove = new ArrayList<>();
        int begin = 0; 

        drawBabyMove.add(null);
        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+1).add(makeScaleMatrix(4, 4));
        drawBabyMove.get(begin+1).add(makeTranslationMatrix(-250, -125));

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+2).add(getFrameTransform(drawBabyMove, begin+1));

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+3).add(makeScaleMatrix(3.5, 3.5));
        drawBabyMove.get(begin+3).add(makeTranslationMatrix(-237, -120)); 

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+4).add(getFrameTransform(drawBabyMove, begin+3));

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+5).add(makeScaleMatrix(3, 3));
        drawBabyMove.get(begin+5).add(makeTranslationMatrix(-220, -115));

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+6).add(getFrameTransform(drawBabyMove, begin+5));

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+7).add(makeScaleMatrix(2.5, 2.5));
        drawBabyMove.get(begin+7).add(makeTranslationMatrix(-197, -103));

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+8).add(getFrameTransform(drawBabyMove, begin+7));

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+9).add(makeScaleMatrix(2, 2));
        drawBabyMove.get(begin+9).add(makeTranslationMatrix(-160, -90));

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+10).add(getFrameTransform(drawBabyMove, begin+9));

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+11).add(makeScaleMatrix(1.5, 1.5));
        drawBabyMove.get(begin+11).add(makeTranslationMatrix(-105, -60));

        drawBabyMove.add(new ArrayList<>());
        drawBabyMove.get(begin+12).add(getFrameTransform(drawBabyMove, begin+11));

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
        int begin = 0;
        
        drawBabyFaceMove.add(new ArrayList<>());
        drawBabyFaceMove.get(begin).add(makeTranslationMatrix(50, 0));

        // drawBabyFaceMove.add(new ArrayList<>());
        // drawBabyFaceMove.get(begin+1).add(makeDefaultMatrix());
    }

    private void createMamaHandMove(){
        drawMamaHandMove = new ArrayList<>();
    }

    private void createHifiveMove(){
        drawHiFiveMove = new ArrayList<>();
    }

    private void createMamaFaceMove(){
        drawMamaFaceMove = new ArrayList<>();
    }

    
    private double sin(double x) { return Math.sin(x); }
    private double cos(double x) { return Math.cos(x); }
}