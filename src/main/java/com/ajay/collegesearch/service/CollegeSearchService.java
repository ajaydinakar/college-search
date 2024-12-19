package com.ajay.collegesearch.service;

import com.ajay.collegesearch.model.College;
import com.ajay.collegesearch.model.CollegeSearchResponse;
import com.ajay.collegesearch.model.MetaData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CollegeSearchService {
    @Value("${api.key}")
    private String apiKey;
    public CollegeSearchResponse getInfo(String year, String degrees, String zip, String city,String state, String per_page, String page) {
        RestTemplate restTemplate=new RestTemplate();
        final String URI = "https://api.data.gov/ed/collegescorecard/v1/schools.json";

        String fields = getStudentSizeField(year)+
                ",school.name,school.city,school.zip,school.state,school.school_url,school.price_calculator_url,school.accreditor";
        UriComponentsBuilder builder = getUriComponentsBuilder(degrees, zip, city, state, per_page, page, URI, fields);
        Map collegeScoreCardResponse = restTemplate.getForObject(builder.toUriString().replaceAll("%20"," "), Map.class);
        ObjectMapper mapper = new ObjectMapper();
        MetaData metadata = mapper.convertValue(collegeScoreCardResponse.get("metadata"), MetaData.class);
        List<Map> list = (List<Map>) collegeScoreCardResponse.get("results");
        CollegeSearchResponse collegeSearchResponse = getResponse(metadata, list,year);
        return collegeSearchResponse;
    }

    public  UriComponentsBuilder getUriComponentsBuilder(String degrees, String zip, String city, String state, String per_page, String page, String URI, String fields) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(URI)
                .queryParam("api_key", apiKey)
                .queryParamIfPresent("school.degrees_awarded.predominant", Optional.ofNullable(degrees))
                .queryParamIfPresent("school.zip", Optional.ofNullable(zip))
                .queryParamIfPresent("school.city", Optional.ofNullable(city))
                .queryParamIfPresent("per_page", Optional.ofNullable(per_page))
                .queryParamIfPresent("page", Optional.ofNullable(page))
                .queryParamIfPresent("school.state", Optional.ofNullable(state))
                .queryParam("fields", fields);
        return builder;
    }

    public CollegeSearchResponse getResponse(MetaData metadata, List<Map> list,String year) {
        Comparator<College> compareByCollegeName = Comparator.comparing(College::getCollegeName);
        CollegeSearchResponse collegeSearchResponse = new CollegeSearchResponse(metadata, mapResultToResponse(list,year)
                .stream()
                .sorted(compareByCollegeName)
                .collect(Collectors.toList()));
        return collegeSearchResponse;
    }

    public List<College> mapResultToResponse(List<Map> result,String year) {
        List<College> colleges = new ArrayList<>();
        result.stream().forEach((college) -> colleges.add(new College(
                (college.get("school.name") == null) ? "Not Available" : college.get("school.name").toString(),
                (college.get("school.city") == null) ? "Not Available" : college.get("school.city").toString(),
                (college.get("school.zip") == null) ? "Not Available" : college.get("school.zip").toString(),
                (college.get("school.state") == null) ? "Not Available" : college.get("school.state").toString(),
                (college.get("school.school_url") == null) ? "Not Available" : college.get("school.school_url").toString(),
                (college.get("school.accreditor") == null) ? "Not Available" : college.get("school.accreditor").toString(),
                (college.get("school.price_calculator_url") == null) ? "Not Available" : college.get("school.price_calculator_url").toString(),
                ((college.get(getStudentSizeField(year)) == null ? "Not Available" :  college.get(getStudentSizeField(year)).toString())))));
        return colleges;
    }
    public  String getStudentSizeField( String year)
    {
        String size=new String();
        if (year.isEmpty()) {
            size="latest.student.size";
        } else {
            size=year + ".student.size";
        }
        return size;
    }
}
