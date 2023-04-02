
package com.ajay.collegesearch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonIgnoreProperties
public class CollegeSearchResponse {
    MetaData metadata;
    List<College> colleges;
}