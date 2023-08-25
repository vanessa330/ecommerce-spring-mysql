package com.ecom.cms.Dashboard;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface DashboardService {

    ResponseEntity<Map<String, Object>> getCount();
}
