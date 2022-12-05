package com.mybucketlistbook.entity;

import lombok.*;
import org.hibernate.annotations.Table;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Entity
@Table(name = "entry")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Entry {

    @Id
    @Column(name = "entry_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer entryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="game_id")
    private Game game;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "count")
    private Integer count;

}
