package com.project.webserver.controller.external.controller;

import com.project.webserver.model.logistics.Assignment;
import com.project.webserver.service.external.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AssignmentController {
    @Autowired
    private AssignmentService assignmentService;

    // Endpoint to get all assignments
    @GetMapping("api/assignments")
    public ResponseEntity<Map<String, Assignment>> getAssignments() {
        System.out.println("getAssignments");
        return assignmentService.getAssignments();
    }

    // Endpoint to get a specific assignment by ID
    @GetMapping("api/assignments/{assignmentNumber}")
    public ResponseEntity getAssignment(@PathVariable String assignmentNumber) {
//        System.out.println("getAssignment"+assignmentNumber);
        return assignmentService.getAssignment(assignmentNumber);
    }
//    @PutMapping("api/assignments/{assignmentNumber}")
//    public ResponseEntity updateAssignment(
//            @PathVariable String assignmentNumber, @RequestBody Assignment updatedAssignment) {
//        System.out.println("updateAssignment");
//        return assignmentService.updateAssignment(assignmentNumber, updatedAssignment);
//    }
}
