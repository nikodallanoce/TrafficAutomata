package pkg_ale;

public class Main_ale {
    public static void main(String[] args) {
        Scenario scenario = new Scenario(2, 8, 0.5, 2, 0.1, 0.5, 1);
        System.out.println(scenario);
        for (int i=0;i<5;i++) {
            scenario.run_step();
            System.out.println(scenario);
        }
    }
}
