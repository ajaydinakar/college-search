package com.ajay.collegesearch.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MetaData {
    int page;
    int total;
    int per_page;

}
