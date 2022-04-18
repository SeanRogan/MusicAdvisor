package advisor;

import java.util.Scanner;

public class Main {

    public static String authServerPath = "https://accounts.spotify.com/";
    public static String apiServerPath = "https://api.spotify.com";

    public static void main(String[] args) {
        //default server paths
        //run program with arguments ' -access {valid server path} '  to change the server path(needed for hyperSkill.org tests)
        if(args[0].equals("-access")){
            authServerPath = args[1].strip();
        }
        if(args[2].equals("-resource")) {
            apiServerPath = args[3].strip();
        }
        Scanner scan = new Scanner(System.in);
        AuthServer httpAuthServer = new AuthServer(authServerPath);
        Controller controller = new Controller(httpAuthServer, apiServerPath);

        while (!Controller.isExit()) {
            controller.execute(scan.nextLine().split(" +"));
        }
        scan.close();
    }

    public static String getApiServerPath() {
        return apiServerPath;
    }
}
