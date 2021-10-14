package com.ajo.itedo.data;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne
    private AjoMember borrower;

    @ManyToOne
    @NotNull
    private AjoMember firstGuarantor;

    @ManyToOne
    @NotNull
    private AjoMember secondGuarantor;

    @NotNull
    private Double loanAmount = BigDecimal.ZERO.doubleValue();

    @NotNull
    private Double weeklyRepayment = BigDecimal.ZERO.doubleValue();

    @NotNull
    private LocalDate dueDate;

    @CreationTimestamp
    private LocalDate created;

    @UpdateTimestamp
    private LocalDate lastUpdated;
}
