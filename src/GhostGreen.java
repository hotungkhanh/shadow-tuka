import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class GhostGreen extends Ghost {
    private final static Image GHOST_GREEN_IMAGE = new Image("res/ghostGreen.png");
    private final static double SPEED = 4;

    private final Random rand = new Random();
    // randomly generation direction between DOWN and RIGHT
    private int direction = rand.nextInt(2);
    public GhostGreen(Point topLeft) {
        super(GHOST_GREEN_IMAGE, topLeft, SPEED);
    }

    public void move(ArrayList<Wall> walls, boolean frenzyMode) {
        double curSpeed;
        if (frenzyMode) {
            curSpeed = getFrenzySpeed();
        } else {
            curSpeed = getSpeed();
        }
        if (direction == LEFT) {
            pointGo = new Point(getPosition().x - curSpeed, getPosition().y);
        } else if (direction == RIGHT) {
            pointGo = new Point(getPosition().x + curSpeed, getPosition().y);
        } else if (direction == UP) {
            pointGo = new Point(getPosition().x, getPosition().y - curSpeed);
        } else {
            // down
            pointGo = new Point(getPosition().x, getPosition().y + curSpeed);
        }
        this.setRectangle(new Rectangle(pointGo, GHOST_GREEN_IMAGE.getWidth(), GHOST_GREEN_IMAGE.getHeight()));

        boolean colliding = false;
        for (Wall wall : walls) {
            if (wall.collidesWith(this)) {
                this.setRectangle(new Rectangle(getPosition(), GHOST_GREEN_IMAGE.getWidth(), GHOST_GREEN_IMAGE.getHeight()));
                this.changeDirection();
                colliding = true;
                break;
            }
        }
        if (!colliding) {
            setPosition(pointGo);
        }
    }

    public void changeDirection() {
        if (direction == RIGHT) {
            direction = LEFT;
        } else if (direction == LEFT) {
            direction = RIGHT;
        } else if (direction == DOWN) {
            direction = UP;
        } else {
            direction = DOWN;
        }
    }
}
