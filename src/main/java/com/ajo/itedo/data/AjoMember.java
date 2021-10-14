package com.ajo.itedo.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class AjoMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @OneToOne
    @JsonIgnore
    private AjoBranch ajoBranch;

    @NotNull
    private String cardNumber;

    private Double totalSavings = BigDecimal.ZERO.doubleValue();

    private String photograph;

    private Boolean isGuarantor = Boolean.FALSE;

    private Boolean collectedLoan = Boolean.FALSE;

    @CreationTimestamp
    private LocalDate created;

    @UpdateTimestamp
    private LocalDate lastUpdated;
}
