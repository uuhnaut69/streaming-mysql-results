package com.uuhnaut69.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author uuhnaut
 * @project streaming-mysql
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    private Long id;

    private String employeeName;

    private String jobTitle;

    private String agency;
}
