package com.ajo.itedo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private AjoMember borrower;

    @ManyToOne
    private AjoMember firstGuarantor;

    @ManyToOne
    private AjoMember secondGuarantor;

    private Double loanAmount;

    private Double weeklyRepayment;

    private LocalDate dueDate;

    @CreatedDate
    private LocalDate created;
}
