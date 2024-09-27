package com.example.BeFETest.Scheduling;

import com.example.BeFETest.Entity.SchedulingCoin.SchedulingCoinResponse;
import com.example.BeFETest.Repository.Coin.Bitcoin.SchedulingCoinRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class BithumbScheduler {
    String marketName = "https://api.bithumb.com/v1/market/all?isDetails=false";
    String currentPrice = "https://api.bithumb.com/v1/ticker";

    List<String> marketList = new ArrayList<>();

    private final SchedulingCoinRepository schedulingCoinRepository;

    @Scheduled(cron = "0 0 */2 * * *")//매 2시간 정각에 실행
    @Transactional
    public void fetchCoinData(){
        OkHttpClient client = new OkHttpClient();

        if(marketList.isEmpty()){
            //market 이름들 가져오기
            Request getMarketName = new Request.Builder()
                    .url(marketName)
                    .get()
                    .addHeader("accept", "application/json")
                    .build();

            try(Response response = client.newCall(getMarketName).execute()){
                String responseData = response.body().string();

                //JSON 파싱
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseData);

                //Json 배열에서 market 필드 추출
                for(JsonNode node : rootNode){
                    String market = node.get("market").asText();
                    marketList.add(market);
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        for(String market:marketList){
            String urlWithMarket = currentPrice + "?markets=" + market;
            Request priceRequest = new Request.Builder()
                    .url(urlWithMarket)
                    .get()
                    .addHeader("accept", "application/json")
                    .build();
            try(Response priceResponse = client.newCall(priceRequest).execute()){
                String priceData = priceResponse.body().string();
                //System.out.println(priceData);

                //JSON 파싱
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(priceData);


                 for(JsonNode node : rootNode){
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");//빗썸 api로 들어오는 data가 20240104형태이므로
                    String dateStr = node.get("trade_date").asText();
                    LocalDate date = LocalDate.parse(dateStr, formatter);
                    String closingPrice = node.get("trade_price").asText();//종가(현재가)
                    String openingPrice = node.get("opening_price").asText();
                    String highPrice = node.get("high_price").asText();
                    String lowPrice = node.get("low_price").asText();
                    String tradingVolume = node.get("acc_trade_price_24h").asText();//24시간 누적 거래금액
                    String fluctuatingRate = "";
                    if(node.get("change").asText() == "FALL"){
                        fluctuatingRate = "-" + node.get("change_rate");
                    }
                    else{
                        fluctuatingRate = "+" + node.get("change_rate");
                    }

                    SchedulingCoinResponse coinData =  SchedulingCoinResponse.builder()
                            .market(market)
                            .date(date)
                            .closingPrice(Double.parseDouble(closingPrice))
                            .openingPrice(Double.parseDouble(openingPrice))
                            .highPrice(Double.parseDouble(highPrice))
                            .lowPrice(Double.parseDouble(lowPrice))
                            .tradingVolume(tradingVolume)
                            .fluctuatingRate(fluctuatingRate)
                            .build();

                    schedulingCoinRepository.save(coinData);

                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
