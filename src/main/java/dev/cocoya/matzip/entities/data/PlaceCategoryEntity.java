package dev.cocoya.matzip.entities.data;

import java.util.Objects;

public class PlaceCategoryEntity {
    private int index;
    private String text;

    public int getIndex() {
        return index;
    }

    public PlaceCategoryEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getText() {
        return text;
    }

    public PlaceCategoryEntity setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceCategoryEntity that = (PlaceCategoryEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}