package Default;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import notDefault.Babuino;

public class main {
	public static int corda;
	public static long inicio = System.currentTimeMillis();
	public static ArrayList Babuinos_l1 = new ArrayList();
	public static ArrayList Babuinos_l2 = new ArrayList();
	public static ArrayList Babuinos_C = new ArrayList();
	public static int morreram = 0;
	private static int qtd = 50;

	public static Object lock = new Object();
	/**
	 * @param args
	 * Autor: Felipe Vidal  - Matricula: 1250187
	 * Trabalho de PPC -> Babuinos na Corda Bamba
	 */
	public static void main(String[] args) {
		int nBabuinos = 0; // Contador de Babuinos
		int dir_1 = 0; // Contador de quantos ficam para o lado 1
		int dir_2 = 0; // Contador de quantos ficam para o lado 2
		while(true){
			if(nBabuinos < qtd){
				Babuino babu = new Babuino(lock);
				// Bloco para não ficar diferenciado a direção dos macacos
				if(dir_1 == 25){
					babu.dir = -1;
					Babuinos_l2.add(babu);
					dir_2++;
				}else if(dir_2 == 25){
					babu.dir=1;
					Babuinos_l1.add(babu);
					dir_1++;
				}
				else{
					if(Math.random()>0.5){
						babu.dir = -1;
						Babuinos_l2.add(babu);
						dir_2++;
					}else{
						babu.dir = 1;
						Babuinos_l1.add(babu);
						dir_1++;
					}
				}
					nBabuinos++;
					babu.setName("Babuino "+nBabuinos);
					if(nBabuinos == 1){
						main.corda = babu.dir;
					}
					synchronized (lock) {
						lock.notifyAll();
					}
					babu.start();
					try {
						int tempo=new Random().nextInt(8)+1;
		//				System.out.println(tempo +"\tSIZE:\t"+ Babuinos_l2.size());
						TimeUnit.SECONDS.sleep(tempo);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}else{
				
				while(main.morreram < qtd){
					System.out.println("Complet: "+Babuinos_C.size());
					System.out.println("L1: "+Babuinos_l1.size()+"\tL2: "+Babuinos_l2.size()+"\t"+corda);
					try {
						TimeUnit.SECONDS.sleep(5);
						synchronized (lock) {
							lock.notifyAll();
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						System.out.println("L1: "+Babuinos_l1.size()+"\tL2: "+Babuinos_l2.size()+"\t"+corda);
				}
				break;
			}
			System.out.println(nBabuinos);
		}
		for(int i = 0;i<Babuinos_C.size();i++){
			Babuino b = (Babuino) Babuinos_C.get(i);
			System.out.println("TEMPO DE: "+b.getName()+" Igual a: "+b.acabou());
		}
		
	}

}
