# PhotoPicker

> 基于 [lovetuzitong/MultiImageSelector](https://github.com/lovetuzitong/MultiImageSelector) 修改的一个照片选择库。

```gradle
compile 'cc.dagger:photopicker:1.0'
```

功能：

- 支持主流图片加载库
- 照片尺寸、Gif过滤
- 运行时权限检查

[Demo Video](http://weibo.com/p/230444d894c9d8478c595aa7ddb832084e4437)

## 使用介绍

- Application 配置

```java
PhotoPicker.init(ImageLoader, null);
```

- AndroidManifest 配置

```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

<activity
    android:name="cc.dagger.photopicker.MultiImageSelectorActivity"
    android:screenOrientation="portrait"
    android:theme="@style/NO_ACTIONBAR" />

<activity
    android:name="cc.dagger.photopicker.PhotoPreviewActivity"
    android:screenOrientation="portrait"
    android:theme="@style/NO_ACTIONBAR"/>
```

- 选择照片

``` java
// 单选
PhotoPicker.load()
        .filter(PhotoFilter) // 照片属性过滤
        .gridColumns(4) // 照片列表显示列数
        .showCamera(false)
        .single()
        .start(Activity/Fragment); // 从Fragment、Activity中启动


// 多选
PhotoPicker.load()
        .filter(PhotoFilter) // 照片属性过滤
        .gridColumns(4) // 照片列表显示列数
        .showCamera(true)
        .multi()
        .maxPickSize(9) // 最大选择数
        .selectedPaths(ArrayList<String>) // 已选择的照片地址
        .start(Activity/Fragment); // 从Fragment、Activity中启动

// 接收选择的照片
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PhotoPicker.REQUEST_SELECTED) {
        if (resultCode == RESULT_OK) {
            ArrayList<String> paths = data.getStringArrayListExtra(PhotoPicker.EXTRA_RESULT);
            // ...
        }
    }
}
```

- 预览照片

```java
PhotoPicker.preview()
        .paths(ArrayList<String>)
        .currentItem(0)
        .start(Activity、Fragment);

// 预览后返回照片地址
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PhotoPicker.REQUEST_PREVIEW) {
        if (resultCode == RESULT_OK) {
            ArrayList<String> paths = data.getStringArrayListExtra(PhotoPicker.PATHS);
            // ...
        }
    }
}
```
