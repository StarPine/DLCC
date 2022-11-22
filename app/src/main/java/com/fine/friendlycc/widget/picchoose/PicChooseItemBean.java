package com.fine.friendlycc.widget.picchoose;

/**
 * @author wulei
 */
public class PicChooseItemBean {
    public static final int TYPE_IMG = 2;
    public static final int TYPE_ADD = 1;

    public static final int MEDIA_TYPE_IMG = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private String src;
    private int type;
    private int mediaType;

    public PicChooseItemBean() {
    }

    public PicChooseItemBean(int type, String src) {
        this.src = src;
        this.type = type;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}