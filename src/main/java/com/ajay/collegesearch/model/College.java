package com.ajay.collegesearch.model;

import lombok.*;

@Data
@AllArgsConstructor
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
