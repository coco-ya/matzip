package dev.cocoya.matzip.entities.data;

import java.util.Objects;

public class ReviewEntity {
    private int index;
    private String userId;
    private int placeIndex;
    private int score;
    private String content;

    public int getIndex() {
        return index;
    }

    public ReviewEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public ReviewEntity setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public int getPlaceIndex() {
        return placeIndex;
    }

    public ReviewEntity setPlaceIndex(int placeIndex) {
        this.placeIndex = placeIndex;
        return this;
    }

    public int getScore() {
        return score;
    }

    public ReviewEntity setScore(int score) {
        this.score = score;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ReviewEntity setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewEntity that = (ReviewEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
