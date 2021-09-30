package sw.hv;

import org.jfree.ui.RefineryUtilities;
import sw.hv.exercise1.CombinedBitrateRetransmissionPlot;
import sw.hv.exercise1.Task_2;
import sw.hv.exercise1.Task_3;
import sw.hv.util.Utils;

public class Main {

    public static void main(String[] args) throws Exception {
        try {
            while (true) {
                int task = Utils.chooseTask();
                if (task == 2) {
                    System.out.println("Running Task 2");
                    Task_2 t2 = new Task_2();
                    t2.parseFile();
                    break;
                } else if (task == 3) {
                    System.out.println("Running Task 3");
                    Task_3 t3 = new Task_3();
                    t3.parseFile();
                    break;
                } else {
                    System.out.println("There are only 2 available tasks: 2 and 3. Please choose again");
                }
            }
        } catch (Exception e){
            throw new Exception("Check again carefully. Please enter the Task number and file path", e.getCause());
        }
    }
}
