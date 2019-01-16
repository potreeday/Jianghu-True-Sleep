package game.xh.indie.com.jianghu.activity.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import game.xh.indie.com.jianghu.R;

public class TextViewWithBorder extends AppCompatTextView {
    public static final float DEFAULT_STROKE_WIDTH = 1.0f; // 默认边框宽度, 1dp
    public static final float DEFAULT_CORNER_RADIUS = 1.0f; // 默认圆角半径, 2dp
    public static final float DEFAULT_LR_PADDING = 6f;      // 默认左右内边距
    public static final float DEFAULT_TB_PADDING = 2f;      // 默认上下内边距

    private int borderWidth;    //边框宽度
    private int borderColor;    //边框颜色
    private int cornerRadius;   //边框圆角半径

    private Paint paint = new Paint();      //绘图的画笔对象
    private RectF myRectF;        //绘制的矩形边框

    public TextViewWithBorder(Context context) {
        this(context, null);
    }

    public TextViewWithBorder(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public TextViewWithBorder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

        // 如果使用时没有设置内边距, 设置默认边距
        int paddingLeft = getPaddingLeft() == 0 ? (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LR_PADDING, displayMetrics) : getPaddingLeft();
        int paddingRight = getPaddingRight() == 0 ? (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LR_PADDING, displayMetrics) : getPaddingRight();
        int paddingTop = getPaddingTop() == 0 ? (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TB_PADDING, displayMetrics) : getPaddingTop();
        int paddingBottom = getPaddingBottom() == 0 ? (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TB_PADDING, displayMetrics) : getPaddingBottom();
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
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
