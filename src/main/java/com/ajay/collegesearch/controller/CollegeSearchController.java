package com.ajay.collegesearch.controller;


import com.ajay.collegesearch.model.College;
import com.ajay.collegesearch.model.CollegeSearchResponse;
import com.ajay.collegesearch.service.CollegeSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CollegeSearchController {
@Autowired
    private CollegeSearchService collegeSearchService;
    @GetMapping("/")
  public String getColleges()
  {
      return "Hi Ajay";
  }


    @GetMapping("/colleges")
    public ResponseEntity<CollegeSearchResponse> getRawCollegeList(@RequestParam(name="year") String year,
                                                                   @RequestParam(name="degrees",required = false) String degrees,
                                                                   @RequestParam(name="zipcode",required = false) String zipcode ,
                                                                   @RequestParam(name="city",required = false) String city,
                                                                   @RequestParam(name="state",required = false) String state,
                                                                   @RequestParam(name="per_page",required = false) String per_page ,
                                                                   @RequestParam(name="page",required = false) String page )
    {
        CollegeSearchResponse response=collegeSearchService.getInfo(year,degrees,zipcode,city,state,per_page,page);
        return ResponseEntity.ok().body(response);
    }


}
