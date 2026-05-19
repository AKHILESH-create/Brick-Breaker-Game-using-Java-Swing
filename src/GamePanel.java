import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;

    private int score = 0;
    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;

    private int ballPosX = 120;
    private int ballPosY = 350;

    private int ballXDir = -1;
    private int ballYDir = -2;

    private MapGenerator map;

    public GamePanel() {

        map = new MapGenerator(3,7);

        addKeyListener(this);

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        timer = new Timer(delay,this);
        timer.start();
    }

    public void paint(Graphics g){

        // background
        g.setColor(Color.black);
        g.fillRect(1,1,692,592);

        // bricks
        map.draw((Graphics2D)g);

        // borders
        g.setColor(Color.yellow);

        g.fillRect(0,0,3,592);
        g.fillRect(0,0,692,3);
        g.fillRect(691,0,3,592);

        // score
        g.setColor(Color.white);
        g.setFont(new Font("serif",Font.BOLD,25));
        g.drawString("Score : "+score,500,30);

        // paddle
        g.setColor(Color.green);
        g.fillRect(playerX,550,100,8);

        // ball
        g.setColor(Color.yellow);
        g.fillOval(ballPosX,ballPosY,20,20);

        // WIN
        if(totalBricks <= 0){

            play = false;
            ballXDir = 0;
            ballYDir = 0;

            g.setColor(Color.green);
            g.setFont(new Font("serif",Font.BOLD,30));

            g.drawString("You Won!",250,300);

            g.setFont(new Font("serif",Font.BOLD,20));
            g.drawString("Press Enter to Restart",220,350);
        }

        // GAME OVER
        if(ballPosY > 570){

            play = false;
            ballXDir = 0;
            ballYDir = 0;

            g.setColor(Color.red);
            g.setFont(new Font("serif",Font.BOLD,30));

            g.drawString("Game Over",240,300);

            g.setFont(new Font("serif",Font.BOLD,20));
            g.drawString("Press Enter to Restart",220,350);
        }

        g.dispose();
    }

    public void actionPerformed(ActionEvent e){

        if(play){

            ballPosX += ballXDir;
            ballPosY += ballYDir;

            // paddle collision
            if(new Rectangle(ballPosX,ballPosY,20,20)
                    .intersects(new Rectangle(playerX,550,100,8))){

                ballYDir = -ballYDir;
            }

            A:
            for(int i=0;i<map.map.length;i++){

                for(int j=0;j<map.map[0].length;j++){

                    if(map.map[i][j]>0){

                        int brickX=j*map.brickWidth+80;
                        int brickY=i*map.brickHeight+50;

                        Rectangle rect=
                                new Rectangle(
                                        brickX,
                                        brickY,
                                        map.brickWidth,
                                        map.brickHeight);

                        Rectangle ballRect=
                                new Rectangle(
                                        ballPosX,
                                        ballPosY,
                                        20,
                                        20);

                        if(ballRect.intersects(rect)){

                            map.setBrickValue(0,i,j);

                            totalBricks--;
                            score +=5;

                            ballYDir=-ballYDir;

                            break A;
                        }
                    }
                }
            }

            if(ballPosX<0)
                ballXDir=-ballXDir;

            if(ballPosY<0)
                ballYDir=-ballYDir;

            if(ballPosX>670)
                ballXDir=-ballXDir;
        }

        repaint();
    }

    public void keyPressed(KeyEvent e){

        if(e.getKeyCode()==KeyEvent.VK_RIGHT){

            if(playerX>=600)
                playerX=600;
            else
                moveRight();
        }

        if(e.getKeyCode()==KeyEvent.VK_LEFT){

            if(playerX<10)
                playerX=10;
            else
                moveLeft();
        }

        // Restart
        if(e.getKeyCode()==KeyEvent.VK_ENTER){

            if(!play){

                play=true;

                ballPosX=120;
                ballPosY=350;

                ballXDir=-1;
                ballYDir=-2;

                playerX=310;

                score=0;
                totalBricks=21;

                map=new MapGenerator(3,7);

                repaint();
            }
        }
    }

    public void moveRight(){

        play=true;
        playerX+=20;
    }

    public void moveLeft(){

        play=true;
        playerX-=20;
    }

    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}
}