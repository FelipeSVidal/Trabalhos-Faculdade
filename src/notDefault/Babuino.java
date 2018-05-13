package notDefault;

import java.nio.Buffer;
import java.util.concurrent.Semaphore;

import Default.main;


/*
 * Problema na Flag! Ela tá ficando true mesmo sem ter ngm na função ir()
 * 
 * 
 */

public class Babuino extends Thread {
	public int dir;
	Object lock;
	static Semaphore buffer = new Semaphore(3);
	static Semaphore mutex = new Semaphore(1);
	private int corda;
	public static int passou = 0;
	public static int bnCorda = 0;
	public static boolean flag = false;
	long inicio;
	long fim;
	
	public Babuino(Object lock){
		inicio = System.currentTimeMillis();
		this.lock = lock;
	}
	public void run(){
		//System.out.println(this.getName()+ " Entrou às: "+inicio+"\ndir: "+this.dir+"\t Corda:"+main.corda);
		while(!this.isInterrupted()){
				////System.out.println("CORDA: "+main.corda);
				try {
					verificar();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				////System.out.println("VEIO : "+ this.getName() +"\t"+ this.dir);
				try {
					ir();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		fim = System.currentTimeMillis();
	}


	public synchronized void verificar() throws InterruptedException{
		int tam_1 = main.Babuinos_l1.size();
		int tam_2 = main.Babuinos_l2.size();
		System.out.println("Já passaram: "+Babuino.passou);
		if(passou>=3){
			System.out.println("[1]");
			if(main.corda == 1){
				System.out.println("[1.1] naCorda = "+bnCorda);
				if(tam_2 > 0){
					if(!naCorda()) {
						main.corda = -1;
						passou = 0;
						//flag = true;
					}
					//naCorda();
				}else {
					buffer.release();
				}
			}else{
				if(tam_1 > 0){
					System.out.println("[1.2] naCorda = "+bnCorda);
					if(!naCorda()) {
						main.corda = 1;
						passou = 0;
						//flag = true;
					}
					//naCorda();
				}else {
					buffer.release();
				}
			}
		}else if(passou < 3 && tam_1 > 0 && tam_2 > 0) {
			flag = false;
			if(buffer.availablePermits() == 0) {
				buffer.release();
			}
		}
		else{// Corrigir essa parte, para quando Passou for < 3, Verificar se existe >0 no oposto

			System.out.println("[3]");
			if(main.corda == 1) {
				if(tam_1 == 0 && tam_2 > 0){
					System.out.println("[3.1] naCorda = "+bnCorda);
					if(!naCorda()) {
						main.corda = -1;
						passou = 0;
						//flag = true;
					}
				}else {
					buffer.release();
				}
			}else{
				if(tam_2 == 0 && tam_1 > 0){
					System.out.println("[3.2] naCorda = "+bnCorda);
					if(!naCorda()) {
						main.corda = 1;
						passou = 0;
						//flag = true;
					}
				}else {
					buffer.release();
				}
			}
			
		}

	}
	public void ir() throws InterruptedException{ // Vai até a corda
		if(!flag) {	

			synchronized(this) {
				this.corda = main.corda;
			}
			if(this.corda == this.dir && buffer.tryAcquire()){
				irCorda();
					bnCorda++;
					this.sleep(4000);
					--bnCorda;
					passou++;
				synchronized(this) {	
					System.out.print("Tamanho do vetor Babuino antes é: "+main.Babuinos_C.size());
						main.Babuinos_C.add(this);
						main.Babuinos_l1.remove(this);
						main.Babuinos_l2.remove(this);
					
					System.out.println("\tdepois é: "+main.Babuinos_C.size());
						main.morreram++;
				}
				
					//naCorda();
					this.interrupt();
			}
			
		}
		if(!this.isInterrupted()) {
			dormir();
		}
		
	}

	public synchronized boolean naCorda() throws InterruptedException{ // Informa que tem na corda
		if(bnCorda == 0){
			flag = false;
			buffer.release(3-buffer.availablePermits());
			acordar();
			return false;
		}
		flag = true;
		return true;
	}
	
	public synchronized void irCorda() throws InterruptedException {
		this.sleep(1000); // indo para corda
	}
	
	public void dormir() throws InterruptedException {
		synchronized (main.lock) {
			main.lock.wait();
		}	
	}
	
	public void acordar() {
		synchronized (main.lock) {
			main.lock.notifyAll();	
		}	
	}
	
	
	public String acabou(){ // Informa o tempo q passou
		return ""+((this.fim - this.inicio)/1000);
	}
	
}
