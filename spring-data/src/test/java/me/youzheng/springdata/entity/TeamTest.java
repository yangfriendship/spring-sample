package me.youzheng.springdata.entity;

import org.hibernate.Hibernate;
import org.hibernate.cfg.annotations.NamedEntityGraphDefinition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/script.sql")
public class TeamTest {

    @PersistenceContext
    EntityManager entityManager;

    @BeforeEach
    public void init() {
        this.entityManager.clear();
    }

    @Test
    public void initTest() {
        List<Team> results = this.entityManager.createQuery("select t from Team t", Team.class)
                .getResultList();
        assertNotNull(this.entityManager);
        assertEquals(3, results.size());
    }

    @Test
    public void persistTest() {
        Team team = Team.builder()
                .teamName("woojungTeam")
                .build();

        this.entityManager.persist(team);
        this.entityManager.flush();
        this.entityManager.clear();
        Team result = this.entityManager.find(Team.class, team.getTeamNo());
        assertNotNull(result);
        assertNotSame(team, result);
        assertEquals(team.getTeamNo(), result.getTeamNo());
    }

    @Test
    public void fetchJoinTest() {
        List<Team> resultList = this.entityManager.createQuery("select distinct t from Team t left join Employee e on t = e.team", Team.class).getResultList();

        assertEquals(3, resultList.size());

        Team team = resultList.get(0);
    }

    @Test
    public void manyToOneJoin() {

        List<Employee> result = this.entityManager.createQuery("select e from Employee e left join Team t on e.team = t", Employee.class).getResultList();
        Employee employee = result.get(0);
        Long teamNo = employee.getTeam().getTeamNo();
        boolean result2 = Hibernate.isPropertyInitialized(employee.getTeam(), "teamNo");
        boolean result3 = Hibernate.isPropertyInitialized(employee, "team.teamName");
        System.out.println("result = " + result2);
        System.out.println("result3 = " + result3);
        assertNotNull(teamNo);
    }

}