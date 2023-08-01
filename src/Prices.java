import java.util.ArrayList;
import java.util.List;

public class Prices {
    private List<List<Double>> prices;

    Prices(){
    }

    Prices(List<String> pricesStrList){
        prices = new ArrayList<List<Double>>();
        for(String pricesStr:pricesStrList){
            String[] pricesArr = pricesStr.split(",");
            List<Double> temp = new ArrayList<Double>();
            for(String price:pricesArr){
                temp.add(Double.parseDouble(price.trim()));
            }
            prices.add(temp);
        }
    }

    /** 
    * retrieves price between two stops
    * @param int  starting stop.
    * @param int  ending stop.
    * @return double  price.
    */
    public double getPrice(int stopA, int stopB){
        return prices.get(stopA-1).get(stopB-1);
    }

    /** 
    * retrieves maximum price for a stop for incomplete records
    * @param int  ending stop.
    * @return double  price.
    */
    public double getMaxPrice(int stop){
        List<Double> pricesList = prices.get(stop-1);
        double maxPrice = 0.0;
        for(Double price:pricesList){
            if(price>maxPrice)
                maxPrice = price;
        }
        return maxPrice;
    }
}
