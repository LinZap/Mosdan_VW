package ui;

import Data.Data;
import java.util.Timer;
import java.util.TimerTask;

public class Mycommand extends Thread {

	/**
	 * �Ƽg�� Server �����O
	 */
	public void command() {
		// �мg���O����
	}
	
	
	/**
	 * ���R�O������n�����Ʊ�
	 */
	public void complete(){
		// �мg���O����
	}
	
	

	
	public void run() {
		boolean pow = true;
	
		
		while (pow) {
			if (!Data.islock()) {
				
				
				Data.setlock(true);
				
				final Timer publicTimer = new Timer();
				publicTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						
						this.cancel();
						publicTimer.cancel();
						
						Data.setlock(false);
						complete();
						
					
					  }
				}, 1300);
				
				command();
	
				pow = false;
				
				
			}

		}

	}
	

}
