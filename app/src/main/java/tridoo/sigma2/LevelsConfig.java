package tridoo.sigma2;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class LevelsConfig {

    private static SparseArray<Level> levelsConfig;
    private static List<Integer> levelsBackgrounds;
    private static ArrayList<Integer> tileBackgrounds;

    static {
        levelsConfig = new SparseArray<>();

        addLevel(Level.of(1, 2, 2, 3, 3, 3, 2));
        addLevel(Level.of(2, 3, 3, 4, 6, 9, 2));
        addLevel(Level.of(3, 3, 3, 5, 10, 15, 3));

        addLevel(Level.of(4, 3, 4, 10, 15, 20, 3));
        addLevel(Level.of(5, 4, 4, 15, 25, 40, 3));
        addLevel(Level.of(6, 4, 5, 20, 40, 60, 3));

        addLevel(Level.of(7, 4, 5, 40, 60, 80, 3));
        addLevel(Level.of(8, 4, 6, 60, 80, 120, 3));
        addLevel(Level.of(9, 4, 6, 80, 120, 160, 4));

        addLevel(Level.of(10, 4, 7, 120, 160, 240, 4));
        addLevel(Level.of(11, 4, 7, 160, 240, 320, 4));
        addLevel(Level.of(12, 4, 7, 240, 320, 480, 5));

        addLevel(Level.of(13, 5, 8, 320, 480, 640, 6));
        addLevel(Level.of(14, 5, 8, 480, 640, 960, 6));
        addLevel(Level.of(15, 5, 9, 640, 960, 1200, 6));

        addLevel(Level.of(16, 6, 9, 960, 1200, 1800, 8));
        addLevel(Level.of(17, 6, 10, 1200, 1800, 2400, 8));
        addLevel(Level.of(18, 6, 11, 1800, 2400, 3500, 9));

        addLevel(Level.of(19, 6, 12, 2400, 3500, 4800, 9));
        addLevel(Level.of(20, 6, 13, 3500, 4800, 7000, 10));
        addLevel(Level.of(21, 6, 14, 4800, 7000, 9000, 10));


        levelsBackgrounds = new ArrayList<>();
        levelsBackgrounds.add(R.mipmap.star0);
        levelsBackgrounds.add(R.mipmap.star1);
        levelsBackgrounds.add(R.mipmap.star2);
        levelsBackgrounds.add(R.mipmap.star3);

        tileBackgrounds = new ArrayList<>();
        tileBackgrounds.add(R.drawable.bg_round);
        tileBackgrounds.add(R.drawable.bg_round1);
        //tileBackgrounds.add(R.drawable.bg_round2);
        tileBackgrounds.add(R.drawable.bg_round3);
        //tileBackgrounds.add(R.drawable.bg_round4);
        tileBackgrounds.add(R.drawable.bg_round5);
        //tileBackgrounds.add(R.drawable.bg_round6);
        tileBackgrounds.add(R.drawable.bg_round7);
        //tileBackgrounds.add(R.drawable.bg_round8);
        tileBackgrounds.add(R.drawable.bg_round9);
        //tileBackgrounds.add(R.drawable.bg_round10);
        tileBackgrounds.add(R.drawable.bg_round11);
        //tileBackgrounds.add(R.drawable.bg_round12);
        tileBackgrounds.add(R.drawable.bg_round13);
        //tileBackgrounds.add(R.drawable.bg_round14);
        tileBackgrounds.add(R.drawable.bg_round15);
        //tileBackgrounds.add(R.drawable.bg_round16);
        tileBackgrounds.add(R.drawable.bg_round17);
        //tileBackgrounds.add(R.drawable.bg_round18);
        tileBackgrounds.add(R.drawable.bg_round19);
        //tileBackgrounds.add(R.drawable.bg_round20);
        tileBackgrounds.add(R.drawable.bg_round21);
        tileBackgrounds.add(R.drawable.bg_round22);
    }

    private static void addLevel(Level level) {
        if (levelsConfig.get(level.getLevelNumber()) == null) {
            levelsConfig.append(level.getLevelNumber(), level);
        } else {
            throw new IllegalArgumentException("Level " + level.getLevelNumber() + " allready exists");
        }
    }

    public static Level getLevelConfig(int levelNumber) {
        return levelsConfig.get(levelNumber);
    }

    public static int getLevelsQuantity() {
        return levelsConfig.size();
    }

    public static int getLevelBackground(int rank) {
        return levelsBackgrounds.get(rank);
    }

    public static int getTileBackground(int value) {
        if (value >= tileBackgrounds.size())
            return tileBackgrounds.get(tileBackgrounds.size() - 1);
        return tileBackgrounds.get(value);
    }

    public static boolean isLastLevel(int level) {
        return level == levelsConfig.size();
    }
}