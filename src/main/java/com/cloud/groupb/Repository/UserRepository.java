package com.cloud.groupb.Repository;

import com.cloud.groupb.Entity.UserDB;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserDB, Integer> {

    //UserDB findById(int id);

    List<UserDB> findByLastNameContainsOrFirstNameContains(String lastname,String firstanme, Pageable page);

    List<UserDB> findByBirthDayBetween(Date date1, Date date2, Pageable page);

    List<UserDB> findByBirthDayBefore(Date date, Pageable page);

    @Query(value ="SELECT *, ( acos(sin(RADIANS(lat))*sin(RADIANS(?1))+cos(RADIANS(lat))*cos(RADIANS(?1))*cos(RADIANS(lon-?2)))*6371 ) AS distance FROM User ORDER BY distance",nativeQuery = true)
    List<UserDB> findByLatLon(double lat, double lon, Pageable page);
}
