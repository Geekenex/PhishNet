//Temp file, testing solace functionality in Java

public class Driver {
    public static void main(String[] args) {
        try {
            Receiver r = new Receiver();
            r.printing();
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

}
