import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;




public class TCPThread implements Callable {
        Library library;
        Socket clientSocket;
        String command;
        
        private class CommandParser{
                String commandname;
                int clientnum;
                int booknum;
                boolean valid;
                public CommandParser(String commandname, int clientnum, int booknum, int sleeptime, boolean valid) {
                        super();
                        this.valid = valid;
                        this.commandname = commandname;
                        this.clientnum = clientnum;
                        this.booknum = booknum;
                }
                public CommandParser(boolean valid){
                        this.valid = valid;
                }
                
        }
        
        public TCPThread(Library library, Socket clientSocket, String command) {
                this.command = command;
                this.library = library;
                this.clientSocket = clientSocket;
        }
        
        public Boolean call(){
                try{
                String commandResponse = new String();
                CommandParser commandParser;
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        DataOutputStream toClient = new DataOutputStream(clientSocket.getOutputStream());
                command = command.toLowerCase();
                commandParser = parseCommand(command);
                if(commandParser.valid){
                        if(commandParser.commandname.equals("return")){
                                boolean success = library.returnBook(commandParser.booknum, commandParser.clientnum);
                                commandResponse = "c" + commandParser.clientnum + " b" + commandParser.booknum;
                                if(!success){
                                        commandResponse = "fail " + commandResponse; 
                                }
                        }
                        else if(commandParser.commandname.equals("reserve")){
                                boolean success = library.reserveBook(commandParser.booknum, commandParser.clientnum);
                                commandResponse = "c" + commandParser.clientnum + " b" + commandParser.booknum;
                                if(!success){
                                        commandResponse = "fail " + commandResponse; 
                                }
                        }
                        else{
                                commandResponse = "unexplained error"; //for debugging, should not be invoked
                        }
                }
                else{
                        commandResponse = "invalid command";
                }
                toClient.writeBytes(commandResponse + "\n");
                return true;
                }
                catch(Exception e){
                        e.printStackTrace();
                }
                return false;
        }


        private CommandParser parseCommand(String command) { //sorry this is super ugly
                StringTokenizer tokenizer = new StringTokenizer(command);
                String client = tokenizer.nextToken();
                int clientnum = -1, booknum = -1;
                if(!client.startsWith("c")) return this.new CommandParser(false);
                try{
                        clientnum = Integer.parseInt(client.substring(1));
                }
                catch(NumberFormatException e){
                        return this.new CommandParser(false);
                }
                String book = tokenizer.nextToken();
                if(!book.startsWith("b")){
                                return this.new CommandParser(false); 
                }
                try{
                        booknum = Integer.parseInt(book.substring(1));
                }
                catch(NumberFormatException e){
                        return this.new CommandParser(false);
                }
                String commandname = tokenizer.nextToken();
                if(commandname.contains("reserve")) commandname = "reserve";
                if(commandname.contains("return")) commandname = "return";
                if(commandname.equals("reserve") || commandname.equals("return")){
                        return this.new CommandParser(commandname, clientnum, booknum, 0, true);
                }
                return this.new CommandParser(false);
        }
        
        
        
}