package com.example.BeFETest.Scheduling.Writer;

import com.example.BeFETest.DTO.kospi200.Kospi200ResponseDTO;
import com.example.BeFETest.Entity.kospi200.Kospi200Response;
import com.example.BeFETest.Repository.Kospi.Kospi200Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class Kospi200Writer implements ItemWriter<Kospi200ResponseDTO> {

    private final Kospi200Repository kospi200Repository;

    @Override
    @Transactional
    public void write(Chunk<? extends Kospi200ResponseDTO> chunk) throws Exception{
        Chunk<Kospi200Response> kospi200List = new Chunk<>();

        chunk.forEach(Kospi200ResponseDTO -> {
            Kospi200Response kospi200Response = Kospi200ResponseDTO.toEntity();
            kospi200List.add(kospi200Response);
        });

        kospi200Repository.saveAll(kospi200List);
    }
}
