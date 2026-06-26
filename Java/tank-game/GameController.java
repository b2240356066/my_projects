
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import java.util.*;

public class GameController {
    private Canvas canvas;
    private GraphicsContext gc;
    private MyTank playerTank;                   // The player controlled tank
    private List<Wall> walls = new ArrayList<>(); // Walls on the map
    private List<Bullet> bullets = new ArrayList<>(); // Bullets currently active
    private List<EnemyTank> enemies = new ArrayList<>(); // List of enemy tanks
    private List<Explosion> explosions = new ArrayList<>(); // Explosions currently on screen
    private Set<KeyCode> keysPressed = new HashSet<>(); // Tracks keys currently pressed by player
    private long lastEnemySpawnTime;               // Tracks last enemy spawn timestamp
    private Random random = new Random();           // Random number generator for enemy spawn/direction etc.
    private int score = 0;                          // Player's score
    private int lives = 3;                          // Player lives count
    private boolean gameOver = false;               // Flag to track if game is over
    private boolean paused = false;                  // Flag to track if game is paused
    private boolean waitingToRespawn = false;       // True if player tank is waiting to respawn
    private double respawnTimer = 0;                 // Countdown timer for respawn delay


    /**
     * Constructor - sets up the game with the given Scene and Canvas.
     * Initializes player tank, walls, key handlers, and starts the game loop.
     */
    public GameController(Scene scene, Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();

        // Initialize player tank at starting position
        playerTank = new MyTank(700, 800, this);

        //Create map walls
        createWalls();

        // Set up key press event handling
        scene.setOnKeyPressed(e -> {
            keysPressed.add(e.getCode()); // Track pressed keys
            if (e.getCode() == KeyCode.X && !gameOver && !paused) playerTank.shoot(); // Shoot bullet on X key if game active
            if (e.getCode() == KeyCode.R && (gameOver||paused)) restart(); // Restart game on R if game over or paused
            if (e.getCode() == KeyCode.P && !gameOver) paused = !paused; // Toggle pause on P key if game not over
            if (e.getCode() == KeyCode.ESCAPE) System.exit(0);  // Exit game on ESC key
        });

        // Remove key from pressed set on key release
        scene.setOnKeyReleased(e -> keysPressed.remove(e.getCode()));

        // Main game loop using AnimationTimer
        new AnimationTimer() {
            long lastTime = System.nanoTime();
            @Override
            public void handle(long now) {
                double dt = (now - lastTime) / 1e9; // Calculate delta time in seconds
                lastTime = now;

                // Update game state only if not paused or game over
                if (!paused && !gameOver) update(dt);

                // Render current game frame
                render();
            }
        }.start();
    }

    /**
     * Update method called every frame when game is running.
     * Updates all game objects and handles spawning and respawn logic.
     */
    private void update(double dt) {
        // Handle delayed player respawn
        if (waitingToRespawn) {
            respawnTimer -= dt;

            if (respawnTimer <= 0) {
                playerTank = new MyTank(700, 800, this); // Respawn
                waitingToRespawn = false;
            }
        }

        // Update player if alive
        if (playerTank != null) {
            playerTank.update(dt, keysPressed);
        }

        // Update bullets
        bullets.removeIf(b -> {
            b.update(dt);
            return !b.isActive();
        });

        // Update enemies
        for (EnemyTank e : enemies) {
            e.update(dt);
        }

        // ✅ Check collision between player and enemy tanks
        if (playerTank != null) {
            for (EnemyTank enemy : new ArrayList<>(enemies)) {
                if (enemy.getBounds().intersects(playerTank.getBounds())) {
                    // Explosions for both
                    explosions.add(new Explosion(playerTank.getX(), playerTank.getY(), "/explosion.png", 40, 40));
                    explosions.add(new Explosion(enemy.getX(), enemy.getY(), "/explosion.png", 40, 40));

                    enemies.remove(enemy); // remove enemy
                    playerHit();           // handle player damage
                    break;                 // stop after first collision
                }
            }
        }

        // Spawn new enemies at interval
        if (System.nanoTime() - lastEnemySpawnTime > 2_000_000_000L) {
            spawnEnemy();
            lastEnemySpawnTime = System.nanoTime();
        }

        // Remove dead explosions
        explosions.removeIf(e -> !e.isAlive());
    }


    /**
     * Render all game elements to the canvas.
     * Draws background, player, enemies, bullets, walls, explosions, and HUD.
     */
    private void render() {
        // Clear screen with black background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw player tank only if it's not null
        if (playerTank != null) {
            playerTank.draw(gc);
        }

        //Draw everything
        for (Wall wall : walls) wall.draw(gc);
        for (Bullet b : bullets) b.draw(gc);
        for (EnemyTank e : enemies) e.draw(gc);
        for (Explosion ex : explosions) ex.draw(gc);

        // HUD - Score and Lives
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Arial", 20));
        gc.fillText("Score: " + score, 40, 60);
        gc.fillText("Lives: " + lives, 40, 80);

        // Pause Message
        if (paused) {
            gc.setFill(Color.YELLOW);
            gc.setFont(javafx.scene.text.Font.font("Arial", 30));
            gc.fillText("PAUSED - Press 'P' to resume", 500, 400);
        }

        // Game Over Message
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(javafx.scene.text.Font.font("Arial", 40));
            gc.fillText("GAME OVER!", canvas.getWidth() / 2 - 150, canvas.getHeight() / 2 - 40);

            gc.setFont(javafx.scene.text.Font.font("Arial", 40));
            gc.fillText("Your Score: " + score, canvas.getWidth() / 2 - 170, canvas.getHeight() / 2 + 20);

            gc.setFont(javafx.scene.text.Font.font("Arial", 30));
            gc.fillText("Press R to restart! or Press ESC to escape!", canvas.getWidth() / 2 - 300, canvas.getHeight() / 2 + 100);
        }
    }


    /**
     * Creates the walls around the edges and some inner walls.
     * Walls are positioned in a grid with size 40x40 pixels.
     */
    private void createWalls() {
        // Top and bottom boundary walls
        for (int i = 0; i < 1500; i += 40) {
            walls.add(new Wall(i, 0, 40, 40)); // Top
            walls.add(new Wall(i, 960, 40, 40)); // Bottom (1000 - 40)
        }
        // Left and right boundary walls
        for (int i = 40; i < 960; i += 40) {
            walls.add(new Wall(0, i, 40, 40)); // Left
            walls.add(new Wall(1460, i, 40, 40)); // Right (1500 - 40)
        }

        // Optional horizontal inner walls
        walls.add(new Wall(500, 480, 40, 40));
        walls.add(new Wall(540, 480, 40, 40));
        walls.add(new Wall(580, 480, 40, 40));
        walls.add(new Wall(620, 480, 40, 40));
        walls.add(new Wall(660, 480, 40, 40));
        walls.add(new Wall(700, 480, 40, 40));
        walls.add(new Wall(740, 480, 40, 40));
        walls.add(new Wall(780, 480, 40, 40));
        walls.add(new Wall(820, 480, 40, 40));
        walls.add(new Wall(860, 480, 40, 40));
        walls.add(new Wall(900, 480, 40, 40));
        walls.add(new Wall(940, 480, 40, 40));

        //Optional vertical inner walls
        walls.add(new Wall(1100, 720, 40, 40));
        walls.add(new Wall(1100, 680, 40, 40));
        walls.add(new Wall(1100, 640, 40, 40));
        walls.add(new Wall(1100, 600, 40, 40));
        walls.add(new Wall(1100, 560, 40, 40));
        walls.add(new Wall(1100, 520, 40, 40));
        walls.add(new Wall(1100, 480, 40, 40));
        walls.add(new Wall(1100, 440, 40, 40));
        walls.add(new Wall(1100, 400, 40, 40));
        walls.add(new Wall(1100, 360, 40, 40));
        walls.add(new Wall(1100, 320, 40, 40));
        walls.add(new Wall(1100, 280, 40, 40));
        walls.add(new Wall(1100, 240, 40, 40));


        walls.add(new Wall(300, 720, 40, 40));
        walls.add(new Wall(300, 680, 40, 40));
        walls.add(new Wall(300, 640, 40, 40));
        walls.add(new Wall(300, 600, 40, 40));
        walls.add(new Wall(300, 560, 40, 40));
        walls.add(new Wall(300, 520, 40, 40));
        walls.add(new Wall(300, 480, 40, 40));
        walls.add(new Wall(300, 440, 40, 40));
        walls.add(new Wall(300, 400, 40, 40));
        walls.add(new Wall(300, 360, 40, 40));
        walls.add(new Wall(300, 320, 40, 40));
        walls.add(new Wall(300, 280, 40, 40));
        walls.add(new Wall(300, 240, 40, 40));

    }


    /**
     * Spawns a new enemy tank at a random X position near the top of the screen.
     */
    private void spawnEnemy() {
        double x = 40 + random.nextInt(15) * 40;
        enemies.add(new EnemyTank(x, 40, this));
    }

    public void addBullet(Bullet b) {

        bullets.add(b);
    }

    public void addSmallExplosion(double x, double y) {
        explosions.add(new Explosion(x, y, "/smallExplosion.png", 20, 20));
    }

    /**
     * Handles what happens when an enemy tank is hit.
     * Removes enemy, increases score, and creates an explosion effect.
     * @param e The enemy tank hit
     */
    public void enemyHit(EnemyTank e) {
        explosions.add(new Explosion(e.getX(), e.getY(), "/explosion.png", 40, 40));
        enemies.remove(e);
        score=score+100;
    }

    /**
     * Handles what happens when the player tank is hit.
     * Reduces lives, triggers explosion, and respawns player after delay.
     * Ends game if no lives left.
     */
    public void playerHit() {
        explosions.add(new Explosion(playerTank.getX(), playerTank.getY(), "/explosion.png", 40, 40));
        lives--;

        if (lives > 0) {
            waitingToRespawn = true;
            respawnTimer = 2.0; // 2 seconds
            playerTank = null; // Remove the tank temporarily
        } else {
            gameOver = true;
        }
    }

    /**
     * Restarts the game by resetting all game data.
     */
    public void restart() {
        playerTank = new MyTank(700, 800, this);
        bullets.clear();
        enemies.clear();
        explosions.clear();
        score = 0;
        lives = 3;
        gameOver = false;
        paused = false;
    }

    /**
     * Returns the current player tank instance.
     * @return Player tank or null if dead
     */
    public MyTank getPlayerTank() {
        return playerTank;
    }

    /**
     * Returns a list of walls for collision detection.
     * @return List of walls in the game
     */
    public List<Wall> getWalls() {
        return walls;
    }

    /**
     * Returns a list of enemy tanks.
     * @return List of enemy tanks
     */
    public List<EnemyTank> getEnemyTanks() {
        return enemies;
    }
}
