package dev.cocoya.matzip.services;

import dev.cocoya.matzip.entities.data.PlaceEntity;
import dev.cocoya.matzip.entities.data.ReviewEntity;
import dev.cocoya.matzip.entities.data.ReviewImageEntity;
import dev.cocoya.matzip.entities.member.UserEntity;
import dev.cocoya.matzip.enums.data.AddReviewResult;
import dev.cocoya.matzip.exceptions.RollbackException;
import dev.cocoya.matzip.interfaces.IResult;
import dev.cocoya.matzip.mappers.IDataMapper;
import dev.cocoya.matzip.vos.PlaceVo;
import dev.cocoya.matzip.vos.ReviewVo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Service(value = "dev.cocoya.matzip.services.DataService")
public class DataService {

    private final IDataMapper dataMapper;

    public DataService(IDataMapper dataMapper) {
        this.dataMapper = dataMapper;
    }

    public PlaceVo[] getPlaces(double minLat, double minLng, double maxLat, double maxLng) {
        return this.dataMapper.selectPlacesExceptImage(minLat, minLng, maxLat, maxLng);
    }

    public PlaceEntity getPlace(int index) {
        return this.dataMapper.selectPlaceByIndex(index);
    }

    public Enum<? extends IResult> addReview(UserEntity user, ReviewEntity review, MultipartFile[] images) throws RollbackException, IOException {
        if (user == null) {
            return AddReviewResult.NOT_SIGNED;
        }
        review.setUserId(user.getId());
        if (this.dataMapper.insertReview(review) == 0) {
            return AddReviewResult.FAILURE;
        }
        if(review.getContent().length() > 100){
            return AddReviewResult.CONTENT;
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

    //리뷰 불러오기
    public ReviewVo[] getReviews(int placeIndex) {
        ReviewVo[] reviews = this.dataMapper.selectReviewsByPlaceIndex(placeIndex);
        for (ReviewVo review : reviews) {
            ReviewImageEntity[] reviewImages = this.dataMapper.selectReviewImagesByReviewIndexExceptData(review.getIndex());

            int[] reviewImageIndexes = Arrays.stream(reviewImages).mapToInt(ReviewImageEntity::getIndex).toArray();
            // 위에 한줄이 밑에 네줄 한줄로 쓴거
            // int[] reviewImageIndexes = new int[reviewImages.length];
            // for (int i=0 ; i < reviewImages.length ; i++){
            // reviewImageIndexes[i] = reviewImages[i].getIndex();
            // }

            review.setImageIndexes(reviewImageIndexes);
        }
        return reviews;
    }

    public ReviewImageEntity getReviewImage(int index){
        return this.dataMapper.selectReviewImageByIndex(index);

    }

    public Enum<? extends IResult> deleteReview(UserEntity signedUser,Integer reviewIndex) {
        ReviewVo existingReview = this.dataMapper.selectReviewsByReviewIndex(reviewIndex);
        if (existingReview == null) {
            return AddReviewResult.FAILURE;
        }
        System.out.println(reviewIndex);
        if (signedUser == null || !signedUser.getId().equals(existingReview.getUserId())) {
            //앞에 조건이 참이면 뒤에 조건은 안쳐다봐도됨 -> ||
            //뒤에 조건이 부정이면 로그인한 사람이랑 게시글 작성한 사람이 다르다
            return AddReviewResult.NOT_SIGNED;
        }

        return this.dataMapper.deleteReviewByIndex(reviewIndex) > 0
                ? AddReviewResult.SUCCESS
                : AddReviewResult.FAILURE;
    }
}