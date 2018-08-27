package com.cqupt.personal.app.Bean;

import java.util.List;

public class BecomeFriendDataBean {
    private List<String>account;
    private List<String>signature;
    private List<String>headIcon;

    public List<String> getAccount() {
        return account;
    }

    public void setAccount(List<String> account) {
        this.account = account;
    }

    public List<String> getSignature() {
        return signature;
    }

    public void setSignature(List<String> signature) {
        this.signature = signature;
    }

    public List<String> getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(List<String> headIcon) {
        this.headIcon = headIcon;
    }
}
