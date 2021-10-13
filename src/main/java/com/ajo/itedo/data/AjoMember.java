package com.ajo.itedo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class AjoMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;

    private String lastName;

    @OneToOne
    private AjoBranch ajoBranch;

    private String cardNumber;

    private Double totalSavings;

    private String photograph;

    private Boolean isGuarantor;
}
