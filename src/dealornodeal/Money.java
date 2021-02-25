package dealornodeal;

public class Money {

    private double money;

    public Money(double money){
        this.money = money;

    }
    public void setMoney(double money){
        this.money = money;
    }

    public double getMoney(){
        return money;
    }

    public String getCurrency(){
        String str = (int) money+"";
        int position = str.length() - 3;

        if (money < 1){
            return money+"";
        }else if (str.length() >= 4 && str.length() < 7){
            str = str.substring(0,position)+","+str.substring(position);
        }else if (str.length() == 7){
            str = str.substring(0,1)+","+str.substring(1,4)+","+str.substring(4);
        }
        return str;
    }

    public String toString(){
        return money+"";
    }

    public String getCurrencyFormat(){
        return "$ "+getCurrency();
    }

    public static double transformCurrencyStringToDouble(String currency){
        return Double.parseDouble(currency);
    }

    public static String removeCurrencyFormat(String currency){
        String removeDollarSign = currency.substring(2);
        String result = "";

        for (int i = 0; i < removeDollarSign.length(); i++){
            if (removeDollarSign.charAt(i) != ','){
                result += removeDollarSign.charAt(i);
            }
        }
        return result;
    }
}
