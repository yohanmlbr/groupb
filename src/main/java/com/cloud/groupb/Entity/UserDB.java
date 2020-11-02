package com.cloud.groupb.Entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user", schema = "cloud_group_b", catalog = "")
public class UserDB {
    private String id;
    private String firstName;
    private String lastName;
    private String birthDay;
    private long lat;
    private long lon;

    @Id
    @Column(name = "id", nullable = false, length = 50)
    public String getId() {
        return id;
    }

    public void setNumCli(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "firstName", nullable = true, length = 50)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "lastName", nullable = true, length = 50)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "birthDay", nullable = true, length = 10)
    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    @Basic
    @Column(name = "lat", nullable = true)
    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    @Basic
    @Column(name = "lon", nullable = true)
    public long getLon() {
        return lon;
    }

    public void setLon(long lon) {
        this.lon = lon;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDB that = (UserDB) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(birthDay, that.birthDay) &&
                Objects.equals(lat, that.lat) &&
                Objects.equals(lon, that.lon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, birthDay, lat, lon);
    }
}
