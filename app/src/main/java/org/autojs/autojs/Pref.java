package org.autojs.autojs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import androidx.preference.PreferenceManager;
import com.stardust.app.GlobalAppContext;
import com.stardust.autojs.runtime.accessibility.AccessibilityConfig;

import org.autojs.autojs.autojs.key.GlobalKeyObserver;
import org.autojs.autoxjs.R;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by Stardust on 2017/1/31.
 */
public class Pref {

    private static final SharedPreferences DISPOSABLE_BOOLEAN = GlobalAppContext.get().getSharedPreferences("DISPOSABLE_BOOLEAN", Context.MODE_PRIVATE);
    private static final String KEY_SERVER_ADDRESS = "KEY_SERVER_ADDRESS";
    private static final String KEY_SHOULD_SHOW_ANNUNCIATION = "KEY_SHOULD_SHOW_ANNUNCIATION";
    private static final String KEY_FLOATING_MENU_SHOWN = "KEY_FLOATING_MENU_SHOWN";
    private static final String KEY_EDITOR_THEME = "editor.theme";
    private static final String KEY_EDITOR_TEXT_SIZE = "editor.textSize";
    private static final String KEY_EDITOR_NEW = "KEY_EDITOR_NEW";

    public static boolean getEditor() {
        return def().getBoolean(KEY_EDITOR_NEW, true);
    }

    public static void setEditor(boolean shouldOpen) {
        def().edit().putBoolean(KEY_EDITOR_NEW, shouldOpen).apply();
    }

    private static SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences p, String key) {
            if (key.equals(getString(R.string.key_guard_mode))) {
                AccessibilityConfig.setIsUnintendedGuardEnabled(p.getBoolean(getString(R.string.key_guard_mode), false));
            } else if ((key.equals(getString(R.string.key_use_volume_control_record)) || key.equals(getString(R.string.key_use_volume_control_running)))
                    && p.getBoolean(key, false)) {
                GlobalKeyObserver.init();
            }
        }
    };

    static {
        AccessibilityConfig.setIsUnintendedGuardEnabled(def().getBoolean(getString(R.string.key_guard_mode), false));
    }

    private static SharedPreferences def() {
        return PreferenceManager.getDefaultSharedPreferences(GlobalAppContext.get());
    }

    private static boolean getDisposableBoolean(String key, boolean defaultValue) {
        boolean b = DISPOSABLE_BOOLEAN.getBoolean(key, defaultValue);
        if (b == defaultValue) {
            DISPOSABLE_BOOLEAN.edit().putBoolean(key, !defaultValue).apply();
        }
        return b;
    }

    public static boolean isNightModeEnabled() {
        return def().getBoolean(getString(R.string.key_night_mode), false);
    }

    public static boolean isFirstGoToAccessibilitySetting() {
        return getDisposableBoolean("isFirstGoToAccessibilitySetting", true);
    }

    public static int oldVersion() {
        return 0;
    }

    public static boolean isRunningVolumeControlEnabled() {
        return def().getBoolean(getString(R.string.key_use_volume_control_running), false);
    }

    public static boolean shouldEnableAccessibilityServiceByRoot() {
        return def().getBoolean(getString(R.string.key_enable_accessibility_service_by_root), false);
    }

    private static String getString(int id) {
        return GlobalAppContext.getString(id);
    }

    public static boolean isFirstUsing() {
        return getDisposableBoolean("isFirstUsing", true);
    }

    static {
        def().registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public static boolean isEditActivityFirstUsing() {
        return getDisposableBoolean("Love Honmua 18.7.9", true);
    }

    public static String getServerAddressOrDefault(String defaultAddress) {
        return def().getString(KEY_SERVER_ADDRESS, defaultAddress);
    }

    public static void saveServerAddress(String address) {
        def().edit().putString(KEY_SERVER_ADDRESS, address).apply();
    }

    public static boolean shouldShowAnnunciation() {
        return getDisposableBoolean(KEY_SHOULD_SHOW_ANNUNCIATION, true);
    }

    private static boolean isFirstDay() {
        long firstUsingMillis = def().getLong("firstUsingMillis", -1);
        if (firstUsingMillis == -1) {
            def().edit().putLong("firstUsingMillis", System.currentTimeMillis()).apply();
            return true;
        }
        return System.currentTimeMillis() - firstUsingMillis <= TimeUnit.DAYS.toMillis(1);
    }

    public static boolean isRecordToastEnabled() {
        return def().getBoolean(getString(R.string.key_record_toast), true);
    }

    public static boolean rootRecordGeneratesBinary() {
        return def().getString(getString(R.string.key_root_record_out_file_type), "binary")
                .equals("binary");
    }

    public static boolean isObservingKeyEnabled() {
        return def().getBoolean(getString(R.string.key_enable_observe_key), false);
    }

    public static boolean isStableModeEnabled() {
        return def().getBoolean(getString(R.string.key_stable_mode), false);
    }

    public static boolean isSingleBuildCleanModeEnabled() {
        return def().getBoolean(getString(R.string.key_single_build_clean_mode), true);
    }

    public static String getDocumentationUrl() {
//        String docSource = def().getString(getString(R.string.key_documentation_source), null);
        return "http://doc.autoxjs.com/";
    }

    public static boolean isFloatingMenuShown() {
        return def().getBoolean(KEY_FLOATING_MENU_SHOWN, false);
    }

    public static boolean isAutoBack() {
        return def().getBoolean(getString(R.string.key_auto_backup), true);
    }

    public static void setFloatingMenuShown(boolean checked) {
        def().edit().putBoolean(KEY_FLOATING_MENU_SHOWN, checked).apply();
    }

    public static String getCurrentTheme() {
        return def().getString(KEY_EDITOR_THEME, null);
    }

    public static void setCurrentTheme(String theme) {
        def().edit().putString(KEY_EDITOR_THEME, theme).apply();
    }

    public static void setEditorTextSize(int value) {
        def().edit().putInt(KEY_EDITOR_TEXT_SIZE, value).apply();
    }

    public static int getEditorTextSize(int defValue) {
        return def().getInt(KEY_EDITOR_TEXT_SIZE, defValue);
    }

    public static String getScriptDirPath() {
        String dir = def().getString(getString(R.string.key_script_dir_path),
                getString(R.string.default_value_script_dir_path));
        return new File(Environment.getExternalStorageDirectory(), dir).getPath();
    }

    public static String getKeyStorePath() {
        return getScriptDirPath().concat("/.keyStore/");
    }

    public static String getKeyStorePassWord(String keyName) {
        return def().getString(keyName, "");
    }

    public static void setKeyStorePassWord(String keyName, String passWord) {
        def().edit().putString(keyName, passWord).apply();
    }

    public static boolean isForegroundServiceEnabled() {
        return def().getBoolean(getString(R.string.key_foreground_servie), false);
    }

    public static void setCode(String value) {
        def().edit().putString("user_code", value).apply();
    }

    public static String getCode(String defValue) {
        return def().getString("user_code", defValue);
    }

    public static void setWebData(String webDataJson) {
        def().edit().putString("WebData", webDataJson).apply();
    }

    public static String getWebData() {
        return def().getString("WebData", "");
    }

    public static void setPermissionCheck(Boolean flag) {
        def().edit().putBoolean("permissionCheck", flag).apply();
    }

    public static Boolean getPermissionCheck() {
        return def().getBoolean("permissionCheck", true);
    }

    public static void setLineWrap(Boolean flag) {
        def().edit().putBoolean("LineWrap", flag).apply();
    }

    public static Boolean getLineWrap() {
        return def().getBoolean("LineWrap", false);
    }

    public static void setTaskManager(int number) {
        def().edit().putInt("TaskManager", number).apply();
    }

    public static int getTaskManager() {
        return def().getInt("TaskManager", 0);
    }

}
