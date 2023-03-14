package dev.cocoya.matzip.enums.data;

import dev.cocoya.matzip.interfaces.IResult;

public enum AddReviewResult implements IResult {
    FAILURE,
    NOT_SIGNED,
    SUCCESS,
    CONTENT
}