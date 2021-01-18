public class Dinning {
    public static void main(String[] args) {
        Philosopher[] philosophers = new Philosopher[5];
        Object[] forks = new Object[philosophers.length];
        for (int i = 0; i < forks.length; i++) {
            forks[i] = new Fork();
        }
        for (int i = 0; i < philosophers.length; i++) {
            philosophers[i] = new Philosopher(i + 1, forks[(i + 1) % philosophers.length], forks[i]);
        }
        for (Philosopher philosopher : philosophers) {
            new Thread(philosopher).start();
        }
    }
}
