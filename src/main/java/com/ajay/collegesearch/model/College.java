package com.ajay.collegesearch.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class College {
    String collegeName;
    String city;
    String zip;
    String state;
    String website;
    String accreditor;
    String feeCalaculationUrl;
    String totalStudents;

}
