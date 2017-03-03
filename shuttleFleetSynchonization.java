import java.util.concurrent.Semaphore;
import java.util.Arrays;
import java.lang.*;
import java.util.Random;


class MyProcess extends Thread {
    private int id;
    private int character; // to determine if this process is a passanger 0 or a shuttle 1.
    
    public static int[] currentPassanger = new int[3];
    public static int[] currentTerminal = new int[3]; // terminal A :0, B:1,C:2,D:3,E:4,T:5;
    public static int waitingAtA=0;
    public static int waitingAtB=0;
    public static int waitingAtC=0;
    public static int waitingAtD=0;
    public static int waitingAtE=0;
    public static int waitingAtT=0;
    static Semaphore A = new Semaphore(0);
    static Semaphore B = new Semaphore(0);
    static Semaphore C = new Semaphore(0);
    static Semaphore D = new Semaphore(0);
    static Semaphore E = new Semaphore(0);
    static Semaphore T = new Semaphore(0);
    public static Semaphore [] semGo = new Semaphore [] {new Semaphore(0), new Semaphore(0), new Semaphore(0)};
    static Semaphore mutex1 = new Semaphore(1);
    static Semaphore mutex2 = new Semaphore(1);
    static Semaphore mutex3 = new Semaphore(1);
    static Semaphore mutex4 = new Semaphore(1);
    
    public static int randInt(int min, int max) { //helper function, chose random int.
        Random rn = new Random();
        int range = max - min + 1;
        int randomNum =  rn.nextInt(range) + min;
        return randomNum;
    }
    public static boolean isEmapty(int a){
        for (int i =0; i <3; i++) {
            if (currentTerminal[i] ==a) {
                return false;
            }
        }        return true;
    }
    public static int checkBus(int a){
        for (int i =0; i <3; i++) {
            if (currentTerminal[i] ==a) {
                return i;
            }
        }
        return 1000;

    }
    
    public MyProcess(int i1,int i2){
        id = i1;
        character =i2;
        
    } 
    public void run() {
        if (this.character ==0) { // the thread is a costumer.
            int T0 = randInt(0,5); //choose which station this passanger is waiting on.
            System.out.println("Passanger :"+this.id+"is waiting on the station"+ T0);
            switch (T0) {  //waiting on that station.
                case 0:
                    waitingAtA++;
                    try{
                        A.acquire();
                    }
                    catch(InterruptedException e){
                    }
                    break;
                case 1:
                    waitingAtB ++;
                    try{
                        B.acquire();
                    }
                    catch(InterruptedException e){
                    }
                    break;
                case 2:
                    waitingAtC ++;
                    try{
                        C.acquire();
                    }
                    catch(InterruptedException e){
                    }
                    break;
                case 3:
                    waitingAtD ++;
                    try{
                        D.acquire();
                    }
                    catch(InterruptedException e){
                    }
                    break;
                case 4:
                    waitingAtE ++;
                    try{
                        E.acquire();
                    }
                    catch(InterruptedException e){
                    }
                    break;
                case 5:
                    waitingAtT ++;
                    try{
                        T.acquire();
                    }
                    catch(InterruptedException e){
                    }
                    break;
                default:
                    System.out.println("there is something wrong with newwait.");
                    break;
            }
            int busIndex = checkBus(T0);
            
            try{
                mutex2.acquire();
            }
            catch(InterruptedException e){
            }

            currentPassanger[busIndex] ++;
            mutex2.release();
            System.out.println("Passanger :"+this.id+"is getting on bus: "+busIndex+" from"+ T0);
            switch (T0) {  //waiting on that station.
                case 0:
                    try{
                        mutex3.acquire();
                    }
                    catch(InterruptedException e){
                    }
                    waitingAtA --;
                    mutex3.release();
                    if (currentPassanger[busIndex] >=10 || waitingAtA <=1) {
                        semGo[busIndex].release();
                    } else {
                        A.release();
                    }
                    break;
                case 1:
                    try{
                        mutex3.acquire();
                    }
                    catch(InterruptedException e){
                    }
                    waitingAtB --;
                    mutex3.release();
                    if (currentPassanger[busIndex] >=10 || waitingAtB <=1) {
                        semGo[busIndex].release();
                    } else {
                        B.release();
                    }
                    break;
                case 2:
                    try{
                        mutex3.acquire();
                    }
                    catch(InterruptedException e){
                    }
                    waitingAtC --;
                    mutex3.release();
                    if (currentPassanger[busIndex] >=10 || waitingAtC <=1) {
                        semGo[busIndex].release();
                    } else {
                        C.release();
                    }
                    break;
                case 3:
                    try{
                        mutex3.acquire();
                    }
                    catch(InterruptedException e){
                    }
                    waitingAtD --;
                    mutex3.release();
                    if (currentPassanger[busIndex] >=10 || waitingAtD <=1) {
                        semGo[busIndex].release();
                    } else {
                        D.release();
                    }
                    break;
                case 4:
                    try{
                        mutex3.acquire();
                    }
                    catch(InterruptedException e){
                    }
                    waitingAtE --;
                    mutex3.release();
                    if (currentPassanger[busIndex] >=10 || waitingAtE <=1) {
                        semGo[busIndex].release();
                    } else {
                        E.release();
                    }
                    break;
                case 5:
                    try{
                        mutex3.acquire();
                    }
                    catch(InterruptedException e){
                    }
                    waitingAtT --;
                    mutex3.release();
                    if (currentPassanger[busIndex] >=10 || waitingAtT <=1) {
                        semGo[busIndex].release();
                    } else {
                        T.release();
                    }
                    break;
                default:
                    System.out.println("there is something wrong with newwait.");
                    break;
            }
            
            int T1 =randInt(0, 5);
            while (T1 == T0) {
                T1 = randInt(0,5);
            }
            System.out.println("Passanger :"+this.id+"is heading to "+ T1+" on bus :"+busIndex);
            
            while (currentTerminal[busIndex] !=T1) {
                continue;
            }
            currentPassanger[busIndex] --;
            if (currentPassanger[busIndex]==0) {
                semGo[busIndex].release();
            }else{
                switch (currentTerminal[busIndex]) {
                    case 0:
                        if (waitingAtA <=1){
                            semGo[busIndex].release();
                        }
                        break;
                    case 1:
                        if (waitingAtB <=1){
                            semGo[busIndex].release();
                        }
                        break;
                    case 2:
                        if (waitingAtC <=1){
                            semGo[busIndex].release();
                        }
                        break;
                    case 3:
                        if (waitingAtD <=1){
                            semGo[busIndex].release();
                        }
                        break;
                    case 4:
                        if (waitingAtE <=1){
                            semGo[busIndex].release();
                        }
                        break;
                    case 5:
                        if (waitingAtT <=1){
                            semGo[busIndex].release();
                        }
                        break;
                    default:
                        break;
                }
            }
            System.out.println("Passanger :"+this.id+"is getting off the bus: "+busIndex);
            
            
            
            
            
        } else { // this thread is a shuttle.
            for (int i =0; i <100; i++) {
                 if (!isEmapty(i%6)) {
                    //System.out.println("Station: "+i%6+" is empty or bing served, bus: "+this.id+"moves to next station");
                    
                    try{
                    mutex1.acquire();
                    }
                    catch(InterruptedException e){
                    }
                    
                    try{
                        Thread.sleep(randInt(1, 4)*100);
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mutex1.release();
                    continue;
                }else{
                    System.out.println("Bus index: "+this.id+" is at station: "+i%6);
                    
                    

                    currentTerminal[this.id] = i%6;
                    mutex1.release();
                    try{
                        Thread.sleep(200);
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    switch (currentTerminal[this.id]) {  //waiting on that station.
                        case 0:
                            if (waitingAtA >0) {
                            A.release();
                            try{
                                semGo[this.id].acquire();
                                }
                                catch(InterruptedException e){
                                
                                }
                        }else{
                            continue;
                        }
                            break;
                        case 1:
                            if (waitingAtB >0) {
                            B.release();
                            try{
                                semGo[this.id].acquire();
                            }
                            catch(InterruptedException e){
                                
                            }
                        }else{
                            continue;
                        }
                            break;
                        case 2:
                            if (waitingAtC >0) {
                            C.release();
                            try{
                                semGo[this.id].acquire();
                            }
                            catch(InterruptedException e){
                                
                            }
                        }else{
                            continue;
                        }
                            break;
                        case 3:
                            if (waitingAtD >0) {
                            D.release();
                            try{
                                semGo[this.id].acquire();
                            }
                            catch(InterruptedException e){
                                
                            }
                        }else{
                            continue;
                        }
                            break;
                        case 4:
                            if (waitingAtE >0) {
                            E.release();
                            try{
                                semGo[this.id].acquire();
                            }
                            catch(InterruptedException e){
                                
                            }
                        }else{
                            continue;
                        }
                            break;
                        case 5:
                            if (waitingAtT >0) {
                            T.release();
                            try{
                                semGo[this.id].acquire();
                            }
                            catch(InterruptedException e){
                                
                            }
                        }else{
                            continue;
                        }
                            break;
                        default:
                            System.out.println("there is something wrong with newwait.");
                            break;
                    }
                    System.out.println("Bus index: "+this.id+" is leaving station: "+i%6);
                    try{
                        Thread.sleep(200);
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
            }
        }
        
        
    }
    public static void main(String[] args){
        final int N = 51;
        MyProcess[] p = new MyProcess[N];
        for (int i =N-1; i >=0; i--){
            if (i <3) { //initialize shuttle
                p[i] = new MyProcess(i,1);
                p[i].start();
            }else{ // initialize passageer
                p[i] = new MyProcess(i,0);
                p[i].start();
            }
            
        }
    }
}