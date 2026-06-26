import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Represents an explosion animation effect at a specific location
 * in the game. The explosion lasts for a fixed duration and is
 * rendered as an image on the canvas.
 */
public class Explosion {
    private double x, y;
    private long startTime;
    private static final long DURATION = 600_000_000; // 0.6 seconds
    private Image image;
    private int width, height;

    /**
     * Creates a new Explosion at the specified coordinates with the given image and size.
     * @param x x-coordinate of the explosion
     * @param y y-coordinate of the explosion
     * @param path the resource path to the explosion image
     * @param width the width to draw the explosion image
     * @param height the height to draw the explosion image
     */
    public Explosion(double x, double y, String path, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startTime = System.nanoTime();
        this.image = new Image(getClass().getResourceAsStream(path));
    }

    /**
     * Checks whether the explosion is still active (within its duration).
     *
     * @return true if the explosion is still visible; false if it has finished
     */
    public boolean isAlive() {
        return System.nanoTime() - startTime < DURATION;
    }

    /**
     * Draws the explosion image on the given GraphicsContext, if it is still active.
     *
     * @param gc the GraphicsContext to draw on
     */
    public void draw(GraphicsContext gc) {
        if (isAlive()) {
            gc.drawImage(image, x, y, width, height);
        }
    }
}
