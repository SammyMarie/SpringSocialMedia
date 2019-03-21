package com.sammy.config;

import com.sammy.entity.business.PictureBusinessDTO;
import com.sammy.repository.PictureRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.actuate.endpoint.MetricReaderPublicMetrics;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.repository.InMemoryMetricRepository;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class PictureConfiguration {

    @Bean
    //@Profile("dev")
    public CommandLineRunner setup(PictureRepository repository, ConditionEvaluationReport report) throws IOException {
        String UPLOAD_ROOT = "upload-dir";

        return (args) -> {
            FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));
            Files.createDirectory(Paths.get(UPLOAD_ROOT));

            FileCopyUtils.copy("Test file", new FileWriter(UPLOAD_ROOT + "/test"));
            repository.save(PictureBusinessDTO.builder().name("test").build());

            FileCopyUtils.copy("Test file2", new FileWriter(UPLOAD_ROOT + "/test2"));
            repository.save(PictureBusinessDTO.builder().name("test2").build());

            FileCopyUtils.copy("Test file3", new FileWriter(UPLOAD_ROOT + "/test3"));
            repository.save(PictureBusinessDTO.builder().name("test3").build());

            report.getConditionAndOutcomesBySource()
                  .entrySet()
                  .stream().filter(entry -> entry.getValue().isFullMatch())
                  .forEach(entry -> System.out.println(entry.getKey() + " => " + entry.getValue().isFullMatch()));
        };
    }

    @Bean
    public InMemoryMetricRepository metricRepository(){
        return new InMemoryMetricRepository();
    }

    @Bean
    public PublicMetrics publicMetrics(){
        return new MetricReaderPublicMetrics(metricRepository());
    }
}