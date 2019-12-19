package tridoo.sigma2;

import android.view.MotionEvent;
import android.view.View;

class TileTouchListener implements View.OnTouchListener {

    private GameActivity gameActivity;

    public TileTouchListener(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!gameActivity.isGameActiv()) return true;

        if (event.getAction() != MotionEvent.ACTION_DOWN) return true;

        Tile tile = (Tile) v;
        if (tile.getValue() == 0) {
            gameActivity.processTouchTile(tile);
        }
        return true;
    }
}
