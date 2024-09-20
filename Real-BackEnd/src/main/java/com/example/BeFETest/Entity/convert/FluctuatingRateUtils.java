package com.example.BeFETest.Entity.convert;

public class FluctuatingRateUtils {

    public static Double convertFluctuatingRate(String fluctuatingRate){
        try{
            if(fluctuatingRate.startsWith("+") || fluctuatingRate.startsWith("-")){
                return Double.parseDouble(fluctuatingRate);
            } else if(fluctuatingRate.equals("0") || fluctuatingRate.equals("+0") || fluctuatingRate.equals("-0")){
                return 0.0;
            } else {
                return Double.parseDouble(fluctuatingRate);
            }
        } catch (NumberFormatException e){
            return -9999.0;
        }
    }
}
