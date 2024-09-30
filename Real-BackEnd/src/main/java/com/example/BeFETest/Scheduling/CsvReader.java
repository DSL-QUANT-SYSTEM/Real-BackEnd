package com.example.BeFETest.Scheduling;

import com.example.BeFETest.DTO.kosdaq.KosdaqResponseDTO;
import com.example.BeFETest.DTO.kospi.KospiResponseDTO;
import com.example.BeFETest.DTO.kospi200.Kospi200ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
@RequiredArgsConstructor
public class CsvReader {
    @Bean
    public FlatFileItemReader<KosdaqResponseDTO> KosdakReader(){
        FlatFileItemReader<KosdaqResponseDTO> flatFileItemReader = new FlatFileItemReader<>();

        Resource resource = new ClassPathResource("KosdakData.csv");

        flatFileItemReader.setResource(resource);
        flatFileItemReader.setEncoding("UTF-8");

        flatFileItemReader.setLinesToSkip(1);

        flatFileItemReader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());

        DefaultLineMapper<KosdaqResponseDTO> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer(",");
        delimitedLineTokenizer.setNames("date","closingPrice","openingPrice","highPrice","lowPrice","tradingVolume","fluctuatingRate");
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

        //customBeanWrapperFiledSetMapper 사용(LocalDate)
        CustomFieldSetMapper<KosdaqResponseDTO> customMapper = new CustomFieldSetMapper<>(KosdaqResponseDTO.class);
        defaultLineMapper.setFieldSetMapper(customMapper);

        flatFileItemReader.setLineMapper(defaultLineMapper);

        return flatFileItemReader;
    }


    @Bean
    public FlatFileItemReader<KospiResponseDTO> KospiReader(){
        FlatFileItemReader<KospiResponseDTO> flatFileItemReader = new FlatFileItemReader<>();

        Resource resource = new ClassPathResource("KospiData.csv");

        flatFileItemReader.setResource(resource);
        flatFileItemReader.setEncoding("UTF-8");

        flatFileItemReader.setLinesToSkip(1);

        flatFileItemReader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());

        DefaultLineMapper<KospiResponseDTO> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer(",");
        delimitedLineTokenizer.setNames("date","closingPrice","openingPrice","highPrice","lowPrice","tradingVolume","fluctuatingRate");
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

        CustomFieldSetMapper<KospiResponseDTO> customMapper = new CustomFieldSetMapper<>(KospiResponseDTO.class);
        defaultLineMapper.setFieldSetMapper(customMapper);

        flatFileItemReader.setLineMapper(defaultLineMapper);

        return flatFileItemReader;
    }

    @Bean
    public FlatFileItemReader<Kospi200ResponseDTO> Kospi200Reader(){
        FlatFileItemReader<Kospi200ResponseDTO> flatFileItemReader = new FlatFileItemReader<>();

        Resource resource = new ClassPathResource("Kospi200Data.csv");

        flatFileItemReader.setResource(resource);
        flatFileItemReader.setEncoding("UTF-8");

        flatFileItemReader.setLinesToSkip(1);

        flatFileItemReader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());

        DefaultLineMapper<Kospi200ResponseDTO> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer(",");
        delimitedLineTokenizer.setNames("date","closingPrice","openingPrice","highPrice","lowPrice","tradingVolume","fluctuatingRate");
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

        CustomFieldSetMapper<Kospi200ResponseDTO> customMapper = new CustomFieldSetMapper<>(Kospi200ResponseDTO.class);
        defaultLineMapper.setFieldSetMapper(customMapper);

        flatFileItemReader.setLineMapper(defaultLineMapper);

        return flatFileItemReader;
    }
}

