package dev.cocoya.matzip.vos;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.cocoya.matzip.entities.data.PlaceEntity;

import java.util.Date;

public class PlaceVo extends PlaceEntity {
    private String categoryText;

    private int score;

    private int reviewCount;

    private boolean isClose;

    private Date isBreakNull;

    private Date isBreak;

    public String getCategoryText() {
        return categoryText;
    }

    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public boolean  getIsClose() {
        return isClose;
    }

    public void setIsClose(boolean isClose) {
        this.isClose = isClose;
    }

    public Date getIsBreakNull() {
        return isBreakNull;
    }

    public void setIsBreakNull(Date isBreakNull) {
        this.isBreakNull = isBreakNull;
    }

    public Date getIsBreak() {
        return isBreak;
    }

    public void setIsBreak(Date isBreak) {
        this.isBreak = isBreak;
    }
}
