package domain.game;
/*
 * GameLoop.java
 * 
 */
import presentation.screens.GameScreen;

public class GameLoop implements Runnable {
    private Game game;
    private boolean running;
    private static final int TARGET_FPS = 30;
    private static final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
    private boolean paused;
    private Thread gameThread;
    private int fps;
    private long lastFpsTime;
    private int frameCount;

    /*
     * Constructor for GameLoop class.
     * 
     * @param gameScreen The game screen instance
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

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    /*  
     * Runs the game loop.
     */
    @Override
    public void run() {
        long lastUpdateTime = System.nanoTime();
        long lastRenderTime = System.nanoTime();
        double unprocessedTime = 0;
        double unprocessedRenderTime = 0;

        while (running) {
            if (!paused) {
                long currentTime = System.nanoTime();
                long updateTime = currentTime - lastUpdateTime;
                lastUpdateTime = currentTime;

                unprocessedTime += updateTime;
                unprocessedRenderTime += updateTime;

                // Update game logic
                while (unprocessedTime >= OPTIMAL_TIME) {
                    game.update();
                    unprocessedTime -= OPTIMAL_TIME;
                }

                // Render game and update FPS
                if (unprocessedRenderTime >= OPTIMAL_TIME) {
                    game.render();
                    unprocessedRenderTime = 0;
                    
                    // Calculate FPS
                    frameCount++;
                    if (currentTime - lastFpsTime >= 1000000000) { // 1 second
                        fps = frameCount;
                        frameCount = 0;
                        lastFpsTime = currentTime;
                        game.setFPS(fps);
                    }
                }

                // Sleep to maintain target FPS
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

