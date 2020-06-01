package com.idobro.kilovoltmetr_dosimetr.fragments.charts_screen;

public final class InfoItem {
    private final String color;
    private final String text;

    InfoItem(String color, String text) {
        this.color = color;
        this.text = text;
    }

    public String getColor() {
        return color;
    }

    public String getText() {
        return text;
    }
}