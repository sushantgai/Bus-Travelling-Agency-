
import java.util.Scanner;

class App {
    public static void main(String[] args) throws Exception {
        
        System.out.println("Welcome to Bus Agency ");
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Add Buss");
        System.out.println("2. Add Passenger");
        System.out.println("3. See Details ");

        int choice = scanner.nextInt();


        switch (choice) {
            case 1:
                Bus b = new Bus();
                b.addBuses();
                break;
            case 2:
                Passenger p = new Passenger();
                p.addPassenger();
                break;
            case 3:
                Passenger pd = new Passenger();
                pd.details();
                break;

            default:
                break;
                
        }
        scanner.close();

        


        }
}
