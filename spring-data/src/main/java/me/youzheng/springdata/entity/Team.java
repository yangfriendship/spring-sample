package me.youzheng.springdata.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Team {

    /**
     * auto_increment 가 아니네..
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long teamNo;

    private String teamName;

    @OneToMany(mappedBy = "team")
    private List<Employee> employees = new ArrayList<>();

}