package me.youzheng.springdata.entity;

import lombok.*;

import javax.persistence.*;

@NamedEntityGraph(
        name = "employee.team",
        attributeNodes = @NamedAttributeNode("team")
)
@Entity
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long employeeNo;

    @ToString.Include
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_no", foreignKey = @ForeignKey(name = "fk_employee_team_no"))
    private Team team;

}