import javax.xml.ws.Endpoint;

import services.NotaCreditoImpl;



public class NotaCreditoPublish {

	public static void main(String[] args)  throws Exception{
		// TODO Auto-generated method stub
		
		Endpoint.publish("http://127.0.0.1:8093/LCRLVL-SOAP-WEBSERVICE-01-NOTACREDITO", new NotaCreditoImpl() );	
	}

}
