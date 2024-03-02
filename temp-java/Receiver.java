public class Receiver {

   //read from .env file to get environment variables, using scanner


    public void printing() throws java.io.FileNotFoundException {
        java.util.Scanner scanner = new java.util.Scanner(new java.io.File(".env"));
        String SOLACE_HOST = "";
    while(scanner.hasNextLine()){
        String line = scanner.nextLine();
        String[] parts = line.split("=");
        if(parts.length >= 2){
             String name = parts[0];
             String value = parts[1];
             if(name.equals("SOLACE_HOST")){
               SOLACE_HOST = value;
             }
        }
    }
    System.out.println("SOLACE_HOST: " + SOLACE_HOST);
    

}
}