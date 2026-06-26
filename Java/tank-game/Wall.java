import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Represents a wall object in the game environment.
 * The wall is a static obstacle with a fixed position and size,
 * rendered using a wall image.
 */

public class Wall {
    private double x, y;
    private int width, height;
    private Image image;

    /**
     * Constructs a Wall at the specified position with the given dimensions.
     * Loads the wall image resource.
     * @param x the x-coordinate of the wall's top-left corner
     * @param y the y-coordinate of the wall's top-left corner
     * @param width the width of the wall
     * @param height the height of the wall
     */
    public Wall(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = new Image(getClass().getResourceAsStream("/wall.png"));
    }


    /**
     * Draws the wall image on the provided GraphicsContext.
     * @param gc the GraphicsContext on which to draw the wall
     */
    public void draw(GraphicsContext gc) {
        gc.drawImage(image, x, y, width, height);
    }

    /**
     * Checks whether the wall intersects with a rectangular area
     * defined by the given coordinates and size.
     *
     * @param ox the x-coordinate of the other rectangle's top-left corner
     * @param oy the y-coordinate of the other rectangle's top-left corner
     * @param ow the width of the other rectangle
     * @param oh the height of the other rectangle
     * @return true if this wall intersects with the given rectangle; false otherwise
     */
    public boolean intersects(double ox, double oy, double ow, double oh) {
        return x < ox + ow && x + width > ox && y < oy + oh && y + height > oy;
    }

}

