package services;

import java.io.IOException;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.xml.sax.SAXException;

@WebService
public interface INotaCredito {
	@WebMethod
	public String getSaldoNC(String i_xml) throws IOException, SAXException;
	
	@WebMethod
	public String addNC(String i_xml)  throws IOException, SAXException;
	
	@WebMethod
	public String getEstadoNC(String i_xml)  throws IOException, SAXException;
}