package sw.hv;

import sw.hv.exercise1.E1_Task2;
import sw.hv.exercise1.E1_Task3;
import sw.hv.exercise2.E2_Task1;
import sw.hv.util.GeneralHelper;

public class Main {

    public static void main(String[] args) throws Exception {
        try {
            while (true) {
                String[] exAndTask = GeneralHelper.chooseTask();
                // Exercise 1
                if (exAndTask[0].equals("1")) {
                    if (exAndTask[1].equals("2")){
                        System.out.println("Running exercise 1 - Task 2");
                        E1_Task2 t2 = new E1_Task2();
                        // path: /Users/hungvu/Desktop/E7130/e1/output/t2_pingresult.txt
                        t2.parseFile();
                    }
                    if (exAndTask[1].equals("3")){
                        System.out.println("Running exercise 1 - Task 3");
                        E1_Task3 t3 = new E1_Task3();
                        // path: /Users/hungvu/Desktop/E7130/e1/from_linux/t3_result.json
                        t3.parseFile();
                    }
                    break;
                }
                // Exercise 2
                else if (exAndTask[0].equals("2")) {
                    if(exAndTask[1].equals("1")){
                        System.out.println("Running exercise 2 - Task 1");
                        E2_Task1 t1 = new E2_Task1();
                        t1.parseFile();
                    }
                    if(exAndTask[1].equals("2")){
                        System.out.println("Running exercise 2 - Task 2");
                    }
                    break;
                } else {
                    System.out.println("Check the available task for each exercise, input them separated with comma and choose again");
                }
            }
        } catch (Exception e){
            throw new Exception("Check again carefully. Please enter the Task number and file path", e);
        }
    }
}
