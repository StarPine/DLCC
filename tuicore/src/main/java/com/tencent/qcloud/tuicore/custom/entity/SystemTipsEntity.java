package com.tencent.qcloud.tuicore.custom.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 修改备注：
 *
 * @Name： PlayFun_Google
 * @Description：
 * @Author： liaosf
 * @Date： 2022/9/12 10:53
 */
public class SystemTipsEntity implements Serializable {
    private int type;
    private String url;
    private List<ContentBean> content;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean implements Serializable {
        private String text;
        private String color;
        private int bold;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public int getBold() {
            return bold;
        }

        public void setBold(int bold) {
            this.bold = bold;
        }
    }
}
