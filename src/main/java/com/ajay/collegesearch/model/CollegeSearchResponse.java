
package com.ajay.collegesearch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class CollegeSearchResponse {
    MetaData metadata;
    List<College> colleges;
}