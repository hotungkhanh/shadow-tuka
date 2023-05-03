import bagel.Image;
import bagel.util.Point;
import java.util.ArrayList;

public abstract class Ghost extends MovingEntity {
    final static Image GHOST_FRENZY_IMAGE = new Image("res/ghostFrenzy.png");
    public final static int FRENZY_SCORE = 30;
    public final static int DOWN = 0;
    public final static int RIGHT = 1;
    public final static int UP = 2;
    public final static int LEFT = 3;

    private int direction;

    private final static double FRENZY_SPEED_DECREASE = 0.5;
    private boolean eaten = false;

    public Ghost(Point topLeft, double speed) {
        super(topLeft, speed, speed - FRENZY_SPEED_DECREASE);
        setPosition(topLeft);
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean checkCollision(ArrayList<Wall> walls) {
        boolean colliding = false;
        for (Wall wall : walls) {
            if (wall.collidesWith(this)) {
                colliding = true;
                changeDirection();
                break;
            }
        }
        return colliding;
    }

    public abstract void changeDirection();

    public void move(ArrayList<Wall> walls, boolean frenzyMode) {
        if (direction == LEFT) {
            goLeft(walls, frenzyMode);
        } else if (direction == RIGHT) {
            goRight(walls, frenzyMode);
        } else if (direction == UP) {
            goUp(walls, frenzyMode);
        } else {
            goDown(walls, frenzyMode);
        }
    }

    /**
     * Draws the ghost image at the
     * coordinate of the ghost Rectangle
     */
    public void draw(boolean frenzyMode) {
        if (frenzyMode) {
            GHOST_FRENZY_IMAGE.drawFromTopLeft(getPosition().x, getPosition().y);
        } else {
            draw();
        }
    }

    public boolean isEaten() {
        return eaten;
    }
    public void setEaten(boolean eaten) {
        this.eaten = eaten;
    }
}

