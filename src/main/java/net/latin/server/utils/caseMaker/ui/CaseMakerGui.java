package net.latin.server.utils.caseMaker.ui;
/**
 * CaseMakerUi es la interfaz gráfica que engloba a
 * CaseMaker, que es el encargado de crear los folder cada
 * vez que se requiera agregar un case al proyecto.
 * Para utilizarlo hay que buscar el Proyecto.gwt.xml,
 * una vez encontrado crea las carpetas necesarias.
 *
 * @author Santiago Aimetta
 */

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.latin.server.utils.caseMaker.domain.fileMaker.XslUtils;



public class CaseMakerGui extends JFrame {


	private static final int WINDOW_WIDTH = 683;
	private static final int WINDOW_HEIGHT = 600;


	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JLabel jLabelNombre = null;

	private JTextField jTextFieldNombreCase = null;

	private JLabel jLabelSeleccionar = null;

	private JButton jButtonBuscar = null;

	private JButton jButtonGenerar = null;

	private static final String UrlBusqueda = "C:\\development\\eclipse\\workspace";  //  @jve:decl-index=0:

	private static final String EXTENSION = ".gwt.xml";  //  @jve:decl-index=0:

	private static final String DESCRIPCION = "Archivos *.GWT.XML";  //  @jve:decl-index=0:

	private JFileChooser fileChooser = null;

	private File UrlArchivo = null;

	private JScrollPane jScrollPane = null;

	private JTextArea jTextArea = null;

	public CaseMakerGui() throws HeadlessException {
		// TODO Auto-generated constructor stub
		super();
		initialize();
	}

	public CaseMakerGui(GraphicsConfiguration gc) {
		super(gc);
		// TODO Auto-generated constructor stub
		initialize();
	}

	public CaseMakerGui(String title) throws HeadlessException {
		super(title);
		// TODO Auto-generated constructor stub
		initialize();
	}

	public CaseMakerGui(String title, GraphicsConfiguration gc) {
		super(title, gc);
		// TODO Auto-generated constructor stub
		initialize();
	}

	/**
	 * This method initializes jTextFieldNombreCase
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldNombreCase() {
		if (jTextFieldNombreCase == null) {
			jTextFieldNombreCase = new JTextField();
			jTextFieldNombreCase.setBounds(new Rectangle(220, 20, 200, 23));
			jTextFieldNombreCase.addKeyListener( new KeyListener() {
				@Override
				public void keyPressed(KeyEvent e) {
					if( e.getKeyCode() == KeyEvent.VK_ENTER ) {
						buscarXml();
					}
				}
				public void keyReleased(KeyEvent e) {
				}
				public void keyTyped(KeyEvent e) {
				}
			});

		}
		return jTextFieldNombreCase;
	}

	/**
	 * This method initializes jButtonBuscar
	 *
	 * @return javax.swing.JButton
	 */

	private JButton getJButtonBuscar() {
		if (jButtonBuscar == null) {
			jButtonBuscar = new JButton();
			jButtonBuscar.setBounds(new Rectangle(28, 91, 144, 33));
			jButtonBuscar.setText("Buscar");
			jButtonBuscar.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					buscarXml();
				}

			});
		}
		return jButtonBuscar;
	}

	private void buscarXml() {
		jTextArea.setText("");
		if(jTextFieldNombreCase.getText().matches("")){
			JOptionPane.showMessageDialog(null, "Falta ingresar el nombre del case","Error",JOptionPane.ERROR_MESSAGE);
		}else{

			fileChooser.setVisible(true);
			int returnVal = fileChooser.showOpenDialog(CaseMakerGui.this);
			if( returnVal == JFileChooser.APPROVE_OPTION ){//le dio aceptar
				//me traigo la url
				UrlArchivo  = fileChooser.getSelectedFile();
				jLabelSeleccionar.setText("Selecciono "+UrlArchivo.getName());
				jButtonGenerar.setVisible(true);


			}
			if(returnVal == JFileChooser.CANCEL_OPTION){//le dio cancelar
				return;
			}
		}
	}

	/**
	 * This method initializes jButtonGenerar
	 *
	 * @return javax.swing.JButton
	 */

	private JButton getJButtonGenerar() {
		if (jButtonGenerar == null) {
			jButtonGenerar = new JButton();
			jButtonGenerar.setBounds(new Rectangle(310, 91, 136, 36));
			jButtonGenerar.setText("Generar");
			jButtonGenerar.setVisible(false);
			jButtonGenerar.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(java.awt.event.ActionEvent e) {
					generarCase();

				}

			});
		}
		return jButtonGenerar;
	}

	private void generarCase() {
		if(UrlArchivo.getName().matches("")){
			JOptionPane.showMessageDialog(null, "No se seleccionó el archivo","Error",JOptionPane.ERROR_MESSAGE);
			return;
		}else {
			//LLamo a caseMaker y genero los folders
			try{
				//CaseMaker.getInstance().makeCaseFolders(UrlArchivo,jTextFieldNombreCase.getText());
				JOptionPane.showMessageDialog(null, "Carpetas generadas con exito","Información",JOptionPane.INFORMATION_MESSAGE);
				jTextArea.setVisible(true);
				jTextArea.setText("/**COPIE ESTE TEXTO EN struts-congif.xml**/\n\n");
				jTextArea.append(armarTxtStruts());
				jTextArea.append("\n\n/**COPIE ESTE TEXTO EN GeneralConfig.xml**/\n");
				jTextArea.append(armarTxtGeneralConfig());
				jTextArea.append("\n/**No olvide actualizar su Controller y las referencias a UseCaseNames**/");
				jTextArea.append("\n/*pegue esta línea en UseCaseNames*/\n");
				jTextArea.append(armarTxtUseCaseNames());
			}
			catch(Exception e2){
				JOptionPane.showMessageDialog(null, "Ha fallado la generación del caso de uso","Error",JOptionPane.ERROR_MESSAGE);
			}


		}
	}
	/**
	 * Armo el string para pegar en el struts config
	 * @return
	 */
	private String armarTxtStruts() {
//		//TODO VER AQUI!
//		//String caseDir = DataGetterStatic.getPackage();
//		String nombreCase = XslUtils.capitalize((jTextFieldNombreCase.getText()))+"Case";
//		caseDir = caseDir.replace(';', '.');
//		caseDir = caseDir + nombreCase;
//
//		String struts = "<action path='/";
//		struts = struts + this.jTextFieldNombreCase.getText();
//		struts = struts + "'\n";
//		struts = struts + "     name='DynamicForm'\n";
//		struts = struts + "     type='"+caseDir+"'\n";
//		struts = struts + "     scope='request'>\n";
//		struts = struts + "</action>";
//		return struts;
		return null;
	}

	private String armarTxtGeneralConfig(){
		String nombreCase = XslUtils.capitalize((jTextFieldNombreCase.getText()))+"Case";
		String gconfig = "<UseCase id='"+nombreCase+"'  title='COMPLETAR-AQUI'/>";
		return gconfig;
	}

	private String armarTxtUseCaseNames(){
		String nombreCase = XslUtils.capitalize((jTextFieldNombreCase.getText()))+"Case";
		String nombreCaseConstant = XslUtils.toConstantFormat(nombreCase);
		String result = "public static final String "+nombreCaseConstant+" ='"+nombreCase+"';";
		return  result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				CaseMakerGui thisClass = new CaseMakerGui();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
				thisClass.jTextFieldNombreCase.requestFocus();
			}
		});
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setContentPane(getJContentPane());
		configureAppereance();
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation( dimension.width / 2 - WINDOW_WIDTH / 2, dimension.height / 2 - WINDOW_HEIGHT / 2 );
		this.setTitle("CaseMaker");

	}

	private void configureAppereance() {
		try {
    		UIManager.setLookAndFeel( LookAndFeelUtil.getLookAndFeel() );
    	} catch (Exception ex) {
    		throw new RuntimeException( "Error stting XP look and feel", ex );
		}
    	SwingUtilities.updateComponentTreeUI( this );
	}

	/**
	 * This method initializes jContentPane
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabelSeleccionar = new JLabel();
			jLabelSeleccionar.setBounds(new Rectangle(97, 50, 269, 24));
			jLabelSeleccionar.setText("Seleccione el archivo *.gwt.xml de su proyecto");
			jLabelNombre = new JLabel();
			jLabelNombre.setBounds(new Rectangle(16, 17, 200, 40));
			jLabelNombre.setText("Ingrese el nombre del case en minúsculas");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(jLabelNombre, null);
			jContentPane.add(getJTextFieldNombreCase(), null);

			jContentPane.add(jLabelSeleccionar, null);
			jContentPane.add(getJButtonBuscar(), null);
			jContentPane.add(getJButtonGenerar(), null);
			jContentPane.add(getJFileChooser(),null);
			jContentPane.add(getJScrollPane(),null);
		}
		return jContentPane;
	}

	private  JFileChooser getJFileChooser(){
		if( fileChooser == null ){
			fileChooser = new JFileChooser();
			fileChooser.setVisible(false);
		    fileChooser.setCurrentDirectory(new File(UrlBusqueda));
		    CustomFilter filtro = new CustomFilter(EXTENSION,DESCRIPCION);
		    fileChooser.setFileFilter(filtro);

		}
		return fileChooser;
	}

	private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
    	jScrollPane = new JScrollPane(this.getJTextArea(),JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
    							,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    	jScrollPane.setBounds(new Rectangle(50, 150, 576, 279));
    	jScrollPane.setViewportView(getJTextArea());
        }
        return jScrollPane;
    }

    /**
     * This method initializes jTextArea
     *
     * @return javax.swing.JTextArea
     */
    private JTextArea getJTextArea() {
        if (jTextArea == null) {
    	jTextArea = new JTextArea();
        }
        jTextArea.setEditable(true);
        jTextArea.setVisible(false);
        return jTextArea;
    }


}  //  @jve:decl-index=0:visual-constraint="10,10"
