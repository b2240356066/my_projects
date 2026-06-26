import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import java.util.Set;
import javafx.scene.SnapshotParameters;
import javafx.scene.paint.Color;

/**
 * Represents the player's tank controlled via keyboard input.
 * The tank can move in four directions, animates its treads when moving,
 * shoots bullets (with cooldown), and detects collisions with walls.
 */
public class MyTank {
    private double x, y;
    private Direction direction = Direction.UP;
    private Image image1;
    private Image image2;
    private Image currentImage;
    private GameController game;
    private double speed = 150;
    private boolean toggle = false;
    private int frameCounter = 0;

    private double shootCooldown = 0; // cooldown timer in seconds

    /**
     * Creates the player's tank at the specified position.
     *
     * @param x the initial x-coordinate of the tank
     * @param y the initial y-coordinate of the tank
     * @param game the game controller managing game state
     */
    public MyTank(double x, double y, GameController game) {
        this.x = x;
        this.y = y;
        this.game = game;
        Image notTurned1= new Image(getClass().getResourceAsStream("/yellowTank1.png"));
        Image notTurned2 = new Image(getClass().getResourceAsStream("/yellowTank2.png"));
        this.image1 = rotate90CounterClockwise(notTurned1);
        this.image2= rotate90CounterClockwise(notTurned2);
        this.currentImage = image1;
    }

    /**
     * Updates the tank's position, direction, animation, and cooldown timer.
     *
     * @param dt time elapsed (seconds) since the last update
     * @param keys the set of currently pressed keys
     */
    public void update(double dt, Set<KeyCode> keys) {
        shootCooldown -= dt; // update cooldown
        double oldX = x, oldY = y;
        boolean move = false;

        if (keys.contains(KeyCode.UP)) {
            y -= speed * dt;
            direction = Direction.UP;
            move = true;
        } else if (keys.contains(KeyCode.DOWN)) {
            y += speed * dt;
            direction = Direction.DOWN;
            move = true;
        } else if (keys.contains(KeyCode.LEFT)) {
            x -= speed * dt;
            direction = Direction.LEFT;
            move = true;
        } else if (keys.contains(KeyCode.RIGHT)) {
            x += speed * dt;
            direction = Direction.RIGHT;
            move = true;
        }

        for (Wall wall : game.getWalls()) {
            if (wall.intersects(x, y, 40, 40)) {
                x = oldX;
                y = oldY;
                move = false;
                break;
            }
        }

        if (move) {
            frameCounter++;
            if (frameCounter >= 10) {
                toggle = !toggle;
                currentImage = toggle ? image1 : image2;
                frameCounter = 0;
            }
        }
    }

    /**
     * Draws the tank rotated according to its current direction.
     *
     * @param gc the GraphicsContext to draw on
     */
    public void draw(GraphicsContext gc) {
        gc.save();
        gc.translate(x + 20, y + 20);
        switch (direction) {
            case UP: gc.rotate(0); break;
            case RIGHT: gc.rotate(90); break;
            case DOWN: gc.rotate(180); break;
            case LEFT: gc.rotate(270); break;
        }
        gc.drawImage(currentImage, -20, -20, 40, 40);
        gc.restore();
    }

    /**
     * Fires a bullet if cooldown has expired.
     */
    public void shoot() {
        if (shootCooldown <= 0) {
            game.addBullet(new Bullet(x + 16, y + 16, direction, game, true));
            shootCooldown = 0.50; // reset cooldown to 0.5 second
        }
    }

    /**
     * Returns the current x-coordinate of the tank.
     *
     * @return the x-coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the current y-coordinate of the tank.
     *
     * @return the y-coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the bounding rectangle used for collision detection.
     *
     * @return the bounding rectangle of size 40x40 at the tank's position
     */
    public java.awt.Rectangle getBounds() {
        return new java.awt.Rectangle((int) x, (int) y, 40, 40);
    }

    private Image rotate90CounterClockwise(Image original) {
        double width = original.getWidth();
        double height = original.getHeight();

        // Create a canvas to draw the rotated image (swapping width & height)
        Canvas tempCanvas = new Canvas(height, width);
        GraphicsContext gc = tempCanvas.getGraphicsContext2D();

        // Fill with transparent — default fill may be white!
        gc.setFill(Color.TRANSPARENT);
        gc.clearRect(0, 0, height, width);

        // Rotate and draw the image
        gc.translate(0, height);
        gc.rotate(-90);
        gc.drawImage(original, 0, 0);

        // Ensure snapshot uses transparency
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        WritableImage rotated = new WritableImage((int) height, (int) width);
        tempCanvas.snapshot(params, rotated);
        return rotated;
    }
}
