import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Represents a bullet fired by either the player or enemy tanks.
 * The bullet moves in a specified direction with a fixed speed,
 * checks for collisions with walls and tanks, and becomes inactive
 * when it hits an obstacle or moves out of bounds.
 */

public class Bullet {
    private double x, y;
    private double speed = 300;
    private Direction direction;
    private boolean active = true;
    private Image image;
    private GameController game;
    private boolean isPlayerBullet;


    /**
     * Creates a bullet at the given position moving in the specified direction.
     *
     * @param x the initial x-coordinate of the bullet
     * @param y the initial y-coordinate of the bullet
     * @param dir the direction the bullet will travel
     * @param game the GameController instance for accessing game state
     * @param isPlayerBullet true if this bullet was fired by the player; false if by an enemy
     */
    public Bullet(double x, double y, Direction dir, GameController game, boolean isPlayerBullet) {
        this.x = x;
        this.y = y;
        this.direction = dir;
        this.game = game;
        this.isPlayerBullet = isPlayerBullet;
        this.image = new Image(getClass().getResourceAsStream("/bullet.png"));
    }

    /**
     * Updates the bullet's position based on its speed and direction.
     * Checks for collisions with walls, enemy tanks, or the player tank,
     * and deactivates the bullet if necessary.
     *
     * @param dt the delta time (in seconds) since the last update
     */
    public void update(double dt) {
        switch (direction) {
            case UP: y -= speed * dt; break;
            case DOWN: y += speed * dt; break;
            case LEFT: x -= speed * dt; break;
            case RIGHT: x += speed * dt; break;
        }

        // Deactivate if bullet is outside game boundaries
        if (x < 0 || x > 2000 || y < 0 || y > 1500) {
            active = false;
        }

        // Check collision with walls
        for (Wall wall : game.getWalls()) {
            if (wall.intersects(x, y, 8, 8)) {
                active = false;
                game.addSmallExplosion(x, y);
                return;
            }
        }

        if (isPlayerBullet) {
            // Check collision with enemy tanks
            for (EnemyTank e : game.getEnemyTanks()) {
                if (e.getBounds().intersects(x, y, 8, 8)) {
                    active = false;
                    game.enemyHit(e);
                    return;
                }
            }
        } else {
            // Check collision with player tank
            if (game.getPlayerTank() != null &&
                    game.getPlayerTank().getBounds().intersects(x, y, 8, 8)) {
                active = false;
                game.playerHit();
            }
        }
    }

    /**
     * Draws the bullet on the specified GraphicsContext if it is active.
     *
     * @param gc the GraphicsContext to draw the bullet on
     */
    public void draw(GraphicsContext gc) {
        if (active)
            gc.drawImage(image, x, y, 8, 8);
    }

    /**
     * Returns whether the bullet is still active (has not collided or gone off-screen).
     *
     * @return true if the bullet is active; false otherwise
     */
    public boolean isActive() {

        return active;
    }
}
