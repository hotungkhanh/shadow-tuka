import bagel.*;

/**
 * SWEN20003 Project 2B, Semester 1, 2023
 * Tung Khanh Ho
 */
public class ShadowPac extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW PAC";
    private final static String LEVEL_0_FILE = "res/level0.csv";
    private final static String LEVEL_1_FILE = "res/level1.csv";
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");

    private final static String LEVEL_COMPLETE = "LEVEL COMPLETE!";
    private final static String WIN_MESSAGE = "WELL DONE!";
    private final static String LOSE_MESSAGE = "GAME OVER!";

    private final static int TITLE_SCREEN = 0;
    private final static int LEVEL_0 = 1;
    private final static int LEVEL_COMPLETE_SCREEN = 2;
    private final static int INSTRUCTION_1_SCREEN = 3;
    private final static int LEVEL_1 = 4;
    private int screenStatus;
    private boolean gameOver;

    private final static int COMPLETE_MESSAGE_FRAMES = 300;
    private int levelCompleteFrameCount = 0;

    private final static int MAX_SCORE_LVL_1 = 800;

    // Frenzy mode attributes
    private final static int FRENZY_MODE_FRAMES = 1000;
    private boolean frenzyMode = false;
    private int frenzyFrameCount;

    private final Level level0 = new Level(LEVEL_0_FILE);
    private final int numDotLevel0 = level0.getNumDots();
    private final Level level1 = new Level(LEVEL_1_FILE);

    public ShadowPac() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        screenStatus = TITLE_SCREEN;
        gameOver = false;
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowPac game = new ShadowPac();
        game.run();
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        } else {
            BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);

            if (screenStatus == TITLE_SCREEN) {
                if (input.wasPressed(Keys.SPACE)) {
                    screenStatus = LEVEL_0;
                }
                if (input.wasPressed(Keys.W)) {
                    screenStatus = LEVEL_1;
                }
            } else if (screenStatus == INSTRUCTION_1_SCREEN && input.wasPressed(Keys.SPACE)) {
                screenStatus = LEVEL_1;
            } else if (screenStatus == LEVEL_COMPLETE_SCREEN && levelCompleteFrameCount == COMPLETE_MESSAGE_FRAMES) {
                screenStatus = INSTRUCTION_1_SCREEN;
            }

            if (screenStatus == TITLE_SCREEN) {
                Message.titleScreen(GAME_TITLE);
            } else if (screenStatus == LEVEL_COMPLETE_SCREEN) {
                Message.drawMessage(LEVEL_COMPLETE);
                levelCompleteFrameCount++;
            } else if (screenStatus == INSTRUCTION_1_SCREEN) {
                Message.instructionLevel1();
            } else if (screenStatus == LEVEL_0 && level0.getPlayer().wonLevel0(numDotLevel0)) {
                screenStatus = LEVEL_COMPLETE_SCREEN;
            } else if (gameOver) {
                Message.drawMessage(LOSE_MESSAGE);
            } else if (screenStatus == LEVEL_1 && level1.getPlayer().getPlayerScore() >= MAX_SCORE_LVL_1) {
                // player has won
                Message.drawMessage(WIN_MESSAGE);
            } else if (screenStatus == LEVEL_0) {
                playLevel(input, level0, false);
            } else {
                playLevel(input, level1, true);
            }
        }
    }

    private void playLevel(Input input, Level level, boolean ghostsMove) {
        level.playerInput(input, frenzyMode);

        for (Pellet pellet : level.getPellets()) {
            if (pellet.collidesWith(level.getPlayer())) {
                frenzyMode = true;
                frenzyFrameCount = 0;
                level.getPellets().remove(pellet);
                break;
            }
        }
        if (frenzyMode) {
            frenzyFrameCount++;
        }

        if (ghostsMove) {
            for (Ghost ghost : level.getGhosts()) {
                if (!ghost.isEaten()) {
                    ghost.move(level.getWalls(), frenzyMode);
                    if (ghost.collidesWith(level.getPlayer())) {
                        if (frenzyMode) {
                            level.getPlayer().increaseScore(Ghost.FRENZY_SCORE);
                            ghost.setEaten(true);
                        } else {
                            level.getPlayer().loseLife();
                            ghost.resetPosition();
                        }
                    }
                }
            }
        } else {
            for (Ghost ghost : level.getGhosts()) {
                if (ghost.collidesWith(level.getPlayer())) {
                    level.getPlayer().loseLife();
                    break;
                }
            }
        }

        for (Dot dot : level.getDots()) {
            if (dot.collidesWith(level.getPlayer())) {
                level.getPlayer().increaseScore(Dot.POINTS);
                level.getDots().remove(dot);
                break;
            }
        }
        for (Cherry cherry : level.getCherries()) {
            if (cherry.collidesWith(level.getPlayer())) {
                level.getPlayer().increaseScore(Cherry.POINTS);
                level.getCherries().remove(cherry);
                break;
            }
        }

        if (level.getPlayer().hasLost()){
            gameOver = true;
        } else {
            // Player still has more than 0 life:
            // draw player, switch between opening and closing mouth every 15 frames
            level.getPlayer().draw();

            // draw stationary objects on screen
            for (Wall wall : level.getWalls()) {
                wall.draw();
            }
            for (Dot dot : level.getDots()) {
                dot.draw();
            }
            for (Cherry cherry : level.getCherries()) {
                cherry.draw();
            }
            for (Pellet pellet : level.getPellets()) {
                pellet.draw();
            }
            for (Ghost ghost : level.getGhosts()) {
                if (!ghost.isEaten()) {
                    ghost.draw(frenzyMode);
                }
            }

            // draw remaining lives and score
            level.getPlayer().drawLives();
            level.getPlayer().drawScore();

            if (frenzyFrameCount == FRENZY_MODE_FRAMES) {
                frenzyMode = false;
                frenzyFrameCount = 0;
                for (Ghost ghost : level.getGhosts()) {
                    if (ghost.isEaten()) {
                        ghost.resetPosition();
                        ghost.setEaten(false);
                    }
                }
            }

        }
    }
}
