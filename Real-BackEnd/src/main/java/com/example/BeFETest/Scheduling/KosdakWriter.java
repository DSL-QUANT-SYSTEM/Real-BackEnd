package com.example.BeFETest.Scheduling;

import com.example.BeFETest.DTO.kosdak.KosdakResponseDTO;
import com.example.BeFETest.Entity.kosdak.KosdakResponse;
import com.example.BeFETest.Repository.Kosdak.KosdakRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KosdakWriter implements ItemWriter<KosdakResponseDTO> {

    private final KosdakRepository kosdakRepository;

    @Override
    @Transactional
    public void write(Chunk<? extends KosdakResponseDTO> chunk) throws Exception{
        Chunk<KosdakResponse> kosdakList = new Chunk<>();

        chunk.forEach(KosdakResponseDTO -> {
            KosdakResponse kosdakResponse = KosdakResponseDTO.toEntity();
            kosdakList.add(kosdakResponse);
        });

        kosdakRepository.saveAll(kosdakList);
    }
}
