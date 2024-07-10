package com.example.BeFETest.dto.kakaoDTO;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long kakaoId;
    @Column(nullable = false)
    private String username;
    @Column
    private String email;
}