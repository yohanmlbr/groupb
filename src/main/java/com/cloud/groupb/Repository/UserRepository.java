package com.cloud.groupb.Repository;

import com.cloud.groupb.Entity.UserDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDB, String> {

}
