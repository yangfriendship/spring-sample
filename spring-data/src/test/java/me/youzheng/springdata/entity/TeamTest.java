package me.youzheng.springdata.entity;

import org.hibernate.Hibernate;
import org.hibernate.Interceptor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static me.youzheng.springdata.config.ScriptConfig.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/script.sql")
public class TeamTest {

    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    EntityManagerFactory entityManagerFactory;
//    @Autowired
//    Interceptor interceptor;

    @BeforeEach
    public void init() {
        this.entityManager.clear();
    }

    @Test
    public void initTest() {
        List<Team> results = this.entityManager.createQuery("select t from Team t", Team.class)
                .getResultList();
        assertNotNull(this.entityManager);
        assertEquals(TEAM_SIZE, results.size());
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

    /**
     * left join test
     */
    @Test
    public void manyToOneJoin() {
        List<Employee> result = this.entityManager.createQuery("select e from Employee e left join Team t on e.team = t", Employee.class).getResultList();
        Employee employee = result.get(0);
        assertTrue(Hibernate.isPropertyInitialized(employee.getTeam().getTeamNo(), "teamNo"));
        assertFalse(Hibernate.isPropertyInitialized(employee.getTeam(), "teamName"));

        // Team.name 에 접근하는 순간 proxy 에 의하여 fk 를 이용해 team 정보를 가져온다.
        String teamName = employee.getTeam().getTeamName();
        assertTrue(Hibernate.isPropertyInitialized(employee.getTeam(), "teamName"));
    }

    /**
     * Sample Data 의 team4 에 소속된 직원은 없다.
      */
    @Test
    public void fetchJoinTest() {
        List<Team> result = this.entityManager.createQuery("select distinct t from Team t join fetch Employee  e on e.team = t", Team.class).getResultList();
        Optional<Team> dontHaveEmployee = result.stream()
                .filter(team -> team.getTeamName().equals(NO_EMPLOYEE_TEAM_NAME))
                .findFirst();

        // fetch join 은 inner join 이기 때문에 직원이 없는 team 은 조회되지 않는다.
        assertFalse(dontHaveEmployee.isPresent());
    }

    @Test
    public void t1() {
//        this.entityManagerFactory.addNamedEntityGraph("dfasdf", new EntityGraph<Team>() {
//        });
    }

}