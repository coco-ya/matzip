package dev.cocoya.matzip.services;

import dev.cocoya.matzip.entities.data.PlaceEntity;
import dev.cocoya.matzip.entities.data.ReviewEntity;
import dev.cocoya.matzip.entities.data.ReviewImageEntity;
import dev.cocoya.matzip.entities.member.UserEntity;
import dev.cocoya.matzip.enums.data.AddReviewResult;
import dev.cocoya.matzip.exceptions.RollbackException;
import dev.cocoya.matzip.interfaces.IResult;
import dev.cocoya.matzip.mappers.IDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service(value = "dev.cocoya.matzip.services.DataService")
public class DataService {
    private final IDataMapper dataMapper;

    @Autowired
    public DataService(IDataMapper dataMapper) {
        this.dataMapper = dataMapper;
    }

    public PlaceEntity getPlace(int index) {
        return this.dataMapper.selectPlaceByIndex(index);
    }

    public PlaceEntity[] getPlaces(double minLat, double minLng, double maxLat, double maxLng) {
        return this.dataMapper.selectPlacesExceptImage(minLat, minLng, maxLat, maxLng);
    }

    @Transactional
    public Enum<? extends IResult> addReview(UserEntity user, ReviewEntity review, MultipartFile[] images) throws
            IOException,
            RollbackException {
        if (user == null) {
            return AddReviewResult.NOT_SIGNED;
        }
        review.setUserId(user.getId());
        if (this.dataMapper.insertReview(review) == 0) {
            return AddReviewResult.FAILURE;
        }
        if (images != null && images.length > 0) {
            for (MultipartFile image : images) {
                ReviewImageEntity reviewImage = new ReviewImageEntity();
                reviewImage.setReviewIndex(review.getIndex());
                reviewImage.setData(image.getBytes());
                reviewImage.setType(image.getContentType());
                if (this.dataMapper.insertReviewImage(reviewImage) == 0) {
                    throw new RollbackException();
                }
            }
        }
        return AddReviewResult.SUCCESS;
    }
}

















