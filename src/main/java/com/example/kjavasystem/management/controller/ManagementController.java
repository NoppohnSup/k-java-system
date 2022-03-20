package com.example.kjavasystem.management.controller;

import com.example.kjavasystem.management.response.TotalMoneyResponse;
import com.example.kjavasystem.management.service.ManagementService;
import com.example.kjavasystem.utils.enums.MessageResponseEnum;
import com.example.kjavasystem.utils.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManagementController {
    @Autowired
    ManagementService managementService;

    @GetMapping(value = "/bank_money/{branchId}")
    public Response getTotalMoneyFromBranch(@PathVariable int branchId, @RequestParam int employeeId){
        TotalMoneyResponse totalMoneyResponse = managementService.getTotalMoneyFromBranch(branchId, employeeId);
        return new Response(totalMoneyResponse, MessageResponseEnum.SUCCESS.getMessage());
    }
}
