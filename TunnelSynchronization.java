
import java.util.concurrent.Semaphore;
import java.util.Arrays;
import java.lang.*;

class MyProcess extends Thread {
	private int id;
	
	public static int leftcounter =0; //prevent starvation for a unpopular direction.
	public static int rightcounter =0;
	public static int left=0;
	public static int right =0;
	static Semaphore turn = new Semaphore(0);
	static Semaphore currentTunnel = new Semaphore(4);
	static Semaphore mutex1 = new Semaphore(1);
	static Semaphore mutex2 = new Semaphore(1);
	public MyProcess(int i){
		  id = i;
	
	}	
	public void run() {
		for (int k =0; k< 3;k++){
			int direction =0; // use an int to present direction, 0 is left 1 is right.
			double D = Math.random();//The thread will choose their direction randomly. In this way we can control the properbility of each direction.
			if (D <=0.1) {
				direction =1;
			}
			
			if (direction ==1 ) {
				System.out.println("Thread "+this.id+" is waiting to travel in direction left!");
				if (left ==0 || leftcounter >10) {
					try{
						turn.acquire();
					}
					catch(InterruptedException e){
					}
				}
				
				try{
					mutex1.acquire();
				}
				catch(InterruptedException e){
				}
				leftcounter ++;
				left ++;
				try{
					currentTunnel.acquire();
				}
				catch(InterruptedException e){
				}
				mutex1.release();
					
				System.out.println("Thread "+this.id+" is traveling in direction of left in the tunnel now!");
				try{
					Thread.sleep(200);
				}catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				
				System.out.println("Thread "+this.id+" is out of the tunnel now");
				try{
					mutex1.acquire();
				}
				catch(InterruptedException e){
				}
				left --;
				currentTunnel.release();
				if (left==0) {
					if (leftcounter >10) {
						leftcounter=0;
					}
					turn.release();
				}
				mutex1.release();
			} else {
				System.out.println("Thread "+this.id+" is waiting to travel in direction right!");
				if (right ==0 || rightcounter >10) {
					try{
						turn.acquire();
					}
					catch(InterruptedException e){
					}
				}
				
				try{
					mutex2.acquire();
				}
				catch(InterruptedException e){
				}
				rightcounter++;;
				right++;
				try{
					currentTunnel.acquire();
				}
				catch(InterruptedException e){
				}
				mutex2.release();
				System.out.println("Thread "+this.id+" is traveling in direction of right in the tunnel now!");
				try{
					Thread.sleep(200);
				}catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Thread "+this.id+" is out of the tunnel now");
				try{
					mutex2.acquire();
				}
				catch(InterruptedException e){
				}
				right --;
				currentTunnel.release();
				if (right==0) {
					if (rightcounter >10) {
						rightcounter =0;
					}
					turn.release();
				}
				mutex2.release();

			}
			
		}
		 
	}
	public static void main(String[] args){
		final int N = 10;
		MyProcess[] p = new MyProcess[N];
		for (int i = 0; i < N; i++){
			p[i] = new MyProcess(i);
			p[i].start();
		}
		turn.release();
	}
}