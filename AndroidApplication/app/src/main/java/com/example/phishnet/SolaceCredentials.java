public class SolaceCredentials {
    public static String SOLACE_HOST = "";
    public static String SOLACE_USERNAME = "";
    public static String SOLACE_PASSWORD = "";
    public static String SOLACE_VPN = "";
    public static String SOLACE_RECEIVING_QUEUE_NAME = "";
    public static String SOLACE_SENDING_QUEUE_NAME = "";

    public static void getCredentials() {
        try {
            java.util.Scanner scanner = new java.util.Scanner(new java.io.File(".env"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("=");
                switch (parts[0]) {
                    case "SOLACE_HOST":
                        SOLACE_HOST = parts[1];
                        break;
                    case "SOLACE_USERNAME":
                        SOLACE_USERNAME = parts[1];
                        break;
                    case "SOLACE_PASSWORD":
                        SOLACE_PASSWORD = parts[1];
                        break;
                    case "SOLACE_VPN":
                        SOLACE_VPN = parts[1];
                        break;
                    case "SOLACE_RECEIVING_QUEUE_NAME":
                        SOLACE_RECEIVING_QUEUE_NAME = parts[1];
                        break;
                    case "SOLACE_SENDING_QUEUE_NAME":
                        SOLACE_SENDING_QUEUE_NAME = parts[1];
                        break;
                }
            }
            scanner.close();
        } catch (java.io.FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }
}
