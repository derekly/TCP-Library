import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;


class TCPClientPt2 {
        public static void main(String args[]) throws Exception {
                if(args.length != 1){
                           System.out.println("expect 1 argument");
                           return;
                }
                preProcess(args[0]);
                Scanner infile = new Scanner(new File(args[0]));
                int servernum = infile.nextInt();
                ArrayList<InetAddress> IPAddresses = new ArrayList<InetAddress>();
                int[] ports = new int[servernum];
                for(int ip = 0; ip < servernum; ip++){
                        String[] ipPort = infile.next().split(":");
                        InetAddress IPAddress = InetAddress.getByName(ipPort[0]);
                        IPAddresses.add(IPAddress);
                        ports[ip] = Integer.parseInt(ipPort[1]);
                }
                Socket clientSocket = new Socket();
                infile.nextLine();
                while(infile.hasNext()){
                        String command = infile.nextLine();
                        StringTokenizer commandParser = new StringTokenizer(command);
                        if(commandParser.countTokens() == 2){
                                String millis = commandParser.nextToken();
                                millis = commandParser.nextToken();
                                int sleep = Integer.parseInt(millis);
                                Thread.sleep(sleep);
                                continue;
                        }
                        boolean connected = false;
                        int srv = 0;
                        String commandresult = "";
                        while(!connected){
                                try{
                                        if(srv == servernum) srv = 0;
                                        clientSocket = new Socket();
                                        clientSocket.connect(new InetSocketAddress(IPAddresses.get(srv), ports[srv]), 100);
                                        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                                        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                                        outToServer.writeBytes(command + "\n");
                                        //System.out.println("write complete");
                                        commandresult = inFromServer.readLine();
                                        //System.out.println("read complete");
                                        connected = true;
                                }
                                catch(IOException e){
                                        srv++;
                                }
                        }
                        System.out.println(commandresult);
                }
    clientSocket.close();
    infile.close();
        }
        
        
        public static void preProcess(String dir) throws IOException {
                File file = new File(dir);
                if (!file.exists() || !file.isFile()) {
                        throw new IOException("input is not a file");
                }
                if (!file.getName().endsWith(".in")) {
                                throw new IOException("input file does not end with .in");
                        
                }
        }
}