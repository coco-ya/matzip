package dev.cocoya.matzip.controllers;

import dev.cocoya.matzip.entities.data.PlaceEntity;
import dev.cocoya.matzip.entities.data.ReviewEntity;
import dev.cocoya.matzip.entities.member.UserEntity;
import dev.cocoya.matzip.enums.data.AddReviewResult;
import dev.cocoya.matzip.exceptions.RollbackException;
import dev.cocoya.matzip.services.DataService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public PlaceEntity[] getPlace(@RequestParam(value = "minLat") double minLat,
                                  @RequestParam(value = "minLng") double minLng,
                                  @RequestParam(value = "maxLat") double maxLat,
                                  @RequestParam(value = "maxLng") double maxLng) {
        return this.dataService.getPlaces(minLat, minLng, maxLat, maxLng);
    }

    @GetMapping(value = "placeImage")
    public ResponseEntity<byte[]> getPlaceImage(@RequestParam(value = "pi") int index) {
        ResponseEntity<byte[]> responseEntity;
        PlaceEntity place = this.dataService.getPlace(index);
        if (place == null) {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(place.getImageType()));
            headers.setContentLength(place.getImage().length);
            responseEntity = new ResponseEntity<>(place.getImage(), headers, HttpStatus.OK);
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
}















