import bagel.util.Point;
import java.util.ArrayList;

public abstract class MovingEntity extends GameEntity {
    private final double speed;
    private final double frenzySpeed;
    Point startPoint;
    Point pointGo;

    public MovingEntity(Point topLeft, double speed, double frenzySpeed) {
        super(topLeft);
        this.startPoint = topLeft;
        pointGo = new Point(topLeft.x, topLeft.y);
        this.speed = speed;
        this.frenzySpeed = frenzySpeed;
    }

    public double getSpeed() {
        return speed;
    }

    public double getFrenzySpeed() {
        return frenzySpeed;
    }

    public void goLeft(ArrayList<Wall> walls, boolean frenzyMode) {
        if (frenzyMode) {
            pointGo = new Point(getPosition().x - frenzySpeed, getPosition().y);
        } else {
            pointGo = new Point(getPosition().x - speed, getPosition().y);
        }

        if (!checkCollision(walls)) {
            setPosition(pointGo);
        }
        else {
            pointGo = getPosition();
        }
    }

    public void goRight(ArrayList<Wall> walls, boolean frenzyMode) {
        if (frenzyMode) {
            pointGo = new Point(getPosition().x + frenzySpeed, getPosition().y);
        } else {
            pointGo = new Point(getPosition().x + speed, getPosition().y);
        }
        if (!checkCollision(walls)) {
            setPosition(pointGo);
        }
        else {
            pointGo = getPosition();
        }
    }

    public void goUp(ArrayList<Wall> walls, boolean frenzyMode) {
        if (frenzyMode) {
            pointGo = new Point(getPosition().x, getPosition().y - frenzySpeed);
        } else {
            pointGo = new Point(getPosition().x, getPosition().y - speed);
        }
        if (!checkCollision(walls)) {
            setPosition(pointGo);
        }
        else {
            pointGo = getPosition();
        }
    }

    public void goDown(ArrayList<Wall> walls, boolean frenzyMode) {
        if (frenzyMode) {
            pointGo = new Point(getPosition().x, getPosition().y + frenzySpeed);
        } else {
            pointGo = new Point(getPosition().x, getPosition().y + speed);
        }
        if (!checkCollision(walls)) {
            setPosition(pointGo);
        }
        else {
            pointGo = getPosition();
        }
    }

    public abstract boolean checkCollision(ArrayList<Wall> walls);
}
