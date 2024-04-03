package services;

import java.io.IOException;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.jws.WebService;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import oracle.jdbc.OracleTypes;

import org.w3c.dom.Document;

@WebService(endpointInterface="services.INotaCredito")
public class NotaCreditoImpl implements INotaCredito{
	
	String o_xml = "";
	Document documento = null;
    NodeList nodos = null;	
	
	@SuppressWarnings("finally")
	@Override
	public String getSaldoNC(String i_xml) throws IOException, SAXException {
		
		//System.out.println("i_xml: "+i_xml);
		int Cod_cliente =0;
		String Num_folio   =""; 
		String pref_plaza  ="";
		double SN_Saldo =0;
		int SN_cod_retorno =0;
        String SV_mens_retorno ="";
        int SN_num_evento =0;
        int HayAbonos = 0;
                
        try {
			try {
	
				SN_cod_retorno =-10000;
		        SV_mens_retorno ="Error sintaxis xml entrada";
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				InputSource is = new InputSource(new StringReader(i_xml));
				Document doc = dBuilder.parse(is);
				doc.getDocumentElement().normalize();
				
				NodeList nodeList = doc.getElementsByTagName("Root");
				for(int x=0,size= nodeList.getLength(); x<size; x++) {			    
				    Cod_cliente = Integer.valueOf(nodeList.item(x).getAttributes().getNamedItem("cod_cliente").getNodeValue());
					Num_folio = nodeList.item(x).getAttributes().getNamedItem("num_folio").getNodeValue();
					pref_plaza = nodeList.item(x).getAttributes().getNamedItem("pref_plaza").getNodeValue();
				}
				
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
			
			try {
				//Abre conexion
	
				SN_cod_retorno =-20000;
		        SV_mens_retorno ="Error conexion base datos";	
				Connection dbConnection = repository.ConexionBD.getDBConnection();
				CallableStatement cst = dbConnection.prepareCall("{call SISCEL.CO_CALCULAR_SALDO.CO_OBTIENE_SALDO(?,?,?,?,?,?,?,?)}");																	
				do {
	                // Parametro 1 del procedimiento almacenado
	                cst.setLong(1, Cod_cliente);
	                cst.setString(2, Num_folio);
	                cst.setString(3, pref_plaza);
	                
	                // Definimos los tipos de los parametros de salida del procedimiento almacenado
	                cst.registerOutParameter(4,OracleTypes.NUMERIC);
	                cst.registerOutParameter(5,OracleTypes.BIGINT);
	                cst.registerOutParameter(6,OracleTypes.VARCHAR);
	                cst.registerOutParameter(7,OracleTypes.BIGINT);
	                cst.registerOutParameter(8,OracleTypes.BIGINT);             
	                
	                // Ejecuta el procedimiento almacenado
	                cst.execute();
	                
	                // Se obtienen la salida del procedimineto almacenado
	                SN_Saldo = cst.getDouble(4);
	                SN_cod_retorno = cst.getInt(5);
	                SV_mens_retorno = cst.getString(6);
	                SN_num_evento = cst.getInt(7);
	                HayAbonos = cst.getInt(8);
	
	                Cod_cliente=0;
	            } while (Cod_cliente > 0);
				//cierra conexion
				dbConnection.close();	
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();        
			}
        } catch (Exception e1) {   
	        e1.printStackTrace();
		
        } finally {
			// TODO Auto-generated catch block			
			o_xml = "<getSaldoNCResponse><DATA idError='"+ SN_cod_retorno +"' txtError='"+ SV_mens_retorno +"' montoSaldo='"+ SN_Saldo +"' numEvento='"+ SN_num_evento+"' hayAbonos='"+ HayAbonos +"'></getSaldoNCResponse>";
			return o_xml ;
		}
		
		
		
	}
	
	@SuppressWarnings("finally")
	@Override
	public String addNC(String i_xml) throws SAXException, IOException {
		//System.out.println("i_xml: "+i_xml);		
		
		int Cod_cliente =0;
		String num_folio   =""; 
		int vp_NumProceso=0;
		String pref_plaza="";
		int ind_ordentotal  =0;				
		int cod_ciclfact=0;
		double monto=0;
		
		int SN_cod_retorno =0;
        String SV_mens_retorno ="";
                
      try {  
		try {

			SN_cod_retorno =-10000;
	        SV_mens_retorno ="Error sintaxis xml entrada";
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(i_xml));
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			
			NodeList nodeList = doc.getElementsByTagName("Root");
			for(int x=0,size= nodeList.getLength(); x<size; x++) {			    
			    Cod_cliente = Integer.valueOf(nodeList.item(x).getAttributes().getNamedItem("cod_cliente").getNodeValue());
			    num_folio = nodeList.item(x).getAttributes().getNamedItem("num_folio").getNodeValue();
			    pref_plaza = nodeList.item(x).getAttributes().getNamedItem("pref_plaza").getNodeValue();
			    ind_ordentotal = Integer.valueOf(nodeList.item(x).getAttributes().getNamedItem("ind_ordentotal").getNodeValue());
			    cod_ciclfact = Integer.valueOf(nodeList.item(x).getAttributes().getNamedItem("cod_ciclfact").getNodeValue());
			    monto = Double.valueOf(nodeList.item(x).getAttributes().getNamedItem("monto").getNodeValue());
			}
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			SV_mens_retorno=e.getMessage();
			e.printStackTrace();
		}
        
		
		try {
			//Abre conexion
			SN_cod_retorno =-20000;
	        SV_mens_retorno ="Error conexion base datos";	
			Connection dbConnection = repository.ConexionBD.getDBConnection();
			CallableStatement cst = dbConnection.prepareCall("{call SISCEL.FA_NEW_NOTACREDPARCIAL(?,?,?,?,?,?,?,?,?)}");
			do {
                // Parametro 1 del procedimiento almacenado
                cst.setLong(1, Cod_cliente);
                cst.setString(2, num_folio);
                
                cst.setString(3, pref_plaza);
                cst.setLong(4, ind_ordentotal);
                cst.setLong(5, cod_ciclfact);
                cst.setDouble(6, monto);
                
                // Definimos los tipos de los parametros de salida del procedimiento almacenado
                cst.registerOutParameter(7,OracleTypes.BIGINT);                
                cst.registerOutParameter(8,OracleTypes.BIGINT);
                cst.registerOutParameter(9,OracleTypes.VARCHAR);
                // Ejecuta el procedimiento almacenado
                cst.execute();
                
                // Se obtienen la salida del procedimineto almacenado
                vp_NumProceso = cst.getInt(7);
                SN_cod_retorno = cst.getInt(8);
                SV_mens_retorno = cst.getString(9);

                Cod_cliente=0;
            } while (Cod_cliente > 0);
			//cierra conexion
			dbConnection.close();	
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			SV_mens_retorno=e1.getMessage();
			e1.printStackTrace();			        
		}
      } catch (Exception e1) {   
    	  SV_mens_retorno=e1.getMessage();
	        e1.printStackTrace();	
		
      } finally {
			// TODO Auto-generated catch block			
    	  o_xml = "<addNCResponse><DATA idError='"+ SN_cod_retorno +"' txtError='"+ SV_mens_retorno +"' vp_NumProceso='"+ vp_NumProceso +"'></addNCResponse>";
  			return o_xml ;
		}
		
	}
	
	@SuppressWarnings("finally")
	@Override
	public String getEstadoNC(String i_xml) throws SAXException, IOException {
		//System.out.println("i_xml: "+i_xml);
		int Cod_cliente =0;
		String Num_proceso   =""; 
		
		int vp_CodEstado  =0;
		String vp_Estado="";
		int SN_cod_retorno =0;
        String SV_mens_retorno ="";
                
        try {
		try {

			SN_cod_retorno =-10000;
	        SV_mens_retorno ="Error sintaxis xml entrada";
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(i_xml));
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			
			NodeList nodeList = doc.getElementsByTagName("Root");
			for(int x=0,size= nodeList.getLength(); x<size; x++) {			    
			    Cod_cliente = Integer.valueOf(nodeList.item(x).getAttributes().getNamedItem("cod_cliente").getNodeValue());
			    Num_proceso = nodeList.item(x).getAttributes().getNamedItem("num_proceso").getNodeValue();
			}
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
		try {
			//Abre conexion
			SN_cod_retorno =-20000;
	        SV_mens_retorno ="Error conexion a base daos";	   
			Connection dbConnection = repository.ConexionBD.getDBConnection();
			CallableStatement cst = dbConnection.prepareCall("{call SISCEL.FA_CONSULTA_NEW_NOTACREDITO(?,?,?,?,?,?)}");																	
			do {
                // Parametro 1 del procedimiento almacenado
                cst.setLong(1, Cod_cliente);
                cst.setString(2, Num_proceso);
                
                // Definimos los tipos de los parametros de salida del procedimiento almacenado
                cst.registerOutParameter(3,OracleTypes.BIGINT);                
                cst.registerOutParameter(4,OracleTypes.VARCHAR);
                cst.registerOutParameter(5,OracleTypes.BIGINT);
                cst.registerOutParameter(6,OracleTypes.VARCHAR);
                // Ejecuta el procedimiento almacenado
                cst.execute();
                
                // Se obtienen la salida del procedimineto almacenado
                vp_CodEstado = cst.getInt(3);
                vp_Estado = cst.getString(4);
                SN_cod_retorno = cst.getInt(5);
                SV_mens_retorno = cst.getString(6);

                Cod_cliente=0;
            } while (Cod_cliente > 0);
			//cierra conexion
			dbConnection.close();	
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();			        
		}
      } catch (Exception e1) {   
	        e1.printStackTrace();	
		
      } finally {
		  o_xml = "<getEstadoNCResponse><DATA idEstado='"+ vp_CodEstado +"' txtEstado='"+ vp_Estado +"' idError='"+ SN_cod_retorno +"' txtError='"+ SV_mens_retorno +"'></getEstadoNCResponse>";
			
			return o_xml ;
		}
				
		
	}
	
	
	

}
