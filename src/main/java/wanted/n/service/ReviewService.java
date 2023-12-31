package wanted.n.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.n.domain.Restaurant;
import wanted.n.domain.Review;
import wanted.n.dto.ReviewRequestDTO;
import wanted.n.exception.CustomException;
import wanted.n.exception.ErrorCode;
import wanted.n.repository.RestaurantRepository;
import wanted.n.repository.ReviewRepository;
import wanted.n.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    /**
     * 리뷰 등록
     * 리뷰 등록시 redis에서 해당 식당의 데이터 삭제
     */
    @Transactional
    @CacheEvict(value = "RestaurantDetailResponseDTO", key = "#reviewRequestDTO.restaurantId")
    public void createReview(ReviewRequestDTO reviewRequestDTO){

        reviewRequestDTO.setUser(userRepository.findById(reviewRequestDTO.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)));

        Restaurant restaurant = restaurantRepository.findById(reviewRequestDTO.getRestaurantId())
                .orElseThrow(() -> new CustomException(ErrorCode.RESTAURANT_NOT_FOUND));

        reviewRequestDTO.setRestaurant(restaurant);

        restaurant.updateReview(reviewRequestDTO.getRate());

        reviewRepository.save(Review.from(reviewRequestDTO));
    }

}
