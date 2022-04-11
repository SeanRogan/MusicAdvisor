package advisor;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //default server path
        String serverPath = "https://accounts.spotify.com/";
        //run program with arguments ' -access {valid server path} '  to change the server path(needed for hyperSkill.org tests)
        if(args[0].equals("-access")){
            serverPath = args[1].strip();
        }
        Scanner scan = new Scanner(System.in);
        AuthServer httpAuthServer = new AuthServer(serverPath);
        Controller controller = new Controller(new Service(), httpAuthServer);

        while (!Controller.isExit()) {
            controller.execute(scan.nextLine().split(" +"));
        }
    }
}
