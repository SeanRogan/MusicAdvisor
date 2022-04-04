package advisor;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Controller controller = new Controller(new Service());
        while (!Controller.isExit()) {
            controller.execute(scan.nextLine().split(" +"));
        }
    }
}
