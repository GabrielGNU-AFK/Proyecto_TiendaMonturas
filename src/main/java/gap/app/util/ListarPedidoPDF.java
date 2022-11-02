package gap.app.util;

import java.awt.Color;
import java.util.Date;
import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import com.lowagie.text.pdf.PdfWriter;

import gap.app.model.DetalleOrden;
import gap.app.model.Usuario;


@Component("usuario/detallecompra")
public class ListarPedidoPDF extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		@SuppressWarnings("unchecked")
		List<DetalleOrden> listadoOrden=(List<DetalleOrden>) model.get("detalles");
		
		
		String nombre=(String) model.get("Nombres");
		String dni=(String) model.get("Dni");
		String direccion=(String) model.get("Direccion");
		
		Double totalM=(Double) model.get("Total");
		
		
		String nrFactura=(String) model.get("NrBoleta");
		Date fecha=(Date) model.get("Fecha");
		
		
		document.setPageSize(PageSize.LETTER.rotate());
		document.open();
		
		//Datos Factura Cliente
		PdfPTable tablaDatos = new PdfPTable(3);
		PdfPCell celdaDato=null;
		celdaDato = new PdfPCell(new Phrase("Sr(a):"+nombre));
		celdaDato.setBorder(0);
		celdaDato.setBackgroundColor(new Color(222,222,222));
		celdaDato.setHorizontalAlignment(Element.ALIGN_LEFT);
		celdaDato.setVerticalAlignment(Element.ALIGN_CENTER);
		celdaDato.setPadding(10);
		tablaDatos.addCell(celdaDato);
		
		
		PdfPCell celdaDatoC=null;
		celdaDatoC = new PdfPCell(new Phrase("DNI:"+dni));
		celdaDatoC.setBorder(0);
		celdaDatoC.setBackgroundColor(new Color(222,222,222));
		celdaDatoC.setHorizontalAlignment(Element.ALIGN_CENTER);
		celdaDatoC.setVerticalAlignment(Element.ALIGN_CENTER);
		celdaDatoC.setPadding(10);
		tablaDatos.addCell(celdaDatoC);
		
		PdfPCell celdaDatoD=null;
		celdaDatoD = new PdfPCell(new Phrase("Direccion:"+direccion));
		celdaDatoD.setBorder(0);
		celdaDatoD.setBackgroundColor(new Color(222,222,222));
		celdaDatoD.setHorizontalAlignment(Element.ALIGN_CENTER);
		celdaDatoD.setVerticalAlignment(Element.ALIGN_CENTER);
		celdaDatoD.setPadding(10);
		tablaDatos.addCell(celdaDatoD);
		
		

		
		//Total
		PdfPTable tablaTotalx = new PdfPTable(2);
		PdfPCell celdaDatoTotal=null;
		celdaDatoTotal = new PdfPCell(new Phrase("Monto Total:"));
		celdaDatoTotal.setBorder(0);
		celdaDatoTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
		celdaDatoTotal.setVerticalAlignment(Element.ALIGN_CENTER);
		
		tablaTotalx.addCell(celdaDatoTotal);
		
		
		
		
		tablaTotalx.addCell("S/."+totalM.toString());
		
		PdfPTable tablaDatosP = new PdfPTable(1);
		PdfPCell celdaDatoPers=null;
		celdaDatoPers = new PdfPCell(new Phrase("DATOS PERSONALES"));
		celdaDatoPers.setBorder(0);
		
		tablaDatosP.addCell(celdaDatoPers);
		
		PdfPTable tablaSub = new PdfPTable(4);
		
		tablaSub.addCell("Nombre");;
		tablaSub.addCell("Cantidad");;
		tablaSub.addCell("Precio U.");;
		tablaSub.addCell("Sub Total(icluye Igv)");;
		
		
		
		PdfPTable tablaTitulo = new PdfPTable(1);
		PdfPCell celda=null;
		celda = new PdfPCell(new Phrase("IMPORTACIONES JUSTYS"+"\n"+"Av. Guillermo Billunghurts 423 San Juan de Miraflores-Lima"+"\n"+"Ruc:20552103816"));
		celda.setBorder(0);
		celda.setBackgroundColor(new Color(222,222,222));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		celda.setVerticalAlignment(Element.ALIGN_CENTER);
		celda.setPadding(30);
		tablaTitulo.addCell(celda);
		tablaTitulo.setSpacingAfter(15);
		
		
		

		PdfPTable tablaNrFactura = new PdfPTable(2);
		PdfPCell celdaFact=null;
		celdaFact = new PdfPCell(new Phrase("BOLETA: "+"N°:"+nrFactura));
		celdaFact.setBorder(0);
		tablaNrFactura.addCell(celdaFact);
		
		PdfPCell celdaFecha=null;
		celdaFecha= new PdfPCell(new Phrase("Fecha: "+fecha.toString()));
		celdaFecha.setBorder(0);
		tablaNrFactura.addCell(celdaFecha);
		
		tablaNrFactura.setSpacingAfter(10);
		
	
		
		
		
		
		PdfPTable tablapedido = new PdfPTable(4);
		
		PdfPTable tablaTotal = new PdfPTable(1);
		
		double total=0 ;
	
		
		listadoOrden.forEach( ordenx ->{
			double totali = 0;
			tablapedido.addCell(ordenx.getNombre());
			tablapedido.addCell(ordenx.getCantidad().toString());
			tablapedido.addCell(ordenx.getPrecio().toString());
			tablapedido.addCell(ordenx.getTotal().toString());
			totali=ordenx.getTotal()+totali;
			totali=total;
		} );
		
		Double tott=total;
		
		 tablaTotal.addCell(tott.toString());
		 
		document.add(tablaTitulo);
		document.add(tablaNrFactura);
		document.add(tablaDatos);
		document.add(tablaSub);
		document.add(tablapedido);	
		document.add(tablaTotalx);
	}
	
	
}
