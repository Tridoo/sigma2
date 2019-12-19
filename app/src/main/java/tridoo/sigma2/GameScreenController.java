package tridoo.sigma2;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.gridlayout.widget.GridLayout;

public class GameScreenController {


    private GameActivity gameActivity;

    private ProgressBar progressBar;
    private TextView source;
    private TextView counter;
    private TextView nextButton;
    private ImageView restartButton;
    private RelativeLayout layGameOver;
    private TextView gameComplete;
    private TextView label;

    private ImageView star1;
    private ImageView star2;
    private ImageView star3;

    private View levelStars;

    private final int fullStar = R.mipmap.star;
    private final int emptyStar = R.mipmap.star_empty;

    public GameScreenController(GameActivity gameActivity) {
        this.gameActivity = gameActivity;

        init();
        initValues();
    }

    private void init() {
        source = gameActivity.findViewById(R.id.source);
        progressBar = gameActivity.findViewById(R.id.progressBar);
        counter = gameActivity.findViewById(R.id.counter);
        nextButton = gameActivity.findViewById(R.id.tv_next);
        restartButton = gameActivity.findViewById(R.id.iv_restart);
        layGameOver = gameActivity.findViewById(R.id.lay_game_over);
        label = gameActivity.findViewById(R.id.tv_label);

        star1 = gameActivity.findViewById(R.id.star1);
        star2 = gameActivity.findViewById(R.id.star2);
        star3 = gameActivity.findViewById(R.id.star3);

        levelStars = gameActivity.findViewById(R.id.iv_stars);
        gameComplete = gameActivity.findViewById(R.id.tv_game_complete);
    }

    private void initValues() {
        progressBar.setMax(gameActivity.getLevelConfig().getRank3());
        restartButton.setOnClickListener(v -> gameActivity.restartGame());
        nextButton.setOnClickListener(v -> gameActivity.nextLevel());
    }


    public void generateBoard(int boardSize) {
        GridLayout gridLayout = gameActivity.findViewById(R.id.grid);
        gridLayout.removeAllViews();
        gridLayout.setColumnCount(boardSize);
        gridLayout.setRowCount(boardSize);

        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                Tile newTile = generateTile(x, y);
                GridLayout.LayoutParams gridParams = generateGridParam(x, y);
                gridLayout.addView(newTile, gridParams);
                setTileLiseners(newTile);
                gameActivity.addTileToRegister(newTile);
            }
        }
        setSourceValue(1);
    }

    private Tile generateTile(int x, int y) {
        Tile newTile = Tile.of(x, y, gameActivity.getApplicationContext());
        newTile.setBackground(ContextCompat.getDrawable(gameActivity, R.drawable.bg_round));
        newTile.setValue(0);
        newTile.setEms(2);
        newTile.setTextColor(Color.BLACK);
        newTile.setTextSize(18f);
        newTile.setGravity(Gravity.CENTER);
        return newTile;
    }

    private GridLayout.LayoutParams generateGridParam(int x, int y) {
        int margin = 5;
        GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams();
        gridParam.setMargins(margin, margin, margin, margin);
        GridLayout.Spec rowSpec = GridLayout.spec(x, 1f);
        GridLayout.Spec colSpec = GridLayout.spec(y, 1f);
        gridParam.rowSpec = rowSpec;
        gridParam.columnSpec = colSpec;
        return gridParam;
    }

    private void setTileLiseners(Tile tile) {
        tile.setOnTouchListener(new TileTouchListener(gameActivity));
        //tile.setOnDragListener(new TileDragListener()); //czy ktos uzywa?
    }


    public void setLayGameOverVisibility(boolean isVisible) {
        layGameOver.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    public void setProgressBarProgress(int progress) {
        progressBar.setProgress(progress);
    }

    public void setStars(int rank) {
        switch (rank) {
            case 3:
                star3.setImageResource(fullStar);
            case 2:
                star2.setImageResource(fullStar);
            case 1:
                star1.setImageResource(fullStar);
                break;
            default:
                star1.setImageResource(emptyStar);
                star2.setImageResource(emptyStar);
                star3.setImageResource(emptyStar);
        }
    }

    public void showPoints(int points) {
        counter.setText(String.valueOf(points));
    }

    private int getLevelCompleteStars(int rank) {
        switch (rank) {
            case 1:
                return R.mipmap.star_complete1;
            case 2:
                return R.mipmap.star_complete2;
            default:
                return R.mipmap.star_complete3;
        }
    }

    public void setSourceValue(int value) {
        source.setText(String.valueOf(value));
    }

    public int getSourceValue() {
        return Integer.valueOf(source.getText().toString());
    }


    public void setProgressBarMax(int points) {
        progressBar.setMax(points);
    }


    public void prepareToRestart(int points) {
        showPoints(0);
        setStars(0);
        setProgressBarProgress(points);
    }

    public void setTileBackground(Tile tile, int value) {
        int background = LevelsConfig.getTileBackground(value);
        tile.setBackground(ContextCompat.getDrawable(gameActivity, background));
    }

    private void setLabel(boolean isGameOver, int level) {
        if (isGameOver) {
            label.setText("Game Over");
            label.setBackgroundResource(R.drawable.bg_round_red);
        } else {
            label.setText("Level " + level + " Complete");
            label.setBackgroundResource(R.drawable.bg_round_green);
        }

        label.setPadding(30, 30, 30, 30); //trzeba ustawic po zmianie tla
    }

    public void gameOver() {
        setLabel(true, 0);
        nextButton.setVisibility(View.INVISIBLE);
        restartButton.setVisibility(View.VISIBLE);
        levelStars.setBackgroundResource(0);
        layGameOver.setVisibility(View.VISIBLE);
    }

    public void levelComplete(int level, int rank) {
        setLabel(false, level);
        levelStars.setBackgroundResource(getLevelCompleteStars(rank));
        layGameOver.setVisibility(View.VISIBLE);

        restartButton.setVisibility(rank == 3 ? View.INVISIBLE : View.VISIBLE);

        if (LevelsConfig.isLastLevel(level)) {
            nextButton.setVisibility(View.INVISIBLE);
            gameComplete.setVisibility(View.VISIBLE);
        } else {
            nextButton.setVisibility(View.VISIBLE);
            gameComplete.setVisibility(View.INVISIBLE);
        }
    }

}
