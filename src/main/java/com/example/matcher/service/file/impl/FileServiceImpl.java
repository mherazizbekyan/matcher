package com.example.matcher.service.file.impl;

import com.example.matcher.controller.model.Employee;
import com.example.matcher.service.file.FileService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Service
public class FileServiceImpl implements FileService {

    private static final String UPLOAD_DIR = "";
    private static final String SAMPLE_CSV_FILE_PATH = "data.csv";

    //region Public methods
    @Override
    public void upload(final MultipartFile file) {
        final String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            final Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Employee> extractEmployeesFromCSV() {
        try (Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH))) {
            final CsvToBean<Employee> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(Employee.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            final List<Employee> employeeCsvModels = csvToBean.parse();
            return employeeCsvModels;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //endregion
}
