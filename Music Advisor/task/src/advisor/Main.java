package advisor;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String serverPath = "https://accounts.spotify.com";
        Scanner scan = new Scanner(System.in);
        Controller controller = new Controller(new Service());
        if(args[0].equals("-access")){
        serverPath = args[1];
        }
        while (!Controller.isExit()) {
            controller.execute(scan.nextLine().split(" +"));
        }
    }
}
