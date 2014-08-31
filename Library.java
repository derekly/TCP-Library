import java.util.StringTokenizer;
import java.util.concurrent.locks.ReentrantLock;




public class Library {


        private int[] books;
        private ReentrantLock lock;
        
        public Library(int booknumber) {
                lock = new ReentrantLock(true);
                this.books = new int[booknumber];
                for(int i = 0; i < books.length; i++){
                        books[i] = 0;
                }
        }
        
        public int getSize(){
                return books.length;
        }
        
        public boolean reserveBook(int booknum, int client){
                lock.lock();
                try{
                        if(books[booknum] != 0){
                                return false;
                        }
                        else{
                                books[booknum] = client;
                                return true;
                        }
                }
                finally{
                        lock.unlock();
                }
        }
        
        public boolean returnBook(int booknum, int client){
                lock.lock();
                try{
                        if(books[booknum] != client){
                                return false;
                        }
                        else{
                                books[booknum] = 0;
                                return true;
                        }
                }
                finally{
                        lock.unlock();
                }
        }


        @Override
        public String toString(){
                String myLibrary = "";
                for (int i = 0; i < books.length; i++ ){
                        myLibrary = myLibrary + books[i] + " ";
                } 
                
                return myLibrary;
        }


        public void update(String updater){
                StringTokenizer st = new StringTokenizer(updater);
                int i = 0;
                while(st.hasMoreElements()){
                        books[i] = Integer.parseInt(st.nextToken());
                        i++;
                }
        }
}