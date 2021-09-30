package by.khmara.util;

public class CommonUtil {

    public static void delay(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}