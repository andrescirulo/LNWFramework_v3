package net.latin.client.widget.captcha;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

import gwt.material.design.client.constants.IconType;
import net.latin.client.utils.audio.AudioManager;
import net.latin.client.widget.base.GwtVisualComponent;
import net.latin.client.widget.button.GwtButton;
import net.latin.client.widget.button.GwtIconButton;
import net.latin.client.widget.label.GwtLabel;
import net.latin.client.widget.textBox.GwtTextBox;

/**
 * Captcha Widget, pide a un servlet una imagen con un texto ofuscado, para
 * despues comprobarlo en el server (acordarse que hay que llamar a render para
 * que se muestre) (y llamar a reloadImage() para cargar la imagen)
 * 
 * @author Fernando Diaz
 * 
 */
public class GwtVerificationCode extends GwtVisualComponent {

	private static final String LABEL_DESCRIPCION_CSS = "GwtVerificationCodeLabelDescripcion";
	private static final String CAMBIAR_IMAGEN = "Cambiar Imagen"; // TODO poner
																	// en i18n
	private static final String DESCRIPCIION = "Escriba los caracteres que usted ve en la imagen"; // TODO
																									// poner
																									// en
																									// i18n
	
	private String SOUND_ID="";
	private HorizontalPanel hPanel;
	private Image verifyImage;
	private GwtTextBox verificationCode;
	private GwtButton reloadImage;
	private GwtLabel label;
	
	private GwtIconButton playSound;
	private GwtIconButton reloadImageIcon;
	private AudioManager audioManager;
	
	public GwtVerificationCode() {
		this(GwtVerificationCodeTypeEnum.TIPO_NORMAL);
	}
	
	public GwtVerificationCode(GwtVerificationCodeTypeEnum tipo) {
		audioManager = new AudioManager();
		verificationCode = new GwtTextBox();
		this.add(verificationCode);
		hPanel = new HorizontalPanel();

		verifyImage = new Image();
		hPanel.add(verifyImage);
		hPanel.setCellWidth(verifyImage, "110px");
		verifyImage.getElement().getParentElement().getStyle().setProperty("border", "1px solid #42a5f5");
		this.add(hPanel);
		label = new GwtLabel();
		label.setText(DESCRIPCIION);
		label.setStyleName(LABEL_DESCRIPCION_CSS);
		
		if (tipo==GwtVerificationCodeTypeEnum.TIPO_NORMAL){
			reloadImage = new GwtButton(CAMBIAR_IMAGEN,new ClickHandler() {
				public void onClick(ClickEvent event) {
					reloadImage();
				}
			});
			reloadImage.setWidth("100px");
			this.add(reloadImage);
		}
		else if (tipo==GwtVerificationCodeTypeEnum.TIPO_CON_SONIDO){
			VerticalPanel vPanel = new VerticalPanel();
			hPanel.add(vPanel);
			
			playSound=new GwtIconButton(IconType.VOLUME_UP);
			playSound.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (!audioManager.isPlaying()){
						playSound();
					}
				}
			});
			playSound.setTitle("Reproducir Sonido");
			vPanel.add(playSound);
			
			reloadImageIcon=new GwtIconButton(IconType.REFRESH);
			reloadImageIcon.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (!audioManager.isPlaying()){
						reloadImage();
					}
				}
			});
			reloadImageIcon.setTitle("Recargar Imagen");
			vPanel.add(reloadImageIcon);
			
			verifyImage.addStyleName("GwtVerificationCodeImageStyle");
			
			
			hPanel.addStyleName("GwtVerificationCodeMainBorderStyle");
			vPanel.addStyleName("GwtVerificationCodeButtonsBorderStyle");
		}
		else{
			throw new RuntimeException("El tipo del captcha no es correcto");
		}
		
		
		// render(); hacer render cuando sea necesario
		
	}
	
	protected void playSound() {
		audioManager.play("sound_captcha.mp3?rand=" + Random.nextInt());
	}

	public void reloadImage() {
		verificationCode.setText("");
		verifyImage.setUrl("verify.png?rand=" + Random.nextInt());
	}

	public String getVerificationCode() {
		return verificationCode.getText();
	}

	public void setFocus() {
		verificationCode.setFocus();
	}

	public void addKeyUpHandler(KeyUpHandler handler){
		verificationCode.addKeyUpHandler(handler);
	}

	public void setTabIndex(int index) {
		verificationCode.setTabIndex(index);
	}

	public void setMaxLength(int length) {
		verificationCode.setMaxLength(length);
	}

	public enum GwtVerificationCodeTypeEnum{
		SIN_CAPTCHA,TIPO_NORMAL,TIPO_CON_SONIDO;
		
		public static GwtVerificationCodeTypeEnum getTipoByText(String key){
			if (key==null){
				return SIN_CAPTCHA;
			}
			else{
				if (key.toUpperCase().equals("TRUE")){
					return TIPO_NORMAL;
				}
				if (key.toUpperCase().equals("SONIDO")){
					return TIPO_CON_SONIDO;
				}
				return SIN_CAPTCHA;
			}
		}
	}
}
