package model;

public class Gibbet {

    public static void printGibbet(){
        System.out.println("----------------------");
        System.out.println("|                    |");
        System.out.println("|");
        System.out.println("|");
        System.out.println("|");
        System.out.println("|");
        System.out.println("|");
        System.out.println("|________________________________________");
    }

    public static void printGibbet3Attempts(){
        System.out.println("----------------------");
        System.out.println("|                    |");
        System.out.println("|                    O");
        System.out.println("|                     ");
        System.out.println("|                     ");
        System.out.println("|");
        System.out.println("|");
        System.out.println("|________________________________________");
    }

    public static void printGibbet2Attempts(){
        System.out.println("----------------------");
        System.out.println("|                    |");
        System.out.println("|                    O");
        System.out.println("|                    |");
        System.out.println("|                     ");
        System.out.println("|");
        System.out.println("|");
        System.out.println("|________________________________________");
    }

    public static void printGibbet1Attempts(){
        System.out.println("----------------------");
        System.out.println("|                    |");
        System.out.println("|                    O");
        System.out.println("|                   (|)");
        System.out.println("|                      ");
        System.out.println("|");
        System.out.println("|");
        System.out.println("|________________________________________");
    }

    public static void printGibbetGameOver(){
        System.out.println("----------------------");
        System.out.println("|                    |");
        System.out.println("|                    O");
        System.out.println("|                   (|)");
        System.out.println("|                   ( )");
        System.out.println("|");
        System.out.println("|");
        System.out.println("|___________YOU HAVE FAILED!____________");
    }
}
