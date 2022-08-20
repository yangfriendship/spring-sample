package me.youzheng.springjpastream.parking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.youzheng.springjpastream.parking.entity.Parkings;
import me.youzheng.springjpastream.parking.repository.ParkingsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ParkingsService {

    private final ParkingsRepository parkingsRepository;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public void action() {
        LocalDate from = LocalDate.of(2017, 1, 1);
        LocalDate to = LocalDate.of(2017, 7, 1).minusDays(1);
        long startTime = System.currentTimeMillis();
        try (Stream<Parkings> parkingsStream = this.parkingsRepository.collectParkings(from, to)) {
            parkingsStream
                    .forEach(parkings -> {
                        this.entityManager.detach(parkings);
                        log.info("parkings: {}", parkings.toString());
                    })
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - startTime);
    }
}
