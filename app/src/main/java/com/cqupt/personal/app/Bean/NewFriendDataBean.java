package com.cqupt.personal.app.Bean;

import java.util.List;

public class NewFriendDataBean {
    private List<String>url;
    private List<String>data;
    private List<String>flag;

    public List<String> getFlag() {
        return flag;
    }

    public void setFlag(List<String> flag) {
        this.flag = flag;
    }

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
