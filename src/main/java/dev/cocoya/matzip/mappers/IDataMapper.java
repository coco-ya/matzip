package dev.cocoya.matzip.mappers;

import dev.cocoya.matzip.entities.data.PlaceEntity;
import dev.cocoya.matzip.entities.data.ReviewEntity;
import dev.cocoya.matzip.entities.data.ReviewImageEntity;
import dev.cocoya.matzip.vos.PlaceVo;
import dev.cocoya.matzip.vos.ReviewVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

@Mapper
public interface IDataMapper {

    // 이미지 빼고 셀렉 + 리뷰 별점 추가
    PlaceVo[] selectPlacesExceptImage(@RequestParam(value = "minLat") double minLat,
                                      @RequestParam(value = "minLng") double minLng,
                                      @RequestParam(value = "maxLat") double maxLat,
                                      @RequestParam(value = "maxLng") double maxLng);

    // 이미지 셀렉
    PlaceEntity selectPlaceByIndex(@Param(value = "index") int index);

    //리뷰 입력
    int insertReview(ReviewEntity review);

    //리뷰 이미지 삽입
    int insertReviewImage (ReviewImageEntity image);

    //리뷰 불러오기
    ReviewVo[] selectReviewsByPlaceIndex(@Param(value = "placeIndex")int placeIndex);

    //리뷰 이미지 불러오기 - 이미지는 용량 크니까 따로 뺌
    ReviewImageEntity[] selectReviewImagesByReviewIndexExceptData(@Param(value = "reviewIndex")int reviewIndex);

    ReviewImageEntity selectReviewImageByIndex(@Param(value = "index") int index);

    ReviewVo selectReviewsByReviewIndex(@Param(value = "index")int index);

    int deleteReviewByIndex(@Param(value = "index") int index);

}
