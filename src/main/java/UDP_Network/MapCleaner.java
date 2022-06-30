package UDP_Network;

public class MapCleaner extends Thread {

    public boolean isActive = true;

    MapCleaner() {
        this.start();
    }

    @Override
    public void run() {
        while (isActive) {
            try {
                this.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!isActive) break;
            StoreServerUDP.clearMaps();
        }
    }
}