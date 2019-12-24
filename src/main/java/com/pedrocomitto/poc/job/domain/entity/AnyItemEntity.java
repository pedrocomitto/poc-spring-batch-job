package com.pedrocomitto.poc.job.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ANY_ITEM")
@Getter
@Setter
@ToString
public class AnyItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String info1;

    private String info2;

    private String info3;

    @Column(name = "date_sync")
    private LocalDateTime dateSync;
}
