package com.ajo.itedo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String message;

    @OneToOne(cascade = CascadeType.PERSIST)
    private AjoBranch ajoBranch;

    private ACTIVITYLOGSTATUS activityStatus;

    @CreationTimestamp
    private LocalDateTime created;
}
