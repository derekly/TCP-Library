import java.util.StringTokenizer;




public class Request implements Comparable<Request>{
        int serverID;
        String command;
        int timestamp; 
        
        //Constructor from Data
        public Request(int sID, String cmd, int ts){
                serverID = sID;
                command = cmd;
                timestamp = ts;
        }
        
        //Constructor for rebuilding data for down server
        public Request(String fromString){
                StringTokenizer st = new StringTokenizer(fromString);
                serverID = Integer.parseInt(st.nextToken());
                command = "";
                while(st.countTokens() > 1){
                        command += st.nextToken() + " ";
                }
                timestamp = Integer.parseInt(st.nextToken().substring(2));
        }
        
        //for use in priority queue
        @Override
        public int compareTo(Request r) {
                int compare = Integer.compare(timestamp, r.timestamp);
                if(compare == 0){
                        return Integer.compare(serverID, r.serverID);
                }
                return compare;
                
        } 


        //for sending to an initializing server
        @Override
        public String toString(){
                return "" + serverID + " " + command + " ts" + timestamp+ " ";
        }
        
        public boolean isMyTurn(int serverID){
                return (this.serverID == serverID);
        }
        
}