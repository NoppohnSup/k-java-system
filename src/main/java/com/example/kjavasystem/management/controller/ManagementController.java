package com.example.kjavasystem.management.controller;

import com.example.kjavasystem.management.reqeust.UpdateBankMoneyRequest;
import com.example.kjavasystem.management.response.TotalMoneyResponse;
import com.example.kjavasystem.management.service.ManagementService;
import com.example.kjavasystem.utils.enums.MessageResponseEnum;
import com.example.kjavasystem.utils.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class ManagementController {
    @Autowired
    ManagementService managementService;

    @GetMapping(value = "/bank_money/{branchId}")
    public Response getTotalMoneyFromBranch(@PathVariable int branchId, @RequestParam int employeeId){
        TotalMoneyResponse totalMoneyResponse = managementService.getTotalMoneyFromBranch(branchId, employeeId);
        return new Response(totalMoneyResponse, MessageResponseEnum.SUCCESS.getMessage());
    }

    @PostMapping(value = "/bank_money/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response updateBankMoney(@RequestBody UpdateBankMoneyRequest updateBankMoneyRequest){
        managementService.updateBankMoney(updateBankMoneyRequest.getEmployeeId(), updateBankMoneyRequest.getBranchId(), updateBankMoneyRequest.getMoney());
        return new Response("", MessageResponseEnum.SUCCESS.getMessage());
    }
}
