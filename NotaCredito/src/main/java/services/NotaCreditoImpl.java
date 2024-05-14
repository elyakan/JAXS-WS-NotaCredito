package services;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.sql.DataSource;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import oracle.jdbc.OracleTypes;
import org.w3c.dom.Document;

@WebService(endpointInterface="services.INotaCredito")
public class NotaCreditoImpl implements INotaCredito{
	
	String o_xml = "";
	Document documento = null;
    NodeList nodos = null;	
    
    @Resource(name="jdbc/JNDICostaRica")
    private DataSource ds;
    
    
	@SuppressWarnings("finally")
	@Override
	public String getSaldoNC(String idCliente, String idFolio, String idPrefijo) throws IOException, SAXException {
		
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
	
				SN_cod_retorno =-10000;
		        SV_mens_retorno ="Error sintaxis xml entrada";
		        
			    Cod_cliente = Integer.valueOf(idCliente);
				Num_folio = idFolio;
				pref_plaza = idPrefijo;
				
			
			try {
				//Abre conexion
	
				SN_cod_retorno =-20000;
		        SV_mens_retorno ="Error conexion base datos";	
		        Connection dbConnection = ds.getConnection();
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
				SN_cod_retorno =e1.getErrorCode();
				SV_mens_retorno = e1.getMessage();
		        System.out.println("SQLException: " + e1.getMessage());
			}
	      } catch (Exception e1) {  
	    	  SV_mens_retorno = e1.getMessage();
		        System.out.println("Exception: " + e1.getMessage());
		
        } finally {
			// TODO Auto-generated catch block			
			o_xml = "<getSaldoNCResponse><DATA idError='"+ SN_cod_retorno +"' txtError='"+ SV_mens_retorno +"' montoSaldo='"+ SN_Saldo +"' numEvento='"+ SN_num_evento+"' hayAbonos='"+ HayAbonos +"'></getSaldoNCResponse>";
			return o_xml ;
		}
		
		
		
	}
	
	@SuppressWarnings("finally")
	@Override
	public String addNC(String idCliente, String idFolio, String idPrefijo, String tipoDocumento, String codCiclFact, String saldo) throws SAXException, IOException {
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
		

			SN_cod_retorno =-10000;
	        SV_mens_retorno ="Error sintaxis xml entrada";
					    
		    Cod_cliente = Integer.valueOf(idCliente);
		    num_folio = idFolio;
		    pref_plaza = idPrefijo;
		    ind_ordentotal = Integer.valueOf(tipoDocumento);
		    cod_ciclfact = Integer.valueOf(codCiclFact);
		    monto = Double.valueOf(saldo);
		
		try {
			//Abre conexion
			SN_cod_retorno =-20000;
	        SV_mens_retorno ="Error conexion base datos";	
	        Connection dbConnection = ds.getConnection();
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
			SN_cod_retorno =e1.getErrorCode();
			SV_mens_retorno = e1.getMessage();
	        System.out.println("SQLException: " + e1.getMessage());
		}
      } catch (Exception e1) {  
    	  SV_mens_retorno = e1.getMessage();
	        System.out.println("Exception: " + e1.getMessage());
		
      } finally {
			// TODO Auto-generated catch block			
    	    o_xml = "<addNCResponse><DATA idError='"+ SN_cod_retorno +"' txtError='"+ SV_mens_retorno +"' vp_NumProceso='"+ vp_NumProceso +"'></addNCResponse>";
  			return o_xml ;
		}
		
	}
	
	@SuppressWarnings("finally")
	@Override
	public String getEstadoNC(String idCliente, String numProceso) throws SAXException, IOException {
		//
		int Cod_cliente =0;
		String Num_proceso   =""; 
		int vp_CodEstado  =0;
		String vp_Estado="";
		int SN_cod_retorno =0;
        String SV_mens_retorno ="";
                
        try {

			SN_cod_retorno =-10000;
	        SV_mens_retorno ="Error sintaxis xml entrada";
	        
		    Cod_cliente = Integer.valueOf(idCliente);
		    Num_proceso = numProceso;
		try {
			//Abre conexion
			SN_cod_retorno =-20000;
			SV_mens_retorno ="Error conexion a base datos";
			
			Connection dbConnection = ds.getConnection();			
				   
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
			SV_mens_retorno = e1.getMessage();
    	    SN_cod_retorno =e1.getErrorCode();    	    
	        System.out.println("SQLException: " + e1.getMessage());
		}
      } catch (Exception e1) {
    	    SV_mens_retorno = e1.getMessage();    	    
	        System.out.println("Exception: " + e1.getMessage());		
      } finally {    	
	      System.out.println("finally: SV_mens_retorno= " + SV_mens_retorno);
		  o_xml = "<getEstadoNCResponse><DATA idEstado='"+ vp_CodEstado +"' txtEstado='"+ vp_Estado +"' idError='"+ SN_cod_retorno +"' txtError='"+ SV_mens_retorno +"'></getEstadoNCResponse>";
		  return o_xml ;
	     }
	}
}
