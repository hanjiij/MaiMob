package et.maimob.com.et;

import java.util.List;

import et.maimob.com.et.datatype.AppInfo;
import et.maimob.com.et.datatype.FunctionInfo;


/**
 * Created by jhj_Plus on 2015/8/5.
 */
public class IMainPanelDataChange {
    private static final String TAG = "IMainPanelDataChange";
    private static IMainPanelDataChange sTest;
    private OnMainPanelDataChangeListener mListener;

    public static IMainPanelDataChange getInstance() {
        if (sTest == null) {
            sTest = new IMainPanelDataChange();
        }
        return sTest;
    }

    public void setOnMainPanelDataChangeListener(
            OnMainPanelDataChangeListener listener) {
        mListener = listener;
    }

    public interface OnMainPanelDataChangeListener {

        void onMainPanelStyleChanged(int style, boolean isShowShortcutSettings);

        void onMainPanelReguseAppChanged(List<AppInfo> newAppInfos);

        void onMainPanelShortcutSettingsChanged(List<FunctionInfo> newFunctionInfos);
    }

    public void changeMainPanelStyle(int style, boolean isShowShortcutSettings) {

        if (mListener != null) {
            mListener.onMainPanelStyleChanged(style, isShowShortcutSettings);
        }
    }

    public void changeMainPanelReguseApp(List<AppInfo> newAppInfos) {

        if (mListener != null) {
            mListener.onMainPanelReguseAppChanged(newAppInfos);
        }

    }

    public void changeMainPanelShortcutSettings(List<FunctionInfo> newFunctionInfos) {

        if (mListener != null) {
            mListener.onMainPanelShortcutSettingsChanged(newFunctionInfos);
        }

    }
}

