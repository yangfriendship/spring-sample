package me.youzheng.springjpastream.parking.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Parkings {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(generator = "parking_auto_increment")
    @GenericGenerator(name = "parking_auto_increment", strategy = "increment")
    private Integer id;

    private Long summonsNumber;

    private String vehicleMake;

    private LocalDate issueDate2;

}