package com.side.football_project.domain.match.service;

import com.side.football_project.domain.reservation.domain.Reservation;
import com.side.football_project.domain.reservation.dto.ReservationRequestDto;
import com.side.football_project.domain.reservation.repository.ReservationRepository;
import com.side.football_project.domain.reservation.service.ReservationService;
import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.stadium.service.StadiumService;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.service.UserService;
import com.side.football_project.domain.user.type.UserTier;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RandomMatchService implements RandomService {

    private final ReservationService reservationService;
    private final Map<String, Queue<User>> matchQueue = new HashMap<>();
    private final List<UserTier> tiers = List.of(UserTier.ROOKIE, UserTier.BEGINNER, UserTier.AMATEUR, UserTier.SEMI_PRO, UserTier.PRO);
    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final StadiumService stadiumService;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void joinMatch(Long userId) {
        User user = userService.getUserFromDB(userId);
        matchQueue.computeIfAbsent(user.getTier().name(), key -> new LinkedList<>()).add(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void fillEmptySpot(Long stadiumId) {
        Stadium stadium = stadiumService.findByIdWithLock(stadiumId);
        int current = stadium.getCurrentReservationCount();
        int capacity = stadium.getCapacity();

        UserTier avgTier = averageTier(stadium.getId());

        while (current < capacity) {
            User user = findRandomUserByTier(avgTier);

            if (user == null) {
                break;
            }

            ReservationRequestDto requestDto = ReservationRequestDto.builder()
                    .userId(user.getId())
                    .name("Random Match")
                    .stadiumId(stadium.getId())
                    .build();

            reservationService.createReservation(requestDto);
            current++;
        }
    }

    private User findRandomUserByTier(UserTier tier) {
        Queue<User> queue = matchQueue.getOrDefault(tier.name(), new LinkedList<>());

        if (!queue.isEmpty()) {
            return queue.poll();
        }

        int index = tiers.indexOf(tier);
        for (int offset = 1; offset < tiers.size(); offset++) {
            int lower = index - offset;
            int upper = index + offset;

            if (lower >= 0) {
                Queue<User> lowerQueue = matchQueue.getOrDefault(tiers.get(lower).name(), new LinkedList<>());
                if (!lowerQueue.isEmpty()) {
                    return lowerQueue.poll();
                }
            }

            if (upper < tiers.size()) {
                Queue<User> upperQueue = matchQueue.getOrDefault(tiers.get(upper).name(), new LinkedList<>());

                if (!upperQueue.isEmpty()) {
                    return upperQueue.poll();
                }
            }
        }

        return null;
    }

    private UserTier averageTier(Long stadiumId) {
        List<Reservation> reservations = reservationRepository.findAllByStadiumId(stadiumId);

        List<UserTier> userTiers = reservations
                .stream()
                .map(reservation -> reservation.getUser().getTier())
                .toList();

        if (userTiers.isEmpty()) {
            return UserTier.ROOKIE;
        }

        int sum = userTiers.stream().mapToInt(tiers::indexOf).sum();
        int avgIndex = sum / userTiers.size();

        return tiers.get(Math.min(avgIndex, tiers.size() - 1));
    }
}
