package com.ecom.cms.Dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/dashboard")
public class DashboardRest {

    @Autowired
    DashboardService dashboardService;

    @GetMapping(path = "get")
    public ResponseEntity<Map<String, Object>> getCount() {
        return dashboardService.getCount();
    }
}
