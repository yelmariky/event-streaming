package fr.nextdigital.lab.account.worker;

public class TestAcount {
    public static void main(String[] args){
        String s = "http://localhost:8080/v1/events";
        System.out.println("http://hello:8087/"+s.substring(s.indexOf("v1"),s.length()));
    }
}
