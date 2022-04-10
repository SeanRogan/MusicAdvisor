package advisor;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String serverPath = "https://accounts.spotify.com/";
        if(args[0].equals("-access")){
            serverPath = args[1].strip();
        }
        Scanner scan = new Scanner(System.in);
        Server httpServer = new Server(serverPath);
        Controller controller = new Controller(new Service(),httpServer);

        while (!Controller.isExit()) {
            controller.execute(scan.nextLine().split(" +"));
        }
    }
}
