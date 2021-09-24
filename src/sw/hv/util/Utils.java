package sw.hv.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Utils {

    public static int chooseTask(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter task number: ");
        return sc.nextInt();
    }

    public static String readInput() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter path to input file: ");
        return sc.nextLine();
    }

    public static boolean isValidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
        return true;
    }

    public static boolean isValidFile (String path) {
        File file = new File(path);
        if (!file.isDirectory())
            file = file.getParentFile();
        return file.exists();
    }

    public static List<String> extractNumberFromString(String fullStr){
        fullStr = fullStr.replaceAll("[^-?0-9.]+", " ");
        return Arrays.asList(fullStr.trim().split(" "));
    }
}
