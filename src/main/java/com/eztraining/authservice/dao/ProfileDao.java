package com.eztraining.authservice.dao;

import com.eztraining.authservice.bean.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileDao extends JpaRepository<Profile, Integer> {
}
