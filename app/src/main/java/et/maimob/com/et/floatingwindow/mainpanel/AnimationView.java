package et.maimob.com.et.floatingwindow.mainpanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Administrator on 2015/7/16.
 */
public class AnimationView extends View {
    private RectF mColorWheelRectangle = new RectF();//圆圈的矩形范围
    private Paint mDefaultWheelPaint; // 绘制底部灰色圆圈的画笔
    private Paint mColorWheelPaint; //绘制蓝色扇形的画笔
    private Paint textPaint; //中间文字的画笔
    private Paint paint;//深度清理的字体
    private int WordSize;//文字颜色
    private float mColorWheelRadius;// 圆圈普通状态下的半径
    private float circleStrokeWidth; //圆圈的线条粗细
    private float pressExtraStrokeWidth;//按下状态下增加的圆圈线条增加的粗细
    private String mText;//中间文字内容
    private int mCount;// 为了达到数字增加效果而添加的变量，他和mText其实代表一个意思
    private float mSweepAnglePer;  //为了达到蓝色扇形增加效果而添加的变量，他和mSweepAngle其实代表一个意思
    private float mSweepAngle; //扇形弧度
    private int mTextSize;//文字颜色
    BarAnimation anim;//动画类
    Context context;
    public AnimationView(Context context) {
        super(context);
        this.context=context;
        init(null, 0);
    }

    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init(attrs,0);
    }

    public AnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init(attrs,defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mColorWheelRectangle, -90, 360, false, mDefaultWheelPaint);
        canvas.drawArc(mColorWheelRectangle, -90, mSweepAnglePer, false, mColorWheelPaint);
        Rect bounds = new Rect();
        String textstr=mCount+""+"%";
        textPaint.getTextBounds(textstr, 0, textstr.length(), bounds);
        paint.getTextBounds(textstr, 0, textstr.length(), bounds);
        canvas.drawText(
                textstr + "",
                (mColorWheelRectangle.centerX())
                        - (textPaint.measureText(textstr) / 2),
                mColorWheelRectangle.centerY() + bounds.height() / 2-20,
                textPaint);
        canvas.drawText(
                "深度清理",
                (mColorWheelRectangle.centerX())
                        - (textPaint.measureText(textstr) / 2)-30,
                mColorWheelRectangle.centerY() + bounds.height() / 2+20,
                paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(),
                heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        mColorWheelRadius = min - circleStrokeWidth -pressExtraStrokeWidth ;

        mColorWheelRectangle.set(circleStrokeWidth + pressExtraStrokeWidth, circleStrokeWidth + pressExtraStrokeWidth,
                mColorWheelRadius, mColorWheelRadius);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (pressed) {
            mColorWheelPaint.setColor(0xFF00CD00);//165da6
            textPaint.setColor(0xFF00FF00);// 070707
            mColorWheelPaint.setStrokeWidth(circleStrokeWidth+pressExtraStrokeWidth);
            mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth + pressExtraStrokeWidth);
            textPaint.setTextSize(mTextSize - pressExtraStrokeWidth);//
//////////////
            MemoryClear.clearCount(context);
            int value=MemoryClear.getMemory(context);
            mText=value+"";
            mSweepAngle= (float)((int)(value*3.4));
        } else {
            mColorWheelPaint.setColor(0xFF00FF00);
            textPaint.setColor(0xFF00FF00);//333333
            mColorWheelPaint.setStrokeWidth(circleStrokeWidth);
            mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth);
            textPaint.setTextSize(mTextSize);
        }
        this.invalidate();
    }

    private void init(AttributeSet attrs, int defStyle) {

        circleStrokeWidth = dip2px(getContext(), 4);//圆弧的宽度
        pressExtraStrokeWidth = dip2px(getContext(), 1);
        mTextSize = dip2px(getContext(),18);//设置字体大小

        mColorWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorWheelPaint.setColor(0xFF00FF00);//29a6f6
        mColorWheelPaint.setStyle(Paint.Style.STROKE);
        mColorWheelPaint.setStrokeWidth(circleStrokeWidth);

        mDefaultWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDefaultWheelPaint.setColor(0xFFeeefef);
        mDefaultWheelPaint.setStyle(Paint.Style.STROKE);
        mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setColor(0xFF00FF00);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(mTextSize);

        paint=new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint.setColor(0xFFB22222);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextAlign(Paint.Align.LEFT);
        WordSize = dip2px(getContext(), 10);
        paint.setTextSize(WordSize);


        mText = "0";
        mSweepAngle = 0;

        anim = new BarAnimation();
        anim.setDuration(2000);

    }
    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;//获取手机分辨率
        return (int)(dipValue * scale + 0.5f);
    }

    /*设置动画旋转角度
    * */
    public void setSweepAngle(int i) {
        mSweepAngle = (float) (i*3.33);
    }
    /*开始动画
    * */
    public void startCustomAnimation() {
        this.startAnimation(anim);
    }
    /*设置数值大小
    * */
    public void setText(String text) {
        mText = text;
        this.startAnimation(anim);
    }

    public class BarAnimation extends Animation {
        /*
        *
        * */
        public BarAnimation() {

        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                mSweepAnglePer =  interpolatedTime * mSweepAngle;
                mCount = (int)(interpolatedTime * Float.parseFloat(mText));
            } else {
                mSweepAnglePer = mSweepAngle;
                mCount = Integer.parseInt(mText);
            }
            postInvalidate();
        }
    }
}
