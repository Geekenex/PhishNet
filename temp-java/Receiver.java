public class Receiver {
    public void printing() {
        SolaceCredentials.getCredentials();

        System.out.println("SOLACE_HOST: " + SolaceCredentials.SOLACE_HOST);
    }
}