package com.uuhnaut69.api.service;

import com.uuhnaut69.api.entity.Employee;
import com.uuhnaut69.api.repository.EmployeeRepository;
import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final EmployeeRepository employeeRepository;

    public void bulkData(String csvPath) throws IOException {
        CsvReader csvReader = new CsvReader();
        csvReader.setContainsHeader(true);
        CsvContainer csv = csvReader.read(Paths.get(csvPath), StandardCharsets.UTF_8);
        List<Employee> employees = new ArrayList<>();
        csv.getRows().forEach(csvRow ->
                employees.add(Employee.builder()
                        .id(Long.parseLong(csvRow.getField("Id")))
                        .employeeName(csvRow.getField("EmployeeName"))
                        .jobTitle(csvRow.getField("JobTitle"))
                        .agency(csvRow.getField("Agency"))
                        .build()));
        employeeRepository.saveAll(employees);
    }

    public void exportUsingStream(HttpServletResponse response) {
        response.addHeader("Content-Type", "application/csv");
        response.addHeader("Content-Disposition", "attachment; filename=employees.csv");
        response.setCharacterEncoding("UTF-8");
        try (Stream<Employee> employeeStream = employeeRepository.streamAll()) {
            PrintWriter out = response.getWriter();
            employeeStream.forEach((employee -> {
                String line = employeeToLine(employee);
                out.write(line);
                out.write("\n");
                entityManager.detach(employee);
            }));
            out.flush();
        } catch (IOException e) {
            log.info("Exception occurred " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while exporting results", e);
        }
    }

    public void exportWithoutStream(HttpServletResponse response) {
        final int PAGE_SIZE = 1000;
        response.addHeader("Content-Type", "application/csv");
        response.addHeader("Content-Disposition", "attachment; filename=todos.csv");
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter out = response.getWriter();
            int page = 0;
            Slice<Employee> employeePage;
            do {
                employeePage = employeeRepository.findAllBy(PageRequest.of(page, PAGE_SIZE));
                for (Employee employee : employeePage) {
                    String line = employeeToLine(employee);
                    out.write(line);
                    out.write("\n");
                }
                entityManager.clear();
                page++;
            } while (employeePage.hasNext());
            out.flush();
        } catch (IOException e) {
            log.info("Exception occurred " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while exporting results", e);
        }
    }

    private String employeeToLine(Employee employee) {
        return String.join(",", "" + employee.getId(), "" + employee.getEmployeeName(), "" + employee.getJobTitle(),
                "" + employee.getAgency());
    }
}
