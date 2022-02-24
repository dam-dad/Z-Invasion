package JasperReport;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class Main {

    public static final String JRXML_FILE ="/reports/integrantes.jrxml";
    public static final String PDF_FILE = "pdf/integrantes.pdf";

    public static void vistaPrevia() throws JRException{
        // COMPILAR EL INFORME
        JasperReport report = JasperCompileManager.compileReport(Main.class.getResourceAsStream(JRXML_FILE));
        // MAPA DE PARÃ?METROS PARA EL INFORME
        Map<String, Object> parameters = new HashMap<String, Object>();
        // GENERAMOS EL INFORME(COMBINAMOS EL INFORME COMPILADO CON LOS DATOS)
        JasperPrint print  = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(IntegrantesDataProvider.getIntegrantes()));
        // VISUALIZA EL INFORME GENERADO
        JasperViewer.viewReport(print);
    }

    public static void generarPdf() throws JRException, IOException{
        // COMPILAR EL INFORME
        JasperReport report = JasperCompileManager.compileReport(Main.class.getResourceAsStream(JRXML_FILE));
        // MAPA DE PARÃ?METROS PARA EL INFORME
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("anyo", 2014); // no lo uso, pero se lo paso
        // GENERAMOS EL INFORME(COMBINAMOS EL INFORME COMPILADO CON LOS DATOS)
        JasperPrint print  = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(IntegrantesDataProvider.getIntegrantes()));
        // EXPORTA EL INFORME A UN FICHERO PDF
        JasperExportManager.exportReportToPdfFile(print, PDF_FILE);
        // ABRE EL PDF GENERADO CON EL PROGRAMA PREDETERMINADO DEL S.O.
        Desktop.getDesktop().open(new File(PDF_FILE));
    }

    public static void main(String[] args) throws JRException {
        vistaPrevia();
    }
}
