package com.example.BeFETest.Scheduling.Writer;

import com.example.BeFETest.DTO.kospi.KospiResponseDTO;
import com.example.BeFETest.Entity.kospi.KospiResponse;
import com.example.BeFETest.Repository.Kospi.KospiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KospiWriter implements ItemWriter<KospiResponseDTO> {
    private final KospiRepository kospiRepository;

    @Override
    @Transactional
    public void write(Chunk<? extends KospiResponseDTO> chunk) throws Exception{
        Chunk<KospiResponse> kospiList = new Chunk<>();

        chunk.forEach(KospiResponseDTO -> {
            KospiResponse kospiResponse = KospiResponseDTO.toEntity();
            kospiList.add(kospiResponse);
        });

        kospiRepository.saveAll(kospiList);
    }

}
