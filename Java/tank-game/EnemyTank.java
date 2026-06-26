import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.SnapshotParameters;
import javafx.scene.paint.Color;

import java.util.Random;


/**
 * Represents an enemy tank in the game.
 * The tank moves randomly, changes direction at intervals,
 * shoots bullets periodically, and animates its treads when moving.
 */

public class EnemyTank {
    private double x, y;
    private Direction direction;
    private Image image1;
    private Image image2;
    private Image currentImage;
    private GameController game;
    private double speed = 100;
    private double shootCooldown = 0;
    private double changeDirCooldown = 0;
    private Random random = new Random();

    private boolean toggle = false;
    private int frameCounter = 0;

    /**
     * Creates an enemy tank at the specified position.
     *
     * @param x the initial x-coordinate of the tank
     * @param y the initial y-coordinate of the tank
     * @param game the game controller to interact with the game state
     */
    public EnemyTank(double x, double y, GameController game) {
        this.x = x;
        this.y = y;
        this.game = game;
        this.direction = Direction.values()[random.nextInt(4)];

        // Load images for tank animation (two frames for treads)
        Image notTurned1 =new Image(getClass().getResourceAsStream("/whiteTank1.png"));
        Image notTurned2 = new Image(getClass().getResourceAsStream("/whiteTank2.png"));
        this.image1 = rotate90CounterClockwise(notTurned1);
        this.image2 = rotate90CounterClockwise(notTurned2);
        this.currentImage = image1;
    }

    /**
     * Updates the tank's position, shooting, animation, and direction.
     * - Moves the tank based on its current direction and speed.
     * - Prevents moving through walls by reverting position and changing direction.
     * - Animates the tank's treads when moving.
     * - Shoots bullets at random intervals.
     * - Changes direction randomly after a cooldown.
     *
     * @param dt the time elapsed (in seconds) since the last update
     */
    public void update(double dt) {
        double oldX = x, oldY = y;
        boolean moved = false;

        switch (direction) {
            case UP: y -= speed * dt; moved = true; break;
            case DOWN: y += speed * dt; moved = true; break;
            case LEFT: x -= speed * dt; moved = true; break;
            case RIGHT: x += speed * dt; moved = true; break;
        }

        // Check collision with walls, revert if collided and change direction
        for (Wall wall : game.getWalls()) {
            if (wall.intersects(x, y, 40, 40)) {
                x = oldX;
                y = oldY;
                direction = Direction.values()[random.nextInt(4)];
                moved = false;
                break;
            }
        }

        // Animate tank treads if moving
        if (moved) {
            frameCounter++;
            if (frameCounter >= 10) {
                toggle = !toggle;
                currentImage = toggle ? image1 : image2;
                frameCounter = 0;
            }
        }

        // Shooting logic
        shootCooldown -= dt;
        if (shootCooldown <= 0) {
            game.addBullet(new Bullet(x + 16, y + 16, direction, game, false));
            shootCooldown = 1 + random.nextDouble(); // random fire interval
        }

        // Random direction change
        changeDirCooldown -= dt;
        if (changeDirCooldown <= 0) {
            direction = Direction.values()[random.nextInt(4)];
            changeDirCooldown = 2 + random.nextDouble();
        }
    }

    /**
     * Draws the enemy tank with rotation corresponding to its current direction.
     *
     * @param gc the GraphicsContext to draw the tank on
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
     * Returns the current x-coordinate of the tank.
     *
     * @return the x-coordinate
     */

    public double getX() {
        return x; }

    /**
     * Returns the current y-coordinate of the tank.
     *
     * @return the y-coordinate
     */
    public double getY() {
        return y; }


    /**
     * Returns the bounding rectangle of the tank used for collision detection.
     *
     * @return the bounding rectangle with width and height of 40
     */
    public java.awt.Rectangle getBounds() {
        return new java.awt.Rectangle((int)x, (int)y, 40, 40);

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
