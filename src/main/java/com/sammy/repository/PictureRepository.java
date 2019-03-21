package com.sammy.repository;

import com.sammy.entity.business.PictureBusinessDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureRepository extends JpaRepository<PictureBusinessDTO, Long> {
    PictureBusinessDTO findByName(String name);
}