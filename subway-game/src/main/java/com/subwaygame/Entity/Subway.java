package com.subwaygame.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="subway")
@Getter
@Setter
@ToString
public class Subway {

    @Id
    @Column(name="subway_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String line;
}
