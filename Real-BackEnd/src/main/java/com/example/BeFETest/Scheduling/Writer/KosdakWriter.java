package com.example.BeFETest.Scheduling.Writer;

import com.example.BeFETest.DTO.kosdaq.KosdaqResponseDTO;
import com.example.BeFETest.Entity.kosdaq.KosdaqResponse;
import com.example.BeFETest.Repository.Kosdaq.KosdaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KosdakWriter implements ItemWriter<KosdaqResponseDTO> {

    private final KosdaqRepository kosdaqRepository;

    @Override
    @Transactional
    public void write(Chunk<? extends KosdaqResponseDTO> chunk) throws Exception{
        Chunk<KosdaqResponse> kosdakList = new Chunk<>();

        chunk.forEach(KosdakResponseDTO -> {
            KosdaqResponse kosdaqResponse = KosdakResponseDTO.toEntity();
            kosdakList.add(kosdaqResponse);
        });

        kosdaqRepository.saveAll(kosdakList);
    }
}
