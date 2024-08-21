package com.example.BeFETest.Scheduling.Writer;

import com.example.BeFETest.DTO.Bitcoin.BitcoinDTO;
import com.example.BeFETest.Entity.Bitcoin.BitcoinResponse;
import com.example.BeFETest.Repository.Coin.Bitcoin.BitcoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BitcoinWriter implements ItemWriter<BitcoinDTO> {

    private final BitcoinRepository bitcoinRepository;

    @Override
    @Transactional
    public void write(Chunk<? extends BitcoinDTO> chunk) throws Exception{
        Chunk<BitcoinResponse> bitcoinList = new Chunk<>();

        chunk.forEach(BitcoinDTO -> {
            BitcoinResponse bitcoinResponse = BitcoinDTO.toEntity();
            bitcoinList.add(bitcoinResponse);
        });

        bitcoinRepository.saveAll(bitcoinList);
    }
}
