package net.latin.server.security.captcha;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sound.sampled.AudioFormat;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import lowlevel.LameEncoder;
import net.latin.server.persistence.SpringUtils;
import net.latin.server.utils.helpers.FileUtils;
import nl.captcha.audio.AudioCaptcha;
import nl.captcha.audio.Mixer;
import nl.captcha.audio.Sample;
import nl.captcha.audio.noise.NoiseProducer;
import nl.captcha.audio.producer.VoiceProducer;
import nl.captcha.text.producer.TextProducer;


public class VerificationCodeServlet extends HttpServlet
{

	public static final String VERIFICATION_CODE_KEY = "verification.code";
	private static final long serialVersionUID = 1L;

	private Log LOG=LogFactory.getLog(VerificationCodeServlet.class);
	
    public void init(ServletConfig config) throws ServletException
    {
      super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	response.setHeader("Pragma", "public");
    	response.setHeader("cache-control", "max-age=0,no-cache, no-store,must-revalidate, proxy-revalidate, s-maxage=0");
    	response.setDateHeader("Expires", 0);
    	response.setContentType("image/png");

        VerificationCodeGenerator verification = new VerificationCodeGenerator(response.getOutputStream());
        String code = verification.getVerificationCode();

        HttpSession session = request.getSession(true);
        session.setAttribute(VERIFICATION_CODE_KEY, code);
        
        
        if (SpringUtils.getProperty("captcha_audio")!=null && SpringUtils.getProperty("captcha_audio").toUpperCase().equals("SI")){
        	crearCaptchaAudio(code, session);
        }
        
		
        response.flushBuffer();
    }

    private void crearCaptchaAudio(final String code, HttpSession session) {
    	try {
	    	VoiceProducer vProd = VoiceProducerImpl.getInstance();
	        NoiseProducer nProd = NoiseProducerImpl.getInstance();
	        AudioCaptcha build = new AudioCaptcha.Builder()
			        .addAnswer(new TextProducer() {
						public String getText() {
							return code;
						}
					})
					.addVoice(vProd)
					
					//.addNoise(nProd)
	        .build();
	        
	        Sample challenge = build.getChallenge();
	        
			AudioFormat aFWav=challenge.getAudioInputStream().getFormat();
			AudioFormat aFMp3=new AudioFormat(16000, 16, 2, true, false); 
			
			LameEncoder audioEncoder=new LameEncoder(aFWav, aFMp3);
			
			InputStream is=challenge.getAudioInputStream();
			
			int bufferSize = audioEncoder.getInputBufferSize();
		    byte buffer[] = new byte[bufferSize];
		    byte[] encoded = new byte[audioEncoder.getOutputBufferSize()];
		    
		    OutputStream outputStream = new ByteArrayOutputStream();
	        int count = is.read(buffer, 0, buffer.length);
	
	        while (count > 0) {
	          int encodedCount = audioEncoder.encodeBuffer(buffer, 0,  count, encoded);
	          outputStream.write(encoded, 0, encodedCount);
	          count = is.read(buffer, 0, buffer.length);
	        }
		        
		    int encodedCount = audioEncoder.encodeFinish(encoded);
		    outputStream.write(encoded, 0, encodedCount);
		    audioEncoder.close();
		    session.setAttribute(VERIFICATION_CODE_KEY + ".sound", outputStream);
			
		} catch (Exception e) {
			LOG.error("Se produjo un error al generar el sonido para el captcha",e);
		}
	}

	private final static class VoiceProducerImpl implements VoiceProducer {
    	private Map<Character,ByteArrayOutputStream> mapa;
		private static VoiceProducerImpl me;
    	
    	private VoiceProducerImpl() {
    		mapa=new HashMap<Character, ByteArrayOutputStream>();
    		URL resource = VerificationCodeServlet.class.getClassLoader().getResource("sounds");
    		//ESCAPE FEO HECHO PARA QUE NO CONSIDERE LA CARPETA DENTRO DEL JAR
    		if (resource.getPath().contains(".jar")){
				return;
			}
			Collection<File> files = FileUtils.getFiles(resource.getPath(), ".+\\.wav");
			try {
				for (File file:files){
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					IOUtils.copy(new FileInputStream(file), baos);
					mapa.put(file.getName().toUpperCase().charAt(0), baos );
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	
    	public static VoiceProducerImpl getInstance(){
    		if (me==null){
    			me=new VoiceProducerImpl();
    		}
    		return me;
    	}
    	
    	public Sample getVocalization(char car) {
    		String carString=car+"";
    		carString=carString.toUpperCase();
    		Sample a;
    		return new Sample(new ByteArrayInputStream(mapa.get(carString.charAt(0)).toByteArray()));
    	}
    }
    
	private final static class NoiseProducerImpl implements NoiseProducer {
		private List<ByteArrayOutputStream> lista;
		private static NoiseProducerImpl me;
		
		private NoiseProducerImpl() {
			lista=new ArrayList<ByteArrayOutputStream>();
			URL resource = VerificationCodeServlet.class.getClassLoader().getResource("sounds/noises");
			//ESCAPE FEO HECHO PARA QUE NO CONSIDERE LA CARPETA DENTRO DEL JAR
			if (resource.getPath().contains(".jar")){
				return;
			}
			Collection<File> files = FileUtils.getFiles(resource.getPath(), ".+\\.wav");
			try {
				for (File file:files){
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					IOUtils.copy(new FileInputStream(file), baos);
					lista.add(baos);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public static NoiseProducerImpl getInstance(){
    		if (me==null){
    			me=new NoiseProducerImpl();
    		}
    		return me;
    	}
		
		public Sample addNoise(List<Sample> samples) {
			Random rand=new Random();
			int indice = rand.nextInt(lista.size());
			Sample appended = Mixer.append(samples);
			Sample noise = new Sample(new ByteArrayInputStream(lista.get(indice).toByteArray()));
			
			// Decrease the volume of the noise to make sure the voices can be heard
			return Mixer.mix(appended, 1.0, noise, 0.1);
		}
	}

}
