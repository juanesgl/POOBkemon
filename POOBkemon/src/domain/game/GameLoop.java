package domain.game;


/**
 * GameLoop class that manages the game loop for a game.
 * It handles the timing for updates and rendering, as well as pausing and resuming the game.
 * The loop runs at a target frame rate of 30 FPS.
 */

public class GameLoop implements Runnable {
    private final Game game;
    private boolean running;
    private static final int TARGET_FPS = 30;
    private static final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
    private boolean paused;
    private Thread gameThread;
    private int fps;
    private long lastFpsTime;
    private int frameCount;

  /*     * Constructor for the GameLoop class.
     * Initializes the game loop with the given game instance.
     * Sets the initial state of the loop to not running and not paused.
     * Initializes FPS tracking variables.
     *
     * @param game The game instance to be managed by this loop.
     */

    public GameLoop(Game game) {
        this.game = game;
        this.running = false;
        this.paused = false;
        this.fps = 0;
        this.lastFpsTime = System.nanoTime();
        this.frameCount = 0;
    }

    /*
     * Starts the game loop thread.
     */

    public void start() {
        if (running) return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    /*
     * Stops the game loop thread.
     */

    public void stop() {
        running = false;
        try {
            if (gameThread != null) {
                gameThread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /*
     * Pauses the game loop.
     */

    public void pause() {
        paused = true;
    }

    /*
     * Resumes the game loop.
     */

    public void resume() {
        paused = false;
    }

    /*  
     * Runs the game loop.
     */

    @Override
    public void run() {
        long lastUpdateTime = System.nanoTime();
        double unprocessedTime = 0;
        double unprocessedRenderTime = 0;

        while (running) {
            if (!paused) {
                long currentTime = System.nanoTime();
                long updateTime = currentTime - lastUpdateTime;
                lastUpdateTime = currentTime;

                unprocessedTime += updateTime;
                unprocessedRenderTime += updateTime;


                while (unprocessedTime >= OPTIMAL_TIME) {
                    game.update();
                    unprocessedTime -= OPTIMAL_TIME;
                }

                if (unprocessedRenderTime >= OPTIMAL_TIME) {
                    game.render();
                    unprocessedRenderTime = 0;
                    
                    frameCount++;
                    if (currentTime - lastFpsTime >= 1000000000) { // 1 second
                        fps = frameCount;
                        frameCount = 0;
                        lastFpsTime = currentTime;
                        game.setFPS(fps);
                    }
                }

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}

