package dev.cocoya.matzip.vos;

import dev.cocoya.matzip.entities.data.ReviewEntity;

public class ReviewVo extends ReviewEntity {
    // 리뷰에 닉네임이랑 이미지 땡겨오기 위함
    private String userNickname;
    private int[] imageIndexes;



    public String getUserNickname() {
        return userNickname;
    }

    public ReviewVo setUserNickname(String userNickname) {
        this.userNickname = userNickname;
        return this;
    }

    public int[] getImageIndexes() {
        return imageIndexes;
    }

    public ReviewVo setImageIndexes(int[] imageIndexes) {
        this.imageIndexes = imageIndexes;
        return this;
    }


}
