package tridoo.sigma2;

public class Level {
    private final int levelNumber;
    private final int size;
    private final int range;
    private final int rank1;
    private final int rank2;
    private final int rank3;
    private final int randomTilesCount;


    private Level(int levelNumber, int size, int range, int rank1, int rank2, int rank3, int randomTilesCount) {
        this.levelNumber = levelNumber;
        this.size = size;
        this.range = range;
        this.rank1 = rank1;
        this.rank2 = rank2;
        this.rank3 = rank3;
        this.randomTilesCount = randomTilesCount;
    }

    public static Level of(int levelNumber, int size, int range, int rank1, int rank2, int rank3, int randomTilesCount) {
        return new Level(levelNumber, size, range, rank1, rank2, rank3, randomTilesCount);
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public int getSize() {
        return size;
    }

    public int getRange() {
        return range;
    }

    public int getRank1() {
        return rank1;
    }

    public int getRank2() {
        return rank2;
    }

    public int getRank3() {
        return rank3;
    }

    public int getRandomTilesCount() {
        return randomTilesCount;
    }
}
