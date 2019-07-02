package com.heshicaihao.photopicker.picker;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.heshicaihao.photopicker.PhotoPicker;
import com.heshicaihao.photopicker.R;

/**
 * Created by wzfu on 16/5/29.
 */
public abstract class PreviewBaseActivity extends AppCompatActivity {

    public static final String CURRENT_ITEM = "photo_preview_current_item";
    public static final String PHOTO_PATHS = "photo_preview_paths";

    public int currentItem;
    public ArrayList<String> paths;
    public List<String> tmpPaths = new ArrayList<>();

    private MenuItem menuItemDelete;
    private static final int menuItemDeleteId = Menu.FIRST + 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //适配安卓8.0版本增加了一个限制：如果是透明的Activity，则不能固定它的方向
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            boolean result = fixOrientation();
        }
        super.onCreate(savedInstanceState);

        setContentView(getContentViewId());

        currentItem = getIntent().getIntExtra(CURRENT_ITEM, 0);
        paths = getIntent().getStringArrayListExtra(PHOTO_PATHS);

        if(paths == null) {
            paths = new ArrayList<>();
        }
        tmpPaths.addAll(paths);

        initWidget();
    }

    protected abstract void initWidget();

    // 布局文件Id
    protected abstract int getContentViewId();

    protected void back(){
        int resultCode = tmpPaths.size() == paths.size()
                ? RESULT_CANCELED : RESULT_OK;
        Intent intent = new Intent();
        intent.putStringArrayListExtra(PhotoPicker.PATHS, paths);
        setResult(resultCode, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                back();
                return true;

            case menuItemDeleteId:
                deleteImage();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // 返回键处理
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuItemDelete = menu.add(Menu.NONE, menuItemDeleteId, 0, getString(R.string.delete));
        menuItemDelete.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    public abstract void updateTitle();

    public abstract void deleteImage();

    private boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable")
                    .getField("Window")
                    .get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
