package ui;

import Data.Data;
import java.util.Timer;
import java.util.TimerTask;

public class Mycommand extends Thread {

	/**
	 * 複寫對 Server 的指令
	 */
	public void command() {
		// 覆寫指令執行
	}
	
	
	/**
	 * 此命令結束後要做的事情
	 */
	public void complete(){
		// 覆寫指令執行
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
