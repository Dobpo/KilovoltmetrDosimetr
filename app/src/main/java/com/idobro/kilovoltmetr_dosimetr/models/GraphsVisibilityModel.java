package com.idobro.kilovoltmetr_dosimetr.models;

import java.util.List;

import static com.idobro.kilovoltmetr_dosimetr.models.GraphLine.FRONT_FIRST_CHANEL;
import static com.idobro.kilovoltmetr_dosimetr.models.GraphLine.FRONT_SECOND_CHANEL;
import static com.idobro.kilovoltmetr_dosimetr.models.GraphLine.FRONT_SECOND_TO_FIRST;
import static com.idobro.kilovoltmetr_dosimetr.models.GraphLine.FRONT_THIRD_CHANEL;
import static com.idobro.kilovoltmetr_dosimetr.models.GraphLine.FRONT_THIRD_OT_FIRST;
import static com.idobro.kilovoltmetr_dosimetr.models.GraphLine.FRONT_THIRD_TO_SECOND;

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

    public GraphVisibility getItem(@GraphLine String name) {
        switch (name) {
            case FRONT_FIRST_CHANEL:
                return items.get(0);
            case FRONT_SECOND_CHANEL:
                return items.get(1);
            case FRONT_THIRD_CHANEL:
                return items.get(2);
            case FRONT_THIRD_TO_SECOND:
                return items.get(3);
            case FRONT_THIRD_OT_FIRST:
                return items.get(4);
            case FRONT_SECOND_TO_FIRST:
                return items.get(5);
            default:
                throw new IllegalArgumentException("Wrong line type");
        }
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