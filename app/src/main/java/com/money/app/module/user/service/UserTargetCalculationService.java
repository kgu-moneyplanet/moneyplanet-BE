package com.money.app.module.user.service;

import com.money.app.module.tx.domain.Tx;
import com.money.app.module.tx.repository.TxRepository;
import com.money.app.module.user.domain.User;
import com.money.app.module.user.repository.UserRepository;
import com.money.app.util.common.enumtype.AbcType;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTargetCalculationService {
    // 로거 선언
    private static final Logger logger = LoggerFactory.getLogger(UserTargetCalculationService.class);

    private final TxRepository txRepository;
    private final UserRepository userRepository;

    // 7일마다 실행
    @Scheduled(cron = "0 0 0 */7 * *")  // 7일마다 0시 정각
    public void updateUserTargets() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.isAchieved()) {
                continue;
            }

            LocalDate now = LocalDate.now();
            LocalDate sevenDaysAgo = now.minusDays(7);

            List<Tx> transactions = txRepository.findTransactionsByUserIdAndDateRange(user.getId(), sevenDaysAgo, now);
            if (transactions.isEmpty()) continue;

            long totalCount = transactions.size();
            long cCount = transactions.stream()
                    .filter(tx -> tx.getAbc() == AbcType.C)
                    .count();

            // C 타입 지출이 아예 없으면 5% 증가
            double increasePercent = 0;
            if (cCount == 0) {
                increasePercent = 5;
            } else {
                // C 타입 지출 비율 계산
                double cRatio = (double) cCount / totalCount;

                // C 타입 지출 비율이 절반 이상이면 목표 증가 x
                if (cRatio < 0.5) {
                    increasePercent = 5 * (1 - cRatio); // C 타입 지출 비율에 따라 목표 증가율 조정
                }
            }

            if (increasePercent > 0) {
                user.increaseTargetByPercent(increasePercent);  // 목표 증가
                userRepository.save(user);
                logger.info("{}의 행성 도달률이 {}% 증가했습니다.", user.getName(), increasePercent);
            } else {
                logger.info("{}의 행성 도달률 증가가 없습니다.", user.getName());
            }
        }
    }
}
