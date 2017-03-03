import java.util.concurrent.Semaphore;
import java.util.Arrays;
import java.lang.*;
import java.util.Random;


class MyProcess extends Thread {
	private int id;
	private int character; // to determine if this process is a passanger 0 or a shuttle 1.
	
	public static int currentPassanger = 0;
	public static int currentTerminal = 0; // terminal A :0, B:1,C:2,D:3,E:4,T:5;
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
	static Semaphore semGo = new Semaphore(0);
	static Semaphore mutex1 = new Semaphore(1);
	
	public static int randInt(int min, int max) { //helper function, chose random int.
		Random rn = new Random();
		int range = max - min + 1;
		int randomNum =  rn.nextInt(range) + min;
		return randomNum;
	}
	
	public MyProcess(int i1,int i2){
		  id = i1;
		  character =i2;
	
	}	
	public void run() {
		if (this.character ==0) { // the thread is a costumer.
			int T0 = randInt(0,5);	//choose which station this passanger is waiting on.
			System.out.println("Passanger :"+this.id+"is waiting on the station"+ T0);
			switch (T0) {		//waiting on that station.
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
			System.out.println("Passanger :"+this.id+"is getting on bus from"+ T0);
			try{
				mutex1.acquire();
			}
			catch(InterruptedException e){
			}
			currentPassanger ++;
			mutex1.release();
			switch (T0) {		//waiting on that station.
				case 0:
					waitingAtA --;
					if (currentPassanger ==10 || waitingAtA ==0) {
						semGo.release();
					} else {
						A.release();
					}
					break;
				case 1:
					waitingAtB --;
					if (currentPassanger ==10 || waitingAtB ==0) {
						semGo.release();
					} else {
						B.release();
					}
					break;
				case 2:
					waitingAtC --;
					if (currentPassanger ==10 || waitingAtC ==0) {
						semGo.release();
					} else {
						C.release();
					}
					break;
				case 3:
					waitingAtD --;
					if (currentPassanger ==10 || waitingAtD ==0) {
						semGo.release();
					} else {
						D.release();
					}
					break;
				case 4:
					waitingAtE --;
					if (currentPassanger ==10 || waitingAtE ==0) {
						semGo.release();
					} else {
						E.release();
					}
					break;
				case 5:
					waitingAtT --;
					if (currentPassanger ==10 || waitingAtT ==0) {
						semGo.release();
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
			System.out.println("Passanger :"+this.id+"is heading to"+ T1);

			while (currentTerminal !=T1) {
				System.out.print("");
			}
			try{
				mutex1.acquire();
			}
			catch(InterruptedException e){
			}
			currentPassanger --;
			mutex1.release();
			System.out.println("Passanger :"+this.id+"is getting off the bus.");

			
			
			
			
		} else { // this thread is a shuttle.
			for (int i =0; i <30; i++) {
				System.out.println("Bus is at station: "+i%6);
				currentTerminal = i%6;
				switch (currentTerminal) {		//waiting on that station.
					case 0:
						if (waitingAtA !=0) {
							A.release();
							try{
								semGo.acquire();
							}
							catch(InterruptedException e){
								
							}
						}
						break;
					case 1:
						if (waitingAtB !=0) {
							B.release();
							try{
								semGo.acquire();
							}
							catch(InterruptedException e){
								
							}
						}
						break;
					case 2:
						if (waitingAtC !=0) {
							C.release();
							try{
								semGo.acquire();
							}
							catch(InterruptedException e){
								
							}
						}
						break;
					case 3:
						if (waitingAtD !=0) {
							D.release();
							try{
								semGo.acquire();
							}
							catch(InterruptedException e){
								
							}
						}
						break;
					case 4:
						if (waitingAtE !=0) {
							E.release();
							try{
								semGo.acquire();
							}
							catch(InterruptedException e){
								
							}
						}
						break;
					case 5:
						if (waitingAtT !=0) {
							T.release();
							try{
								semGo.acquire();
							}
							catch(InterruptedException e){
								
							}
						}
						break;
					default:
						System.out.println("there is something wrong with newwait.");
						break;
				}
				System.out.println("Bus is leaving station: "+currentTerminal+" with "+currentPassanger+" passangers on bus");
				try{
					Thread.sleep(200);
				}catch (InterruptedException e) {
					e.printStackTrace();
				}

				
			}
		}

		 
	}
	public static void main(String[] args){
		final int N = 51;
		MyProcess[] p = new MyProcess[N];
		for (int i = 0; i < N; i++){
			if (i ==0) { //initialize shuttle
				p[i] = new MyProcess(i,1);
				p[i].start();
			}else{ // initialize passageer
				p[i] = new MyProcess(i,0);
				p[i].start();
			}
			
		}
	}
}