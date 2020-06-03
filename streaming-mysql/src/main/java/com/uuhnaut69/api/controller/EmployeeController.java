package com.uuhnaut69.api.controller;

import com.uuhnaut69.api.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author uuhnaut
 * @project streaming-mysql
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public void importData(@RequestParam(value = "csvPath", defaultValue = "") String csvPath) throws IOException {
        employeeService.bulkData(csvPath);
    }

    @GetMapping("/stream")
    public void exportUsingStream(HttpServletResponse response) {
        employeeService.exportUsingStream(response);
    }

    @GetMapping("/without-stream")
    public void exportWithOutStream(HttpServletResponse response) {
        employeeService.exportWithoutStream(response);
    }

}
