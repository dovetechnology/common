package com.foamtrace.photopicker;

/**
 * 图片实体
 * Created by Nereo on 2015/4/7.
 */
public class Image {
    public String path;
    public String name;
    public long time;
    public long size;
    public int width;
    public int height;

    public Image(String path, String name, long time) {
        this.path = path;
        this.name = name;
        this.time = time;
    }

    public Image(String mPath, String mName, long mTime, long mSize, int mWidth, int mHeight) {
        path = mPath;
        name = mName;
        time = mTime;
        size = mSize;
        width = mWidth;
        height = mHeight;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Image other = (Image) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
