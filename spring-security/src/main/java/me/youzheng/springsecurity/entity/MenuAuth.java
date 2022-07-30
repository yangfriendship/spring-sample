package me.youzheng.springsecurity.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuAuthNo;

    private Long menuNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menuAuthGroupNo")
    private MenuAuthGroup menuAuthGroup;

}