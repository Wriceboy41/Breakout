import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.program.GraphicsProgram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

import svu.csc213.Dialog;

public class Breakout extends GraphicsProgram {

    /*make a lives variable and have it reset somehow, and it shows them
    make a lives variable that shows up in init and on screen.
    all bricks only take one hit

    make it so number of bricks broken is shown

    brick powerups
    more than one level
    * */







    private Ball ball;

    private Paddle paddle;

    private int lives = 3;
    private GLabel life = new GLabel("lives:" + lives );

    private int brokeBricksCount = 0;
    private GLabel brokeBrick = new GLabel("bricks broken:" + brokeBricksCount);

    private int numBricksInRow;
    private Color[] rowColors = {Color.red, Color.red, Color.green, Color.green, Color.orange, Color.orange, Color.blue, Color.blue, Color.black, Color.black};


    private Random rand;
    private int powerup;

    private int t;


    @Override
    public void init(){

        add(life, 50, 13);
        add(brokeBrick, 100, 13);

        numBricksInRow = (int) (getWidth()/(Brick.WIDTH+ 5.0));

        rand = new Random();


        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < numBricksInRow; col++) {
                Brick brick = new Brick(10 + col * (Brick.WIDTH + 5), Brick.HEIGHT + row * (Brick.HEIGHT + 5), rowColors[row], row, powerup);
                powerup  = rand.nextInt(20);
                if (powerup == 0){
                    brick.setFillColor(Color.WHITE);

                }
                add(brick);
            }
        }

        ball = new Ball(getWidth()/2, 350, 10, this.getGCanvas());
       add(ball);



       paddle = new Paddle(230, 430, 50,10);
       add(paddle);


    }

    @Override
    public void run(){
        addMouseListeners();
        waitForClick();
        gameLoop();
    }

    @Override
    public void mouseMoved(MouseEvent me){
        //make sure that the paddle doesn't go offscreen
        if((me.getX() < getWidth()- ball.getWidth()/2)&&(me.getX() > getX())){
            paddle.setLocation(me.getX()-paddle.getWidth()/2, paddle.getY());
        }
    }


    private void gameLoop(){
        int t = 0;
        while(t == 0){
            //move the ball
            ball.handleMove();
            // handle collisions
            handleCollisions();
            //handle losing the ball
            if(ball.lost){

                    handleLoss();
            }
            if(lives == 0){
                t++;
                Dialog.showMessage("restart to restart retard");
            }
            pause(5);
        }
    }


    private void handleCollisions(){
       // obj can store what we hit
        GObject obj = null;

        // check if the ball is about to hit something
        if(obj == null){
            //check to the top right corner
            obj = this.getElementAt(ball.getX()+ball.getWidth(), ball.getY());
        }

        if(obj == null){
            //check to the top left corner
            obj = this.getElementAt(ball.getX(), ball.getY());
        }

        if(obj == null){
            //check to the bottom right corner
            obj = this.getElementAt(ball.getX()+ball.getWidth(), ball.getY()+ ball.getWidth());
        }

        if(obj == null){
            //check to the bottom left corner
            obj = this.getElementAt(ball.getX(), ball.getY()+ball.getWidth());
        }
        //if by end of the method obj is still null, we hit nothing
        // see if hit something
        if(obj != null) {
            //see what we hit
            if (obj instanceof Paddle) {

                if (ball.getX() < (paddle.getX() + (paddle.getWidth() * .2))) {
                    //left ide
                    ball.bounceLeft();
                } else if (ball.getX() > (paddle.getX() + (paddle.getWidth() * .8))) {
                    //right side
                    ball.bounceRight();
                } else {
                    //middle
                    ball.bounce();
                }
            }


            if (obj instanceof Brick) {
                //bounce
                ball.bounce();
                //destroy brick or decrease life
                if (((Brick) obj).getFillColor() == Color.red) {
                    ((Brick) obj).setFillColor(Color.green);
                } else if (((Brick) obj).getFillColor() == Color.green) {
                    ((Brick) obj).setFillColor(Color.orange);
                } else if (((Brick) obj).getFillColor() == Color.orange) {
                    ((Brick) obj).setFillColor(Color.blue);
                } else if (((Brick) obj).getFillColor() == Color.blue) {
                    ((Brick) obj).setFillColor(Color.black);
                } else if (((Brick) obj).getFillColor() == Color.WHITE) {
                    if (rand.nextInt(5) == 0) {
                        paddle.setBounds(paddle.getX(), paddle.getY(), paddle.getWidth() + 30, paddle.getHeight());
                    } else {
                        lives++;
                        life.setLabel("lives:" + lives);
                    }
                    this.remove(obj);
                    brokeBricksCount++;
                    brokeBrick.setLabel("broke bricks:" + brokeBricksCount);
                }
             else {
                this.remove(obj);
                brokeBricksCount++;
                brokeBrick.setLabel("broke bricks:" + brokeBricksCount);
            }
        }
        }


        }


    private void handleLoss(){
        ball.lost = false;
        lives--;
        life.setLabel("lives:" + lives);
        reset();
    }

    private void reset(){
        ball.setLocation(getWidth()/2, 350);
        paddle.setLocation(230, 430);
        waitForClick();
    }


    public static void main(String[] args) {
        new Breakout().start();
    }

}
