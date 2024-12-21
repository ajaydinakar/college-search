package com.ajay.collegesearch.service;

import com.ajay.collegesearch.model.College;
import com.ajay.collegesearch.model.CollegeSearchResponse;
import com.ajay.collegesearch.model.MetaData;
import com.ajay.collegesearch.util.CollegeConstants;
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
    @Value("${api.url}")
    private String apiUrl;
    public CollegeSearchResponse getInfo(String year, String degrees, String zip, String city,String state, String per_page, String page) {
        RestTemplate restTemplate=new RestTemplate();
        final String URI = apiUrl;
        String fields = getStudentSizeField(year) +
                "," + CollegeConstants.SCHOOL_NAME +
                "," + CollegeConstants.SCHOOL_CITY +
                "," + CollegeConstants.SCHOOL_ZIP +
                "," + CollegeConstants.SCHOOL_STATE +
                "," + CollegeConstants.SCHOOL_URL +
                "," + CollegeConstants.SCHOOL_PRICE_CALCULATOR_URL +
                "," + CollegeConstants.SCHOOL_ACCREDITOR;
        UriComponentsBuilder builder = getUriComponentsBuilder(degrees, zip, city, state, per_page, page, URI, fields);
        try {
            Map collegeScoreCardResponse = restTemplate.getForObject(builder.toUriString().replaceAll("%20", " "), Map.class);
            ObjectMapper mapper = new ObjectMapper();
            MetaData metadata = mapper.convertValue(collegeScoreCardResponse.get("metadata"), MetaData.class);
            List<Map> list = (List<Map>) collegeScoreCardResponse.get("results");
            return getResponse(metadata, list, year);
        } catch (Exception e) {
            log.error("Error occurred while fetching college information", e);
            return new CollegeSearchResponse();
        }
    }

    public  UriComponentsBuilder getUriComponentsBuilder(String degrees, String zip, String city, String state, String per_page, String page, String URI, String fields) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(URI)
                .queryParam("api_key", apiKey)
                .queryParamIfPresent(CollegeConstants.SCHOOL_DEGREES_AWARDED_PREDOMINANT, Optional.ofNullable(degrees))
                .queryParamIfPresent(CollegeConstants.SCHOOL_ZIP, Optional.ofNullable(zip))
                .queryParamIfPresent(CollegeConstants.SCHOOL_CITY, Optional.ofNullable(city))
                .queryParamIfPresent(CollegeConstants.PER_PAGE, Optional.ofNullable(per_page))
                .queryParamIfPresent(CollegeConstants.PAGE, Optional.ofNullable(page))
                .queryParamIfPresent(CollegeConstants.SCHOOL_STATE, Optional.ofNullable(state))
                .queryParam(CollegeConstants.FIELDS, fields);
        return builder;
    }

    public CollegeSearchResponse getResponse(MetaData metadata, List<Map> list,String year) {
        Comparator<College> compareByCollegeName = Comparator.comparing(College::getCollegeName);
        return new CollegeSearchResponse(metadata, mapResultToResponse(list,year)
                .stream()
                .sorted(compareByCollegeName)
                .collect(Collectors.toList()));
    }

    public List<College> mapResultToResponse(List<Map> result,String year) {
        List<College> colleges = new ArrayList<>();
        result.forEach((college) -> colleges.add(new College(
                (college.get(CollegeConstants.SCHOOL_NAME) == null) ? CollegeConstants.NOT_AVAILABLE : college.get(CollegeConstants.SCHOOL_NAME).toString(),
                (college.get(CollegeConstants.SCHOOL_CITY) == null) ? CollegeConstants.NOT_AVAILABLE : college.get(CollegeConstants.SCHOOL_CITY).toString(),
                (college.get(CollegeConstants.SCHOOL_ZIP) == null) ? CollegeConstants.NOT_AVAILABLE : college.get(CollegeConstants.SCHOOL_ZIP).toString(),
                (college.get(CollegeConstants.SCHOOL_STATE) == null) ? CollegeConstants.NOT_AVAILABLE: college.get(CollegeConstants.SCHOOL_STATE).toString(),
                (college.get(CollegeConstants.SCHOOL_URL) == null) ? CollegeConstants.NOT_AVAILABLE: college.get(CollegeConstants.SCHOOL_URL).toString(),
                (college.get(CollegeConstants.SCHOOL_ACCREDITOR) == null) ? CollegeConstants.NOT_AVAILABLE: college.get(CollegeConstants.SCHOOL_ACCREDITOR).toString(),
                (college.get(CollegeConstants.SCHOOL_PRICE_CALCULATOR_URL) == null) ? CollegeConstants.NOT_AVAILABLE: college.get(CollegeConstants.SCHOOL_PRICE_CALCULATOR_URL).toString(),
                ((college.get(getStudentSizeField(year)) == null ? CollegeConstants.NOT_AVAILABLE:  college.get(getStudentSizeField(year)).toString())))));
        return colleges;
    }
    public  String getStudentSizeField( String year)
    {
        String size;
        if (year.isEmpty()) {
            size= CollegeConstants.LATEST_STUDENT_SIZE;
        } else {
            size=year + CollegeConstants.STUDENT_SIZE_SUFFIX;
        }
        return size;
    }
}
