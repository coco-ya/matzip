package dev.cocoya.matzip.entities.data;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.Objects;

public class PlaceEntity {
    private int index;
    private String name;
    private byte[] image;
    private String imageType;
    private String addressPrimary;
    private String addressSecondary;
    private String contactFirst;
    private String contactSecond;
    private String contactThird;
    private String homepage;
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+9")
    private Date openFrom;
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+9")
    private Date openTo;
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+9")
    private Date breakFrom;
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+9")
    private Date breakTo;
    private String description;
    private double latitude;
    private double longitude;
    private int categoryIndex;
    private Date createdOn;
    private Date modifiedOn;

    public int getIndex() {
        return index;
    }

    public PlaceEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getName() {
        return name;
    }

    public PlaceEntity setName(String name) {
        this.name = name;
        return this;
    }

    public byte[] getImage() {
        return image;
    }

    public PlaceEntity setImage(byte[] image) {
        this.image = image;
        return this;
    }

    public String getImageType() {
        return imageType;
    }

    public PlaceEntity setImageType(String imageType) {
        this.imageType = imageType;
        return this;
    }

    public String getAddressPrimary() {
        return addressPrimary;
    }

    public PlaceEntity setAddressPrimary(String addressPrimary) {
        this.addressPrimary = addressPrimary;
        return this;
    }

    public String getAddressSecondary() {
        return addressSecondary;
    }

    public PlaceEntity setAddressSecondary(String addressSecondary) {
        this.addressSecondary = addressSecondary;
        return this;
    }

    public String getContactFirst() {
        return contactFirst;
    }

    public PlaceEntity setContactFirst(String contactFirst) {
        this.contactFirst = contactFirst;
        return this;
    }

    public String getContactSecond() {
        return contactSecond;
    }

    public PlaceEntity setContactSecond(String contactSecond) {
        this.contactSecond = contactSecond;
        return this;
    }

    public String getContactThird() {
        return contactThird;
    }

    public PlaceEntity setContactThird(String contactThird) {
        this.contactThird = contactThird;
        return this;
    }

    public String getHomepage() {
        return homepage;
    }

    public PlaceEntity setHomepage(String homepage) {
        this.homepage = homepage;
        return this;
    }

    public Date getOpenFrom() {
        return openFrom;
    }

    public PlaceEntity setOpenFrom(Date openFrom) {
        this.openFrom = openFrom;
        return this;
    }

    public Date getOpenTo() {
        return openTo;
    }

    public PlaceEntity setOpenTo(Date openTo) {
        this.openTo = openTo;
        return this;
    }

    public Date getBreakFrom() {
        return breakFrom;
    }

    public PlaceEntity setBreakFrom(Date breakFrom) {
        this.breakFrom = breakFrom;
        return this;
    }

    public Date getBreakTo() {
        return breakTo;
    }

    public PlaceEntity setBreakTo(Date breakTo) {
        this.breakTo = breakTo;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PlaceEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public PlaceEntity setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public PlaceEntity setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public int getCategoryIndex() {
        return categoryIndex;
    }

    public PlaceEntity setCategoryIndex(int categoryIndex) {
        this.categoryIndex = categoryIndex;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public PlaceEntity setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public PlaceEntity setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceEntity that = (PlaceEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}