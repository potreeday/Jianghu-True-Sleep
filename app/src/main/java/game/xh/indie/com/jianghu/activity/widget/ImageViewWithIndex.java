package game.xh.indie.com.jianghu.activity.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class ImageViewWithIndex extends AppCompatImageView {
    private int xIndex;
    private int yIndex;

    public ImageViewWithIndex(Context context) {
        this(context,null);
    }

    public ImageViewWithIndex(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ImageViewWithIndex(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getxIndex() {
        return xIndex;
    }

    public int getyIndex() {
        return yIndex;
    }

    public void setxIndex(int xIndex) {
        this.xIndex = xIndex;
    }

    public void setyIndex(int yIndex) {
        this.yIndex = yIndex;
    }
}
