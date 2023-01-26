package dev.cocoya.matzip.entities.member;

import java.util.Date;
import java.util.Objects;

public class UserEntity {
    private String id;
    private String nickname;
    private Date registeredOn;

    public String getId() {
        return id;
    }

    public UserEntity setId(String id) {
        this.id = id;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public UserEntity setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public Date getRegisteredOn() {
        return registeredOn;
    }

    public UserEntity setRegisteredOn(Date registeredOn) {
        this.registeredOn = registeredOn;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}