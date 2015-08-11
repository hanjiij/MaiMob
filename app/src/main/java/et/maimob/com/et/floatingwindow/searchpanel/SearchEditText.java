package et.maimob.com.et.floatingwindow.searchpanel;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import et.maimob.com.et.floatingwindow.floatwindow.FloatWindowMgr;


/**
 * Created by jhj_Plus on 2015/7/8.
 * 自定义带右边删除输入文本的EditText
 */
public class SearchEditText extends EditText
        implements TextWatcher,TextView.OnEditorActionListener
{
    private static final String TAG = "SearchEditText";
    private Drawable clearDrawable;
    private Context appContext;
    private InputMethodManager imm;

    public SearchEditText(Context context) {
        this(context,null);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        appContext = context.getApplicationContext();
        init();
    }

    private void init() {
        Log.i(TAG, "-----------------init-----------");
        imm = (InputMethodManager) appContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        addTextChangedListener(this);
        setOnEditorActionListener(this);
//        Drawable rightDrawable = getCompoundDrawables()[2];
//        clearDrawable = rightDrawable == null ? getResources()
//                .getDrawable(R.drawable.float_search_delete_btn_normal) : rightDrawable;
//        clearDrawable
//                .setBounds(0, 0, (int) getResources().getDimension(R.dimen.drawable_clear_width),
//                           (int) getResources().getDimension(R.dimen.drawable_clear_height));
//        setClearDrawableVisible(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventType = event.getAction();
        switch (eventType) {
            case MotionEvent.ACTION_UP:
                int searchEditTextWidth = getWidth();
                int searchEditTextHeight = getHeight();
                //判断触摸的范围是否在清除图标的范围内
                boolean isCanSearch = event.getX() >= searchEditTextWidth - getTotalPaddingRight() &&
                                  event.getX() <= searchEditTextWidth - getPaddingRight() &&
                                  event.getY() >= getTotalPaddingTop() &&
                                  event.getY() <= searchEditTextHeight - getTotalPaddingBottom();
                if (isCanSearch) {
                    searchOnWeb(getText().toString());
                }
                break;
            default:
                //强制显示键盘
                imm.showSoftInput(this, InputMethodManager.SHOW_FORCED);
                return super.onTouchEvent(event);
        }
        return true;
    }

//    private void setClearDrawableVisible(boolean visible) {
//        Log.i(TAG, "-----------------setClearDrawableVisible-----------" + visible);
//        Drawable[] drawables = getCompoundDrawables();
//        setCompoundDrawables(drawables[0], drawables[1], visible ? clearDrawable : null,
//                             drawables[3]);
//        boolean b = getCompoundDrawables()[2] == null;
//        Log.i(TAG, "-----------------rightDrawable is null-----------" + b);
//
//    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        //setClearDrawableVisible(s.toString().length() > 0);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchOnWeb(getText().toString());
        }
        return true;
    }

    private void searchOnWeb(String s) {
        //强制隐藏键盘
        imm.hideSoftInputFromWindow(getWindowToken(), 0);
        FloatWindowMgr.getInstance(appContext).backToSmallFloatWindow();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                .parse("http://www.baidu.com/s?wd=" + Uri.encode(s)));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appContext.startActivity(intent);
    }
}
