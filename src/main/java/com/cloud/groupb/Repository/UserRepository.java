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

    @Query("SELECT u FROM UserDB u WHERE u.birthDay<?1")
    List<UserDB> findByAgeGt(Date date, Pageable page);

    @Query("SELECT u FROM UserDB u WHERE u.birthDay<=?1 AND u.birthDay>=?2")
    List<UserDB> findByAgeEq(Date date1, Date date2, Pageable page);

    List<UserDB> findByLastNameContainsOrFirstNameContains(String lastname,String firstanme, Pageable page);

    List<UserDB> findByBirthDayBetween(Date date1, Date date2, Pageable page);

    List<UserDB> findByBirthDayBefore(Date date, Pageable page);

    List<UserDB> findByLat(double lat, double lon, Pageable page);
}
