package domain.game;

public class GameLoop implements Runnable {
    private static final int TARGET_FPS = 60;
    private static final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

    private final Game game;
    private boolean running = false;
    private Thread gameThread;

    public GameLoop(Game game) {
        this.game = game;
    }

    public void start() {
        if (running) return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void stop() {
        if (!running) return;
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.nanoTime();
        long lastUpdateTime;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while (running) {           
            lastUpdateTime = System.nanoTime();           
            game.render();
            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                game.setFPS(frames);
                frames = 0;
            }           
            try {
                long sleepTime = (lastUpdateTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
                if (sleepTime > 0) Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
