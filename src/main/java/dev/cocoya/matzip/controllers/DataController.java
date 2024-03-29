package dev.cocoya.matzip.controllers;

import dev.cocoya.matzip.entities.data.PlaceEntity;
import dev.cocoya.matzip.entities.data.ReviewEntity;
import dev.cocoya.matzip.entities.data.ReviewImageEntity;
import dev.cocoya.matzip.entities.member.UserEntity;
import dev.cocoya.matzip.enums.data.AddReviewResult;
import dev.cocoya.matzip.exceptions.RollbackException;
import dev.cocoya.matzip.services.DataService;
import dev.cocoya.matzip.vos.PlaceVo;
import dev.cocoya.matzip.vos.ReviewVo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController(value = "dev.cocoya.matzip.controllers")
@RequestMapping(value = "data")
public class DataController {
    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping(value = "place", produces = MediaType.APPLICATION_JSON_VALUE)
    public PlaceVo[] getPlace(@RequestParam(value = "minLat") double minLat,
                              @RequestParam(value = "minLng") double minLng,
                              @RequestParam(value = "maxLat") double maxLat,
                              @RequestParam(value = "maxLng") double maxLng) {
//        return this.dataService.getPlaces();
        return this.dataService.getPlaces(minLat, minLng, maxLat, maxLng);
    }

    @GetMapping(value = "placeImage")
    public ResponseEntity<byte[]> getPlaceImage(@RequestParam(value = "pi") int index) {
        PlaceEntity place = this.dataService.getPlace(index);
        ResponseEntity<byte[]> responseEntity;

        if (place == null) {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(place.getImageType()));
            headers.setContentLength(place.getImage().length);
            responseEntity = new ResponseEntity<>(place.getImage(), HttpStatus.OK);
        }
        return responseEntity;
    }

    @PostMapping(value = "review")
    @ResponseBody
    public String postReview(@SessionAttribute(value = "user", required = false) UserEntity user,
                             @RequestParam(value = "images", required = false) MultipartFile[] images,
                             ReviewEntity review) throws IOException {
        JSONObject responseObject = new JSONObject();
        Enum<?> result;
        try {
            result = this.dataService.addReview(user, review, images);
        } catch (RollbackException ignored) {
            result = AddReviewResult.FAILURE;
        }


        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }

    //리뷰 불러오기
    @GetMapping(value = "review")
    public ReviewVo[] getReview(@RequestParam(value = "pi") int placeIndex) {
        return this.dataService.getReviews(placeIndex);
        //서비스에서 불러온거 바로 리턴
    }

    @GetMapping(value = "reviewImage")
    public ResponseEntity<byte[]> getReviewImage(@RequestParam(value = "index") int index) {
        ResponseEntity<byte[]> responseEntity;

        ReviewImageEntity reviewImage = this.dataService.getReviewImage(index);

        if (reviewImage == null) {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(reviewImage.getType()));
            headers.setContentLength(reviewImage.getData().length);
            responseEntity = new ResponseEntity<>(reviewImage.getData(), HttpStatus.OK);
        }
        return responseEntity;
    }
//
//    @RequestMapping(value = "review",
//            method = RequestMethod.DELETE, // 댓글 삭제하는 메서드
//            //위에 GET 이랑 주소를 동일하게 하고 방식만 다르게 함 -> 레스트
//            //DELETE 는 의미는 없는데 삭제할 기능을 하는 메서드니까 DELETE 로 써야함-> 약속
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public String deleteReview(@SessionAttribute(value = "user", required = false) UserEntity signedUser, @RequestParam(value = "reviewIndex", required = false) Integer reviewIndex ) {
//
//        ReviewVo review = new ReviewVo();
////        review.setIndex(reviewIndex);
//        System.out.println("controller : "+reviewIndex);
//        JSONObject responseObject = new JSONObject();
//        Enum<?> result = this.dataService.deleteReview(reviewIndex);
//        responseObject.put("result", result.name().toLowerCase());
//
//        if (result == AddReviewResult.SUCCESS) {
//            responseObject.put("reviewIndex", review.getIndex());
//        }
//        return responseObject.toString();
//    }

    @RequestMapping(value = "review",
            method = RequestMethod.DELETE, // 댓글 삭제하는 메서드
            //위에 GET 이랑 주소를 동일하게 하고 방식만 다르게 함 -> 레스트
            //DELETE 는 의미는 없는데 삭제할 기능을 하는 메서드니까 DELETE 로 써야함-> 약속
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteReview(@SessionAttribute(value = "user", required = false) UserEntity signedUser, @RequestParam(value = "reviewIndex", required = false) Integer reviewIndex) {
        Enum<?> result;
        if (signedUser == null) {
            result = AddReviewResult.NOT_SIGNED;
        } else {
            result = this.dataService.deleteReview(signedUser, reviewIndex);
        }

        JSONObject responseObject = new JSONObject();
        ReviewVo review = new ReviewVo();
        responseObject.put("result", result.name().toLowerCase());

        if (result == AddReviewResult.SUCCESS) {
            responseObject.put("reviewIndex", review.getIndex());
        }
        return responseObject.toString();
    }

}