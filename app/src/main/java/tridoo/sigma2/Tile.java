package tridoo.sigma2;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.HashSet;
import java.util.Set;

public class Tile extends AppCompatTextView {

    private int value;
    private int positionX;
    private int positionY;
    private Set<Tile> neighbours;

    private Tile(int value, int x, int y, Context context) {
        super(context);
        this.value = value;
        this.positionX = x;
        this.positionY = y;
        neighbours = new HashSet<>();
    }

    public static Tile of(int x, int y, Context context) {
        return new Tile(0, x, y, context);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        this.setText((value > 0) ? String.valueOf(value) : " ");
    }

    public Set<Tile> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(Set<Tile> neighbours) {
        this.neighbours = neighbours;
    }


    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public String getPositionAsString() {
        return String.valueOf("" + positionX + positionY);
    }


    public void incrementValue() {
        this.setValue(value + 1);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        //Log.v("onMeasure", MeasureSpec.toString(widthMeasureSpec) + " "+ positionX +" "+ positionY);
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(getPositionAsString());
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;
        // null check
        if (obj == null)
            return false;
        // type check and cast
        if (getClass() != obj.getClass())
            return false;

        Tile tile = (Tile) obj;
        return tile.positionX == this.positionX
                && tile.positionY == this.positionY;
    }

    @Override
    public String toString() {
        return positionX + "." + positionY + " " + value;
    }
}
