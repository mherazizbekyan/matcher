package com.example.matcher.service.file;

import com.example.matcher.controller.model.Employee;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface FileService {

    void upload(final MultipartFile file);

    List<Employee> extractEmployeesFromCSV() throws IOException;
}
