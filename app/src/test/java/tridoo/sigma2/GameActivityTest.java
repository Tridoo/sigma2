package tridoo.sigma2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class GameActivityTest {

    private GameActivity game;

    @BeforeEach
    void init() {
        game = new GameActivity();

        Class<?> secretClass = game.getClass();
        Field field;
        try {
            field = secretClass.getDeclaredField("levelConfig");
            field.setAccessible(true);
            field.set(game, generateLevelConfig());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getRank() {
        int points = 10;
        int expectedRank = 3;

        int rank = game.getRank(points);
        Assertions.assertEquals(expectedRank, rank);
    }

    @Test
    void getLevelConfig() {
        Level cfg = game.getLevelConfig();
        Assertions.assertEquals(generateLevelConfig().getLevelNumber(), cfg.getLevelNumber());
    }

    private Level generateLevelConfig() {
        return Level.of(1, 1, 1, 2, 4, 8, 0);
    }
}