package dev.cocoya.matzip.mappers;

import dev.cocoya.matzip.entities.data.PlaceEntity;
import dev.cocoya.matzip.entities.data.ReviewEntity;
import dev.cocoya.matzip.entities.data.ReviewImageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IDataMapper {
    int insertReview(ReviewEntity review);

    int insertReviewImage(ReviewImageEntity reviewImage);

    PlaceEntity selectPlaceByIndex(@Param(value = "index") int index);

    PlaceEntity[] selectPlacesExceptImage(@Param(value = "minLat") double minLat,
                                          @Param(value = "minLng") double minLng,
                                          @Param(value = "maxLat") double maxLat,
                                          @Param(value = "maxLng") double maxLng);
}