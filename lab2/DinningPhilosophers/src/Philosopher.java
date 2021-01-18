public class Philosopher implements Runnable {
    private final int id;
    private final Object leftFork;
    private final Object rightFork;

    Philosopher(int id, Object left, Object right) {
        this.id = id;
        this.leftFork = left;
        this.rightFork = right;
    }

    private void doAction(String action) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " " + action);
        Thread.sleep(((int) (Math.random() * 100)));
    }

    @Override
    public void run() {
        try {
            while (true) {
                doAction("Philosopher " + id + " " + System.nanoTime() + ": Thinking"); // thinking
                if (id % 2 == 0) {
                    ((Fork) rightFork).pickUpFork(id, "right");
                    ((Fork) leftFork).pickUpFork(id, "left");
                } else {
                    ((Fork) leftFork).pickUpFork(id, "left");
                    ((Fork) rightFork).pickUpFork(id, "right");
                }
                doAction("Philosopher " + id + " " + System.nanoTime() + " : Eating"); // eating
                if (id % 2 == 0) {
                    ((Fork) rightFork).putDownFork(id, "right");
                    ((Fork) leftFork).putDownFork(id, "left");
                } else {
                    ((Fork) leftFork).putDownFork(id, "left");
                    ((Fork) rightFork).putDownFork(id, "right");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
