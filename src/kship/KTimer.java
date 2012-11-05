package kship;

import java.util.HashMap;
import java.util.Map.Entry;

public class KTimer {

	static private HashMap<String, Long> timeStart = new HashMap<String, Long>();
	static private HashMap<String, Long> times = new HashMap<String, Long>();
	
	static public void start(String timer) {
		timeStart.put(timer, System.currentTimeMillis());		
	}
	
	static public void stop(String timer) {
		Long time0 = timeStart.get(timer);
		if (time0 == null) {
			return;
		}
		long ms = System.currentTimeMillis() - time0;
		
		Long all = times.get(timer);
		if (all == null) {
			all = 0L;
		}
		all += ms;		
		times.put(timer, all);
	}
	
	static public void print() {
		for (Entry<String, Long> e : times.entrySet()) {
			System.out.println(e.getKey() + ": " + e.getValue() + "ms");
		}
	}
}
