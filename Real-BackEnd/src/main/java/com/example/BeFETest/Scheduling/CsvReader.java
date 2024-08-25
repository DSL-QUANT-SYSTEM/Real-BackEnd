package com.example.BeFETest.Scheduling;

import com.example.BeFETest.DTO.Bitcoin.BitcoinDTO;
import com.example.BeFETest.DTO.kosdak.KosdakResponseDTO;
import com.example.BeFETest.DTO.kospi.KospiResponseDTO;
import com.example.BeFETest.DTO.kospi200.Kospi200ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
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
    public FlatFileItemReader<KosdakResponseDTO> KosdakReader(){
        FlatFileItemReader<KosdakResponseDTO> flatFileItemReader = new FlatFileItemReader<>();

        Resource resource = new ClassPathResource("KosdakData.csv");

        flatFileItemReader.setResource(resource);
        flatFileItemReader.setEncoding("UTF-8");

        flatFileItemReader.setLinesToSkip(1);

        flatFileItemReader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());

        DefaultLineMapper<KosdakResponseDTO> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer(",");
        delimitedLineTokenizer.setNames("date","closingPrice","openingPrice","highPrice","lowPrice","tradingVolume","fluctuatingRate");
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

//        BeanWrapperFieldSetMapper<KosdakResponseDTO> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
//        beanWrapperFieldSetMapper.setTargetType(KosdakResponseDTO.class);
//
//        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

        //customBeanWrapperFiledSetMapper 사용(LocalDate)
        CustomFieldSetMapper<KosdakResponseDTO> customMapper = new CustomFieldSetMapper<>(KosdakResponseDTO.class);
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

//        BeanWrapperFieldSetMapper<KospiResponseDTO> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
//        beanWrapperFieldSetMapper.setTargetType(KospiResponseDTO.class);
//
//        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

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

//        BeanWrapperFieldSetMapper<Kospi200ResponseDTO> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
//        beanWrapperFieldSetMapper.setTargetType(Kospi200ResponseDTO.class);
//
//        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

        CustomFieldSetMapper<Kospi200ResponseDTO> customMapper = new CustomFieldSetMapper<>(Kospi200ResponseDTO.class);
        defaultLineMapper.setFieldSetMapper(customMapper);

        flatFileItemReader.setLineMapper(defaultLineMapper);

        return flatFileItemReader;
    }

    @Bean
    public FlatFileItemReader<BitcoinDTO> BitcoinReader(){
        FlatFileItemReader<BitcoinDTO> flatFileItemReader = new FlatFileItemReader<>();

        Resource resource = new ClassPathResource("BitcoinData.csv");

        flatFileItemReader.setResource(resource);
        flatFileItemReader.setEncoding("UTF-8");

        flatFileItemReader.setLinesToSkip(1);

        flatFileItemReader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());

        DefaultLineMapper<BitcoinDTO> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer(",");
        delimitedLineTokenizer.setNames("date","closingPrice","openingPrice","highPrice","lowPrice","tradingVolume","fluctuatingRate");
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

//        BeanWrapperFieldSetMapper<BitcoinDTO> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
//        beanWrapperFieldSetMapper.setTargetType(BitcoinDTO.class);
//
//        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

        CustomFieldSetMapper<BitcoinDTO> customMapper = new CustomFieldSetMapper<>(BitcoinDTO.class);
        defaultLineMapper.setFieldSetMapper(customMapper);

        flatFileItemReader.setLineMapper(defaultLineMapper);

        return flatFileItemReader;
    }

}

