package com.example.delivery.domain.review.repository;

import com.example.delivery.domain.review.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {


    /*
    review 엔티티를 조회, 리뷰가 속한 Store의 id가 storeId 파라미터와 일치해야함
    리뷰의 별점이 minRating 이상 maxRating 이하인 경우만 가져옴
    결과를 createdAt(작성일) 기준으로 최신순 정렬
     */
    @EntityGraph(attributePaths = {"user", "store"})
    @Query("SELECT r FROM Review r WHERE r.store.id = :storeId AND r.rating BETWEEN :minRating AND :maxRating ORDER BY r.createdAt DESC")
    List<Review> findByStoreIdAndRatingBetweenOrderByCreatedAtDesc(
            @Param("storeId") Long storeId,
            @Param("minRating") Integer minRating,
            @Param("maxRating") Integer maxRating
    );
}
