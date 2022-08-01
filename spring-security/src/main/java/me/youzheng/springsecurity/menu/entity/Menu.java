package me.youzheng.springsecurity.menu.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuNo;

    @Column(length = 50)
    private String menuName;

    @Column(length = 20)
    private String httpMethod;

    @Column(length = 100)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MenuType menuType;

    @Column(nullable = false)
    private boolean isUse;

}