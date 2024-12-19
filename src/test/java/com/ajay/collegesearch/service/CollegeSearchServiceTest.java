package com.ajay.collegesearch.service;

import com.ajay.collegesearch.model.College;
import com.ajay.collegesearch.model.MetaData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileNotFoundException;
import java.util.*;


@ExtendWith(MockitoExtension.class)
class CollegeSearchServiceTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    ResponseEntity responseEntity;
    @InjectMocks
    @Spy
    private CollegeSearchService collegeSearchService=new CollegeSearchService();
    @Test
    void mapResultToResponse() throws Exception {
        Mockito.when(collegeSearchService.getStudentSizeField(ArgumentMatchers.anyString())).thenReturn("latest.student.size");
        List<College> colleges=collegeSearchService.mapResultToResponse(resultlist(),"latest");
        Assertions.assertEquals(3,colleges.size());
        Assertions.assertEquals("3695",colleges.get(0).getTotalStudents());
        Assertions.assertEquals("Not Available",colleges.get(2).getCity());
        Assertions.assertEquals("Not Available",colleges.get(2).getTotalStudents());

    }

    public List<Map> resultlist()
    {
 List<Map> list=new ArrayList<>();
Map map1=new HashMap<String,String>();
        map1.put("latest.student.size" ,"3695");
        map1.put("school.name","Abilene Christian University");
        map1.put("school.city","Abilene");
        map1.put("school.zip","79699");
        map1.put("school.state","TX");
        map1.put("school.school_url","www.acu.edu/");
        map1.put("school.price_calculator_url","https://www.highered.texas.gov/apps/NPC/?Fice=003537");
        map1.put("school.accreditor","Southern Association of Colleges and Schools Commission on Colleges");

        Map map2=new HashMap<String,String>();
        map2.put("latest.student.size" ,"4982");
        map2.put("school.name","Alvin Community College");
        map2.put("school.city","Alvin");
        map2.put("school.zip","77511-4898");
        map2.put("school.state","TX");
        map2.put("school.school_url","www.alvincollege.edu/");
        map2.put("school.price_calculator_url","www.alvincollege.edu/financial-aid/netprice/npcalc.htm");
        map2.put("school.accreditor","Southern Association of Colleges and Schools Commission on Colleges");
        Map map3=new HashMap<String,String>();
        map3.put("latest.student.size" ,null);
        map3.put("school.name",null);
        map3.put("school.city",null);
        map3.put("school.zip",null);
        map3.put("school.state",null);
        map3.put("school.school_url",null);
        map3.put("school.price_calculator_url",null);
        map3.put("school.accreditor",null);
        list.add(map1);
        list.add(map2);
        list.add(map3);
return list;
    }

   @Test
    void getInfo() throws FileNotFoundException {

       Assertions.assertEquals(collegeSearchService.getInfo("2020","2,3","76308-2099","Wichita Falls","TX","100","0"
       ).getColleges().get(0).getState(),"TX");
    }


    @Test
    void getStudentField() {
        Assertions.assertEquals(collegeSearchService.getStudentSizeField(""),"latest.student.size");
        Assertions.assertEquals(collegeSearchService.getStudentSizeField("2017"),"2017.student.size");
    }


    @Test
    void getUriComponentsBuilder() {
    }

    @Test
    void getResponse() {
    }
    public UriComponentsBuilder getURI()
    {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString("URI")
                .queryParam("api_key", "")
                .queryParamIfPresent("school.degrees_awarded.predominant", Optional.ofNullable("degrees"))
                .queryParamIfPresent("school.zip", Optional.ofNullable("zip"))
                .queryParamIfPresent("school.city", Optional.ofNullable("city"))
                .queryParamIfPresent("per_page", Optional.ofNullable("per_page"))
                .queryParamIfPresent("page", Optional.ofNullable("page"))
                .queryParamIfPresent("school.state", Optional.ofNullable("state"))
                .queryParam("fields", "fields");
        return builder;
    }
    @Test
    void getInfo_withValidParameters_returnsCorrectState() throws FileNotFoundException {
        Mockito.when(collegeSearchService.getUriComponentsBuilder(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(getURI());
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), ArgumentMatchers.any(Class.class)))
                .thenReturn(new HashMap<String, Object>() {{
                    put("metadata", new MetaData());
                    put("results", resultlist());
                }});
        Assertions.assertEquals("TX", collegeSearchService.getInfo("2020", "2,3", "76308-2099", "Wichita Falls", "TX", "100", "0")
                .getColleges().get(0).getState());
    }

    @Test
    void getInfo_withNullResults_returnsEmptyColleges() throws FileNotFoundException {
        Mockito.when(collegeSearchService.getUriComponentsBuilder(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(getURI());
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), ArgumentMatchers.any(Class.class)))
                .thenReturn(new HashMap<String, Object>() {{
                    put("metadata", new MetaData());
                    put("results", null);
                }});
        Assertions.assertTrue(collegeSearchService.getInfo("2020", "2,3", "76308-2099", "Wichita Falls", "TX", "100", "0")
                .getColleges().isEmpty());
    }

    @Test
    void getInfo_withEmptyResults_returnsEmptyColleges() throws FileNotFoundException {
        Mockito.when(collegeSearchService.getUriComponentsBuilder(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(getURI());
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), ArgumentMatchers.any(Class.class)))
                .thenReturn(new HashMap<String, Object>() {{
                    put("metadata", new MetaData());
                    put("results", new ArrayList<>());
                }});
        Assertions.assertTrue(collegeSearchService.getInfo("2020", "2,3", "76308-2099", "Wichita Falls", "TX", "100", "0")
                .getColleges().isEmpty());
    }

    @Test
    void mapResultToResponse_withNullValues_returnsNotAvailable() {
        List<Map> result = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("latest.student.size", null);
        map.put("school.name", null);
        map.put("school.city", null);
        map.put("school.zip", null);
        map.put("school.state", null);
        map.put("school.school_url", null);
        map.put("school.price_calculator_url", null);
        map.put("school.accreditor", null);
        result.add(map);
        List<College> colleges = collegeSearchService.mapResultToResponse(result, "latest");
        Assertions.assertEquals("Not Available", colleges.get(0).getTotalStudents());
        Assertions.assertEquals("Not Available", colleges.get(0).getCollegeName());
        Assertions.assertEquals("Not Available", colleges.get(0).getCity());
        Assertions.assertEquals("Not Available", colleges.get(0).getZip());
        Assertions.assertEquals("Not Available", colleges.get(0).getState());
        Assertions.assertEquals("Not Available", colleges.get(0).getFeeCalaculationUrl());
        Assertions.assertEquals("Not Available", colleges.get(0).getAccreditor());
    }
}