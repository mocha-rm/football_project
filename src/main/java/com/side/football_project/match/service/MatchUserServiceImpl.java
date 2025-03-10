package com.side.football_project.match.service;

import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.service.UserService;
import com.side.football_project.domain.user.type.UserRole;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.UserErrorCode;
import com.side.football_project.match.domain.Match;
import com.side.football_project.match.domain.MatchUser;
import com.side.football_project.match.dto.MatchRatingRequestDto;
import com.side.football_project.match.dto.MatchRequestDto;
import com.side.football_project.match.dto.MatchResponseDto;
import com.side.football_project.match.repository.MatchRepository;
import com.side.football_project.match.repository.MatchUserRepository;
import com.side.football_project.reservation.dto.ReservationResponseDto;
import com.side.football_project.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchUserServiceImpl implements MatchUserService {
    private final MatchUserRepository matchUserRepository;
    private final MatchRepository matchRepository;
    private final UserService userService;
    private final ReservationService reservationService;

    @Transactional
    @Override
    public MatchResponseDto createMatch (MatchRequestDto requestDto) {
        User user = userService.getLoginUser();

        if (!UserRole.ADMIN.equals(user.getRole())) {
            throw new CustomException(UserErrorCode.NOT_ALLOWED);
        }

        ReservationResponseDto reservation = reservationService.findReservation(requestDto.getReservationId());

        List<User> users = userService.getAllUser();

        Match match = Match.builder()
                .matchName(requestDto.getMatchName())
                .reservation(ReservationResponseDto.toDto(reservation))
                .matchDate(requestDto.getMatchDate())
                .stadium(reservation.getStadium())
                .build();

        matchRepository.save(match);

        for (User matchUser : users) {
            MatchUser matchUserEntity = MatchUser.builder()
                    .match(match)
                    .user(matchUser)
                    .build();
            matchUserRepository.save(matchUserEntity);
        }

        return MatchResponseDto.toDto(match);
    }

    /**
     * 경기 종료 후 평점 등록
     */
    @Transactional
    @Override
    public void submitMatchRatings(Long matchId, List<MatchRatingRequestDto> ratings) {
        User user = userService.getLoginUser();

        if (!UserRole.ADMIN.equals(user.getRole())) {
            throw new CustomException(UserErrorCode.NOT_ALLOWED);
        }

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("경기를 찾을 수 없습니다."));

        for (MatchRatingRequestDto ratingRequest : ratings) {
            User findUser = userService.getUserFromDB(ratingRequest.getUserId());

            // 중복 평점 방지
            boolean exists = matchUserRepository.existsByUserIdAndMatchId(findUser.getId(), matchId);
            if (exists) {
                throw new IllegalStateException("이미 해당 유저의 평점이 등록되었습니다.");
            }

            MatchUser matchParticipant = MatchUser.builder()
                    .match(match)
                    .rate(ratingRequest.getRating())
                    .user(findUser)
                    .build();
            matchUserRepository.save(matchParticipant);
        }
    }

    /**
     * 경기 종료 후 모든 참가자의 UserTier 업데이트
     */
    @Transactional
    @Override
    public void completeMatch(Long matchId) {
        User user = userService.getLoginUser();

        if (!UserRole.ADMIN.equals(user.getRole())) {
            throw new CustomException(UserErrorCode.NOT_ALLOWED);
        }

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("경기를 찾을 수 없습니다."));

        List<MatchUser> matchById = matchUserRepository.findMatchById(matchId);
        for (MatchUser matchUser : matchById) {
            userService.updateUserTier(matchUser.getUser().getId());
        }
    }
}
