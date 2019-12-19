package tridoo.sigma2;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class GameActivity extends AppCompatActivity {

    private Map tiles;

    private int maxGeneratedValue;
    private int maxLevelValue;
    private Level levelConfig;
    private int level;
    private int savedRank;
    private int points;

    private boolean isGameActiv;

    private Repository repository;
    private GameScreenController screenController;

    public GameActivity() {
        this.tiles = new HashMap<String, Tile>();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        showAds();

        level = getIntent().getIntExtra("level", 1);
        savedRank = getIntent().getIntExtra("rank", 0);

        levelConfig = LevelsConfig.getLevelConfig(level);
        maxLevelValue = levelConfig.getRange();

        screenController = new GameScreenController(this);
        startGame();
    }


    private void startGame() {
        screenController.setLayGameOverVisibility(false);
        isGameActiv = true;
        maxGeneratedValue = 1;
        randomNextValue(1);

        int boardSize = levelConfig.getSize();
        screenController.generateBoard(boardSize);
        connectNeighbours(boardSize);
        fillRandomTiles(levelConfig.getRandomTilesCount(), boardSize);
    }

    public void restartGame() {
        points = 0;
        screenController.prepareToRestart(points);
        startGame();
    }

    public void nextLevel() {
        level++;
        levelConfig = LevelsConfig.getLevelConfig(level);
        screenController.setProgressBarMax(levelConfig.getRank3());
        maxLevelValue = levelConfig.getRange();
        savedRank = getRepository().getLevelRank(level);
        restartGame();

    }

    public void fillRandomTiles(int amount, int boardSize) {
        Random random = new Random();

        int i = 0;
        do {
            int x = random.nextInt(boardSize);
            int y = random.nextInt(boardSize);
            String position = String.valueOf("" + x + y);
            Tile randomTile = (Tile) tiles.get(position);

            if (randomTile == null || randomTile.getValue() != 0) {
                continue;
            }

            if (getNeighboursWithEqualValue(randomTile, 1).size() < 2) {
                int value = getValueForRandomTile();
                randomTile.setValue(value);
                screenController.setTileBackground(randomTile, value);
                i++;
            }

        } while (i < amount);
    }

    private int getValueForRandomTile() {
        int range = 1;
        if (level > 18) range = 4;
        else if (level > 15) range = 3;
        else if (level > 10) range = 2;

        return randomNextValue(range);
    }

    public void addTileToRegister(Tile newTile) {
        tiles.put(newTile.getPositionAsString(), newTile);
    }

    private void connectNeighbours(int boardSize) {
        Iterator<Map.Entry<String, Tile>> iterator = tiles.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Tile> pair = iterator.next();
            String position = pair.getKey();

            Set<String> neighboursPositions = generateNeighboursPositions(position, boardSize);
            pair.getValue().setNeighbours(getTilesFromPositions(neighboursPositions));
        }
    }

    private Set<String> generateNeighboursPositions(String basePosition, int boardSize) {
        final int[] OFFSETS = {-1, 0, 1};
        int x = Integer.valueOf(basePosition.substring(0, 1));
        int y = Integer.valueOf(basePosition.substring(1));
        Set<String> result = new HashSet<>();

        for (int xOffset : OFFSETS) {
            for (int yOffset : OFFSETS) {
                if ((xOffset != 0 ^ yOffset != 0)
                        && (x + xOffset > -1 && x + xOffset < boardSize)
                        && (y + yOffset > -1 && y + yOffset < boardSize)) {
                    int newX = x + xOffset;
                    int newY = y + yOffset;
                    result.add(String.valueOf("" + newX + newY));
                }
            }
        }
        return result;
    }

    private Set<Tile> getTilesFromPositions(Set<String> positions) {
        Set<Tile> result = new HashSet<>();
        for (String position : positions) {
            result.add((Tile) tiles.get(position));
        }
        return result;
    }

    private Set<Tile> getNeighboursWithEqualValue(Tile tile, int value) {
        Set<Tile> result = new HashSet<>();
        Deque<Tile> tilesToCheck = new ArrayDeque<>();
        Set<Tile> tilesChecked = new HashSet<>();
        tilesToCheck.add(tile);

        Tile testedTile;
        do {
            testedTile = tilesToCheck.poll();

            for (Tile neighbour : testedTile.getNeighbours()) {
                if (neighbour.getValue() == value && !tilesChecked.contains(neighbour)) {
                    tilesToCheck.add(neighbour);
                    result.add(neighbour);
                }
            }
            tilesChecked.add(testedTile);


        } while (!tilesToCheck.isEmpty());
        return result;
    }

    public void processTouchTile(Tile tile) {
        tile.setValue(screenController.getSourceValue());
        boolean foundEnoughNeighbours = false;

        while (getNeighboursWithEqualValue(tile, tile.getValue()).size() > 1) {
            foundEnoughNeighbours = true;
            Set<Tile> equalNeighbours = getNeighboursWithEqualValue(tile, tile.getValue());
            int newPoints = calculatePoints(tile.getValue(), equalNeighbours.size());
            addPoints(newPoints);
            screenController.setProgressBarProgress(points);

            tile.incrementValue();
            setMaxGeneratedValue(tile.getValue());
            screenController.setStars(getRank(points));

            for (Tile neighbour : equalNeighbours) {
                neighbour.setValue(0);
                screenController.setTileBackground(neighbour, 0);
            }
        }

        screenController.setTileBackground(tile, tile.getValue());

        if (isLevelComplete()) {
            isGameActiv = false;
            screenController.levelComplete(level, 3);
            saveScore(points);
        } else {
            int newRandomValue = randomNextValue(maxGeneratedValue);
            setSourceValue(newRandomValue);
        }

        if (!foundEnoughNeighbours && isGameOver()) {
            isGameActiv = false;
            int rank = getRank(points);
            if (rank > 0) {
                screenController.levelComplete(level, rank);
            } else {
                screenController.gameOver();
            }
            saveScore(points);
            savedRank = rank;
        }
    }


    private void setMaxGeneratedValue(int tileValue) {
        if (tileValue <= maxLevelValue && tileValue > maxGeneratedValue) maxGeneratedValue++;
    }

    private void addPoints(int newPoints) {
        points += newPoints;
        screenController.showPoints(points);
    }

    private boolean isLevelComplete() {
        return points >= levelConfig.getRank3();
    }

    private boolean isGameOver() {
        GridLayout gridLayout = findViewById(R.id.grid);
        for (int index = 0; index < gridLayout.getChildCount(); ++index) {
            Tile tile = (Tile) gridLayout.getChildAt(index);
            if (tile.getValue() == 0) return false;
        }
        return true;
    }

    private void setSourceValue(int value) {
        screenController.setSourceValue(value);
    }

    private void saveScore(int points) {
        int rank = getRank(points);
        if (rank > savedRank) getRepository().saveLevelResult(level, rank);
    }

    public int getRank(int points) {
        if (points >= levelConfig.getRank3()) return 3;
        if (points >= levelConfig.getRank2()) return 2;
        if (points >= levelConfig.getRank1()) return 1;
        return 0;
    }

    private Repository getRepository() {
        return repository == null ? new Repository(this) : repository;
    }

    public boolean isGameActiv() {
        return isGameActiv;
    }

    static int calculatePoints(int value, int tilesNumber) {
        return value * (tilesNumber + 1);
    }

    static int randomNextValue(int range) {
        List<Integer> values = new ArrayList<>();

        for (int i = 0; i < range; i++) {
            for (int j = 0; j < range - i; j++) {
                values.add(i + 1);
            }
        }

        int randomNum = ThreadLocalRandom.current().nextInt(0, values.size());
        return values.get(randomNum);
    }

    private void showAds() {
        MobileAds.initialize(this, new OnInitializationCompleteListenerImpl());

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public Level getLevelConfig() {
        return levelConfig;
    }
}
