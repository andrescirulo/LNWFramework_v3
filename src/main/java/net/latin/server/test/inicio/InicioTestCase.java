package net.latin.server.test.inicio;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;

import net.latin.client.test.inicio.pages.domain.Persona;
import net.latin.client.test.inicio.rpc.InicioTestClient;
import net.latin.client.widget.base.RespuestRPC;
import net.latin.client.widget.base.SimpleRespuestRPC;
import net.latin.server.GwtUseCase;
import net.latin.server.fileUpload.FileContainer;
import net.latin.server.fileUpload.FileDownloaderServlet;
import net.latin.server.fileUpload.FileToShowOnClient;
import net.latin.server.utils.fileTypes.Pdf;
import net.latin.server.utils.reports.visualizer.ReportViewerServlet;

public class InicioTestCase extends GwtUseCase implements InicioTestClient {

	@Override
	protected String getServiceName() {
		return "InicioTestCase";
	}

	@Override
	public SimpleRespuestRPC getTextoInicial() {
		SimpleRespuestRPC res = new SimpleRespuestRPC();
		res.setMensaje("Este es el mensaje de bienvenida");
		
		InputStream pdfIS;
		try {
			pdfIS = new FileInputStream(new File(InicioTestCase.class.getClassLoader().getResource("/test.pdf").toURI()));
			Pdf pdfFile=new Pdf("test.pdf", IOUtils.toByteArray(pdfIS));
			ReportViewerServlet.setFileToShow(pdfFile);
		} catch (Exception e) {
			res.setError("Error al cargar el archivo PDF");
			LOG.error(res.getMensaje(),e);
		}
		
		return res;
	}
	
	public RespuestRPC<Persona> getListaPersonas() {
		RespuestRPC<Persona> res = new RespuestRPC<Persona>();
		try {
			InputStream nombresIS=new FileInputStream(new File(InicioTestCase.class.getClassLoader().getResource("/nombres.txt").toURI()));
			InputStream apellidosIS=new FileInputStream(new File(InicioTestCase.class.getClassLoader().getResource("/apellidos.txt").toURI()));
			InputStream profesionesIS=new FileInputStream(new File(InicioTestCase.class.getClassLoader().getResource("/profesiones.txt").toURI()));
		
			List<String> nombres = IOUtils.readLines(nombresIS,Charset.forName("utf8"));
			List<String> apellidos = IOUtils.readLines(apellidosIS,Charset.forName("utf8"));
			List<String> profesionesSrc = IOUtils.readLines(profesionesIS,Charset.forName("utf8"));
			List<String> profesiones=new ArrayList<String>();
			Random rnd=new Random();
			for (int i = 0;i<10;i++){
				int idx = rnd.nextInt(profesionesSrc.size());
				profesiones.add(profesionesSrc.get(idx));
			}
			
			List<Persona> personas=new ArrayList<Persona>();
			for (int i = 0;i<100;i++){
				Persona pers=new Persona(); 
				int idx = rnd.nextInt(nombres.size());
				pers.setNombre(nombres.get(idx));
				idx = rnd.nextInt(apellidos.size());
				pers.setApellido(apellidos.get(idx));
				idx = rnd.nextInt(profesiones.size());
				pers.setProfesion(profesiones.get(idx));
				pers.setFechaNacimiento(new Date(50 +  rnd.nextInt(50),rnd.nextInt(12),rnd.nextInt(28)));
				personas.add(pers);
			}
			res.setBusinessObjectsList(personas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public SimpleRespuestRPC prepareForDownload(String fileId) {
		SimpleRespuestRPC rta = new SimpleRespuestRPC();
		try {
			FileToShowOnClient file = FileContainer.getFileFromCurrentSession(fileId);
			if (file == null) {
				rta.setError("No se pudo descargar el archivo");
			} else {
				FileDownloaderServlet.setFileToShow(file);
				rta.setRespuesta(true);
			}
		} catch (Exception e) {
			rta.setError("No se pudo descargar el archivo");
			LOG.error(rta.getMensaje(),e);
		}
		
		return rta;
	}

	public SimpleRespuestRPC prepareForView(String fileId) {
		SimpleRespuestRPC rta = new SimpleRespuestRPC();
		try {
			
			FileToShowOnClient file = FileContainer.getFileFromCurrentSession(fileId);
			if (file == null) {
				rta.setError("No se pudo mostrar el archivo");
			} else {
				ReportViewerServlet.setFileToShow(file);
				rta.setRespuesta(true);
			}
		} catch (Exception e) {
			rta.setError("No se pudo mostrar el archivo");
			LOG.error(rta.getMensaje(),e);
		}
		
		return rta;
	}

}
