package com.example.api.ramsha.storage;

public class StorageItems {
    String path;
    String name;
    String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public StorageItems(String icon) {
        this.icon = icon;
    }

    public StorageItems(String path, String name,String icon) {
        this.path = path;
        this.name = name;
        this.icon=icon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
