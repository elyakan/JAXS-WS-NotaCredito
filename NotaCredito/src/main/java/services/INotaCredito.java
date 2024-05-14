package services;

import java.io.IOException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.xml.sax.SAXException;

@WebService
public interface INotaCredito {
	@WebMethod
	public String getSaldoNC(@WebParam(name = "idCliente") String idCliente, 
							@WebParam(name = "idFolio") String idFolio, 
							@WebParam(name = "idPrefijo") String idPrefijo) throws IOException, SAXException;
	
	@WebMethod
	public String addNC(@WebParam(name = "idCliente") String idCliente, 
						@WebParam(name = "idFolio") String idFolio, 
						@WebParam(name = "idPrefijo") String idPrefijo, 
						@WebParam(name = "tipoDocumento") String tipoDocumento, 
						@WebParam(name = "codCiclFact") String codCiclFact, 
						@WebParam(name = "saldo") String saldo)  throws IOException, SAXException;
	
	@WebMethod
	public String getEstadoNC(@WebParam(name = "idCliente") String idCliente, 
							  @WebParam(name = "numProceso") String numProceso)  throws IOException, SAXException;
}