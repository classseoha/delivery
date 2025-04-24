package com.example.delivery.domain.review.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {


    //가게 기준 + 별점 범위 + 최신순 정렬
    @Query("SELECT r FROM Review r WHERE r.store.id = :storeId AND r.rating BETWEEN :minRating AND :maxRating ORDER BY r.createdAt DESC")
    // 특정 가게(storeId)의 리뷰들 중에서, rating이 min~max 사이인 리뷰를 최신순으로 조회
    List<Review> findByStoreIdAndRatingBetweenOrderByCreatedAtDesc(
            @Param("storeId") Long storeId,
            @Param("minRating") Integer minRating,
            @Param("maxRating") Integer maxRating
    );
}
