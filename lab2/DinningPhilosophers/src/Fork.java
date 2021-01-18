public class Fork {
    private boolean isUsing;

    Fork() {
        this.isUsing = false;
    }

    synchronized void pickUpFork(int id, String hand) {
        while (isUsing) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isUsing = true;
        System.out.println(Thread.currentThread().getName() + " Philosopher " + id + " " + System.nanoTime() + ": Picked up " + hand + " fork");
    }

    synchronized void putDownFork(int id, String hand) {
        isUsing = false;
        System.out.println(Thread.currentThread().getName() + " Philosopher " + id + " " + System.nanoTime() + ": Put down " + hand + " fork");
        notifyAll();
    }
}
