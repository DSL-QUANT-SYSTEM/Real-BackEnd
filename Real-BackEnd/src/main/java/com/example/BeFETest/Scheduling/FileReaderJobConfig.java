package com.example.BeFETest.Scheduling;

import com.example.BeFETest.DTO.kosdak.KosdakResponseDTO;
import com.example.BeFETest.DTO.kospi.KospiResponseDTO;
import com.example.BeFETest.DTO.kospi200.Kospi200ResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FileReaderJobConfig{
    private final CsvReader csvReader;
    private final KosdakWriter kosdakWriter;
    private final KospiWriter kospiWriter;
    private final Kospi200Writer kospi200Writer;

    @Bean
    public Job csvScheduleJob(JobRepository jobRepository, Step csvFileReader1, Step csvFileReader2, Step csvFileReader3){
        return new JobBuilder("csvScheduleJob",jobRepository)
                .start(csvFileReader1)
                .next(csvFileReader2)
                .next(csvFileReader3)
                .build();
    }

    @Bean
    public Step csvFileReader1(
            JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("csvFileReader1",jobRepository)
                .<KosdakResponseDTO, KosdakResponseDTO>chunk(10, platformTransactionManager)
                .reader(csvReader.KosdakReader())  // csv 파일 읽기 리더 설정
                //fileId:1 --> kosdaq
                //fileId:2 --> kospi
                //fileId:3 --> kospi200
                .writer(kosdakWriter)  // 받은 데이터 DB에 저장
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step csvFileReader2(
            JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("csvFileReader2",jobRepository)
                .<KospiResponseDTO, KospiResponseDTO>chunk(10, platformTransactionManager)
                .reader(csvReader.KospiReader())  // csv 파일 읽기 리더 설정
                .writer(kospiWriter)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step csvFileReader3(
            JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("csvFileReader3",jobRepository)
                .<Kospi200ResponseDTO, Kospi200ResponseDTO>chunk(10, platformTransactionManager)
                .reader(csvReader.Kospi200Reader())  // csv 파일 읽기 리더 설정
                .writer(kospi200Writer)
                .allowStartIfComplete(true)
                .build();
    }

}