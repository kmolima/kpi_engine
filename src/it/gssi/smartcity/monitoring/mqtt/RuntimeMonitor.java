package it.gssi.smartcity.monitoring.mqtt;

public class RuntimeMonitor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	System.out.println(	java.util.Calendar.getInstance().getTime());
		BreezoPublisher p=new BreezoPublisher();  
		Thread t1 =new Thread(p);  
		t1.start();  
		
		AirQualitySensorPublisher p2= new AirQualitySensorPublisher();
		Thread t2 =new Thread(p2);  
		t2.start();  
		
		OpenWeatherPublisher p3 = new OpenWeatherPublisher();
		Thread t3 = new Thread(p3);
		t3.start();

	}


}
