import java.lang.Math;
public class LamportClock {
        int c;
        public LamportClock() {
                c = 1;
        }
        
        public LamportClock(int start){
                c = start;
        }
        
        public synchronized int getValue() {
                return c;
        }
        public synchronized void tick() { // on internal actions
          c = c + 1;
          }
        public synchronized void sendAction() {
                // include c in message
                c = c + 1;
        }
        public synchronized void receiveAction(int sentValue) {
                c = Math.max(c, sentValue) + 1;
        }
        
        @Override
        public synchronized String toString(){
                return "" + c;
        }
}