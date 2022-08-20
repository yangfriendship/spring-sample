package me.youzheng.springjpastream.parking.repository;

import me.youzheng.springjpastream.parking.entity.Parkings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.QueryHint;
import java.time.LocalDate;
import java.util.stream.Stream;

public interface ParkingsRepository extends CrudRepository<Parkings, Integer> {

    @Query("select p from Parkings p where p.issueDate2 >= :from and p.issueDate2 <= :to ")
    @QueryHints(
            value = {
                    @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE, value = "" + Integer.MIN_VALUE),
                    @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "false")
            }
    )
    Stream<Parkings> collectParkings(LocalDate from, LocalDate to);

}
