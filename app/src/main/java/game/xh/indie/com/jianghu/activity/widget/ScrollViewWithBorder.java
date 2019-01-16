package game.xh.indie.com.jianghu.activity.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.ScrollView;

import game.xh.indie.com.jianghu.R;

public class ScrollViewWithBorder extends ScrollView {
    public static final float DEFAULT_STROKE_WIDTH = 1.0f; // 默认边框宽度, 1dp
    public static final float DEFAULT_CORNER_RADIUS = 1.0f; // 默认圆角半径, 2dp

    private int borderWidth;    //边框宽度
    private int borderColor;    //边框颜色
    private int cornerRadius;   //边框圆角半径

    private Paint paint = new Paint();      //绘图的画笔对象
    private RectF myRectF;        //绘制的矩形边框

    public ScrollViewWithBorder(Context context) {
        this(context, null);
    }

    public ScrollViewWithBorder(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollViewWithBorder(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ScrollViewWithBorder(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        // 将DIP单位默认值转为PX
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        borderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_STROKE_WIDTH, displayMetrics);
        cornerRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CORNER_RADIUS, displayMetrics);

        //读取属性值
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BorderStyle);
        borderWidth = ta.getDimensionPixelSize(R.styleable.BorderStyle_layout_borderWidth, borderWidth);
        cornerRadius = ta.getDimensionPixelSize(R.styleable.BorderStyle_cornerRadius, cornerRadius);
        borderColor = ta.getColor(R.styleable.BorderStyle_layout_borderColor, Color.TRANSPARENT);
        ta.recycle();

        myRectF = new RectF();
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.STROKE); // 空心效果
        paint.setAntiAlias(true); // 设置画笔为无锯齿
        paint.setStrokeWidth(borderWidth); // 线宽
        paint.setColor(borderColor);        //颜色

        // 画空心圆角矩形
        myRectF.left = myRectF.top = 0.5f * borderWidth;
        myRectF.right = getMeasuredWidth() - borderWidth;
        myRectF.bottom = getMeasuredHeight() - borderWidth;
        canvas.drawRoundRect(myRectF, cornerRadius, cornerRadius, paint);
    }
}
