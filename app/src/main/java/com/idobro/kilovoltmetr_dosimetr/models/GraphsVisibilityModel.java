package com.idobro.kilovoltmetr_dosimetr.models;

import java.util.List;

public class GraphsVisibilityModel {
    private List<GraphVisibility> items;

    public GraphsVisibilityModel(List<GraphVisibility> items) {
        this.items = items;
    }

    public List<GraphVisibility> getItems() {
        return items;
    }

    public void setItems(List<GraphVisibility> items) {
        this.items = items;
    }

    public static class GraphVisibility {
        private String title;
        private String color;
        private Boolean isChecked;

        public GraphVisibility(String title, String color, Boolean isChecked) {
            this.title = title;
            this.color = color;
            this.isChecked = isChecked;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public Boolean getChecked() {
            return isChecked;
        }

        public void setChecked(Boolean checked) {
            isChecked = checked;
        }
    }
}