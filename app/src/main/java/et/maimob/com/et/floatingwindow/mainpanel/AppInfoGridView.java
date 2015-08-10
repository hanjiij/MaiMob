package et.maimob.com.et.floatingwindow.mainpanel;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by jhj_Plus on 2015/7/13.
 */
public class AppInfoGridView extends GridView {
    private static final String TAG = "AppInfoGridView";

    public AppInfoGridView(Context context) {
        super(context);
    }

    public AppInfoGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppInfoGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
}
