/*
 * LnwCaseMakerGui.java
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
 *
 * Created on 5 de noviembre de 2007, 08:27
 */

package net.latin.server.utils.caseMaker.ui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.latin.server.utils.caseMaker.domain.CaseMaker;
import net.latin.server.utils.clonarUseCases.domain.ClonarUseCases;
import net.latin.server.utils.deleteUseCase.domain.DeleteUseCase;
import net.latin.server.utils.pagesEdit.domain.PagesEditListener;
import net.latin.server.utils.pagesEdit.gui.PagesEditGUI;
import net.latin.server.utils.projectData.ProjectData;
import net.latin.server.utils.projectData.UseCaseData;
import net.latin.server.utils.projectData.logic.ProjectStructureReader;
import net.latin.server.utils.renameUseCase.domain.RenameUseCase;
import net.latin.server.utils.resources.ResourceFinder;

/**
 * Creador de casos de usos. Es un aplicacion que ademas de crear los casos,
 * nos permite modificarlos seleccionando alguno desde la lista de casos,
 * además podemos borrar (eliminar), clonar y renombrar un caso.
 * La edición de un caso nos remite a otra ventana "PageEdit" en donde
 * podemos cambiar la pagina de inicio del caso, agregar nuevas paginas, eliminar
 * algo ya existente y a su vez editar el contenido de las mismas por medio del "PageMaker"
 *
 * @author Matias Leone
 * @author Maxi Ariosti
 *
 */
public class LnwCaseMakerGui extends javax.swing.JFrame implements PagesEditListener{

	public static String initialPath;
    public static String mockPagePath;
    public static String imageFolderPath;

    //contiene los datos de sus respctivos ListBoxs
	private DefaultListModel pageListModel;
	private DefaultListModel useCaseListModel;

	private String projectPath;
	private ProjectData projectData;
	private PagesEditGUI pagesEdit;
	private String useCase;



	public static void init(String initialPath, String mockPagePath, String imageFolderPath ) {

		LnwCaseMakerGui.initialPath = initialPath;
		LnwCaseMakerGui.mockPagePath = mockPagePath;
		LnwCaseMakerGui.imageFolderPath = imageFolderPath;
	}


	/**Toma los valores que le envia el Launch del proyecto
	 * @param args the command line arguments
	 */
    public static void main(final String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            	if(LnwCaseMakerGui.initialPath == null && args.length == 3){
            		init(args[0],args[1],args[2]);
            		new LnwCaseMakerGui().setVisible(true);
            	} else{
            		new LnwCaseMakerGui().setVisible(true);
            	}
			}
		});
	}

	public LnwCaseMakerGui() {

		projectPath = ResourceFinder.getLocalProjectPath();
		System.out.println(projectPath);
		projectData = ProjectStructureReader.getInstance().readProjectStructure(projectPath+"/config/GeneralConfig.xml");

		pagesEdit = new PagesEditGUI(this, projectPath, projectData);
		pagesEdit.init(LnwCaseMakerGui.initialPath, LnwCaseMakerGui.mockPagePath, LnwCaseMakerGui.imageFolderPath);

		configureAppereance();
		initComponents();

		//window list
		pageListModel = new DefaultListModel();
		this.pageList.setModel(pageListModel);
		pageList.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && pageListModel.getSize() > 0) {
					pageName.setText((String) pageList.getSelectedValue());
					pageName.requestFocus();
					pageName.setSelectionStart(0);
					pageName.setSelectionEnd(pageName.getText().length());
					pageListModel.remove(pageList.getSelectedIndex());

				}
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}

		});

		useCaseListModel = new DefaultListModel();
		this.useCaseList.setModel(useCaseListModel);
		useCaseList.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && useCaseListModel.getSize() > 0) {
					onEdit(null);
				}
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}

		});

		cargarUseCaseList();

		this.setLocation( 50, 50 );
	}

	/**
	 * Crea un nuevo Caso de Uso
	 * @param evt
	 */
	private void onCreate(java.awt.event.ActionEvent evt) {
		String text = useCaseName.getText();
		if( !validateInput() ) {
			return;
		}


		List<String> pages = new ArrayList<String>();
		for (int i = 0; i < pageListModel.size(); i++) {
			pages.add( (String) pageListModel.get( i ) );
		}

		//ejecutar creacion
		CaseMaker caseMaker = new CaseMaker();
		String result = caseMaker.makeUseCase( text, pages);
		consola.setText( result );
		this.onRefresh(null);
	}

	private boolean validateInput() {
		//Use Case Name
		String useCase = useCaseName.getText();
		if( useCase.equals( "" ) ) {
			JOptionPane.showMessageDialog(null, "Falta ingresar el nombre del caso de uso", "Error" ,JOptionPane.ERROR_MESSAGE);
			return false;
		}

		//Al menos una page
		if( pageListModel.size() == 0 ) {
			JOptionPane.showMessageDialog(null, "Tiene que haber por lo menos una pagina", "Error" ,JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	private void onClear(ActionEvent evt) {
		pageListModel.clear();
	}


	/**
	 *Remueve una página del ListBox de Páginas, la misma todavía no fue creeada por el "CaseMaker"
	 *por ende es una baja lógica
	 */
	private void onRemove(ActionEvent evt) {
		if( pageListModel.getSize() == 0 ) {
			return;
		}
		int n = pageList.getSelectedIndex();
		if( n < 0 ) {
			return;
		}

		pageListModel.remove( n );
	}

	/**
	 * Agrega una página al Listbox de Páginas, no crea el .java correspondiente a esa página
	 */
	private void onAdd(ActionEvent evt) {
		String name = pageName.getText();

		//validar
		if( name.equals( "" ) ) {
			JOptionPane.showMessageDialog(null, "Falta ingresar el nombre de la Page", "Error" ,JOptionPane.ERROR_MESSAGE);
			return;
		}
		if( !Character.isUpperCase( name.charAt( 0 ) ) ) {
			//JOptionPane.showMessageDialog(null, "Escriba el nombre de la page con la primera letra mayúscula", "Error" ,JOptionPane.ERROR_MESSAGE);
			String nameOK = name.substring(0,1).toUpperCase() + name.substring(1);
			pageName.setText(nameOK);
			pageListModel.addElement( nameOK );
			return;
		}
		if( pageExist( name ) ) {
			JOptionPane.showMessageDialog(null, "Ya agregaste una pagina con ese nombre", "Error" ,JOptionPane.ERROR_MESSAGE);
			return;
		}

		pageListModel.addElement( name );


	}


	/**me habilita la acción de renombrar el caso de uso ( boton y texto) */
    private void onRenameChange(javax.swing.event.ChangeEvent evt) {
    	setEnableRenameNow(rename.isSelected());
    	newCaseName.requestFocus();
    }

    /**me habilita la acción de clonar el caso de uso ( boton y textbox)
     * @param evt
     */
    private void onClonarChange(javax.swing.event.ChangeEvent evt) {
    	setEnableClonNow(clonar.isSelected());
    	clonCaseName.requestFocus();
    }

	/**Da curso a la acción de clonar un caso de uso
	 */
	private void onClonNow(java.awt.event.ActionEvent evt) {

		String selectedValue = (String) useCaseList.getSelectedValue();
		String newName = clonCaseName.getText();
		final String texto = "Estás seguro de Clonar el Caso de Uso: "+ selectedValue + " con el nombre "+ newName +" ?";

		if( newName.equals( "" ) ) {
			JOptionPane.showMessageDialog(null, "Debe ingresar como se llamará el Use Case a Clonar", "Error" ,JOptionPane.ERROR_MESSAGE);
			return;
		}

		if( Character.isUpperCase( newName.charAt( 0 ) ) ) {
			JOptionPane.showMessageDialog(null, "Escriba del useCase con la primera letra en minúscula.", "Error" ,JOptionPane.ERROR_MESSAGE);
			return;
		}

		final int result = JOptionPane.showConfirmDialog(this, texto,projectData.getName(), JOptionPane.YES_NO_OPTION);

    	if (result == 0) {

		String text = ClonarUseCases.getInstance().clonarUseCases((String) useCaseList.getSelectedValue(), newName, projectPath, projectData);

		this.consola.setText(text);
		setEnableClonNow(false);
		onRefresh(null);

		JOptionPane.showMessageDialog(null,"Operación Completa. Verifique en consola los resultados.");
    	}
	}


	/**Da curso a la acción de renombrar un caso de uso, pero previamente validandolo
	 */
	private void onRenameNow(java.awt.event.ActionEvent evt) {

		String selectedValue = (String) useCaseList.getSelectedValue();
		String newName = newCaseName.getText();
		final String texto = "Estás seguro de Renombrar el Caso de Uso: "+ selectedValue + " por "+ newName +" ?";

		if( newName.equals( "" ) ) {
			JOptionPane.showMessageDialog(null, "Debe ingresar como se llamará el Use Case a Clonar", "Error" ,JOptionPane.ERROR_MESSAGE);
			return;
		}

		if( Character.isUpperCase( newName.charAt( 0 ) ) ) {
			JOptionPane.showMessageDialog(null, "Escriba del useCase con la primera letra en minúscula.", "Error" ,JOptionPane.ERROR_MESSAGE);
			return;
		}

		final int result = JOptionPane.showConfirmDialog(this, texto,projectData.getName(), JOptionPane.YES_NO_OPTION);

    	if (result == 0) {

		RenameUseCase.getInstance().renameUseCase(selectedValue, newName, projectPath, projectData);
		setEnableRenameNow(false);
		onRefresh(null);

	    JOptionPane.showMessageDialog(null,"Operación Completa. Verifique en consola los resultados.");

    	}
	}

	/**
	 * actuliza el ListBox con los nombres de los casos de uso del proyecto
	 */
    private void onRefresh(java.awt.event.ActionEvent evt) {
		projectPath = ResourceFinder.getLocalProjectPath();
		projectData = ProjectStructureReader.getInstance().readProjectStructure(projectPath+"/config/GeneralConfig.xml");

		useCaseListModel.removeAllElements();
		this.cargarUseCaseList();
    }


	private void onUp(java.awt.event.ActionEvent evt) {
        int n = pageList.getSelectedIndex();
        if (n < 0 || n == 0) {
            return;
        }
        String element = (String) pageListModel.get( n );
        pageListModel.remove( n );
        pageListModel.add(n - 1, element);
        pageList.setSelectedIndex(n - 1);
    }//GEN-LAST:event_accionUp



	private void onDown(java.awt.event.ActionEvent evt) {
        int n = pageList.getSelectedIndex();
        if (n < 0 || n == pageListModel.size() - 1) {
            return;
        }
        String element = (String) pageListModel.get( n );
        pageListModel.remove( n );
        pageListModel.add(n + 1, element);
        pageList.setSelectedIndex(n + 1);
    }//GEN-LAST:event_accionDown

	/**
	 * @param evt
	 */
	private void onDelete(java.awt.event.ActionEvent evt) {

    	String selectedValue = (String)useCaseList.getSelectedValue();
		final String texto = "Estás seguro de eleminar el Caso de Uso: "+ selectedValue + " ?";
    	final int result = JOptionPane.showConfirmDialog(this, texto,projectData.getName(), JOptionPane.YES_NO_OPTION);

    	if (result == 0) {

    		DeleteUseCase.getInstance().borrarCaso( selectedValue,projectPath, projectData);

    		if( useCaseListModel.getSize() == 0 ) {
    			return;
    		}
    		int n = useCaseList.getSelectedIndex();
    		if( n < 0 ) {
    			return;
    		}

    		useCaseListModel.remove( n );
    		JOptionPane.showMessageDialog(this,"Operación Completa. Verifique en consola si se borraron todos los archivos.");
    	}
	}

	/**
	 * Edita el caso de eso elegido, también se puede elegir uno haciendo doble click sobre el nombre del caso
	 */
	private void onEdit(java.awt.event.ActionEvent evt) {

		String useCase = (String) useCaseList.getSelectedValue();

		pagesEdit.clear();
		pagesEdit.cargarPageList(useCase);
		pagesEdit.setVisible(true);

	}



	@Override
	public void onDialogClosed() {
		this.repaint();

	}


    private void onUseCaseList_change(javax.swing.event.ListSelectionEvent evt) {
        // TODO add your handling code here:
    }




	//******* ENTERS *********

    private void onPageName_KeyPressed(java.awt.event.KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER)	onAdd(null);
    }

    private void onUseCaseName_KeyPressed(java.awt.event.KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER)	pageName.requestFocus();
    }

    private void onNewNameCase_KeyPressed(java.awt.event.KeyEvent e) {
		String text = newCaseName.getText();

		if( !Character.isLowerCase( text.charAt( 0 ) ) ) {
			String textOK = text.substring(0,1).toLowerCase() + text.substring(1);
			newCaseName.setText(textOK);
		}

        if(e.getKeyCode() == KeyEvent.VK_ENTER)	onRenameNow(null);
    }

    private void onClonCaseName_KeyPressed(java.awt.event.KeyEvent e) {
		String text = clonCaseName.getText();

		if( !Character.isLowerCase( text.charAt( 0 ) ) ) {
			String textOK = text.substring(0,1).toLowerCase() + text.substring(1);
			clonCaseName.setText(textOK);
		}

    	if(e.getKeyCode() == KeyEvent.VK_ENTER)	onClonNow(null);
    }


    private void onUseCaseName_LostFocus(java.awt.event.FocusEvent evt) {
		String text = useCaseName.getText();
    	if( !text.equals("") && !Character.isLowerCase( text.charAt( 0 ) )  ) {
    		String textOK = text.substring(0,1).toLowerCase() + text.substring(1);
    		useCaseName.setText(textOK);
    	}
    }

    //********* OTROS *************

	private void setEnableClonNow(boolean bool) {
		clonNow.setEnabled(bool);
		clonCaseName.setEnabled(bool);
		if(bool) {
			clonCaseName.setText((String) this.useCaseList.getSelectedValue());
			clonCaseName.setSelectionStart(0);
			clonCaseName.setSelectionEnd(clonCaseName.getText().length());
		}else {
			clonCaseName.setText("");
			clonar.setSelected(false);
		}
	}

	private void setEnableRenameNow(boolean bool) {
		renameNow.setEnabled(bool);
		newCaseName.setEnabled(bool);
		if(bool) {
			newCaseName.setText((String) this.useCaseList.getSelectedValue());
			newCaseName.setSelectionStart(0);
			newCaseName.setSelectionEnd(newCaseName.getText().length());
		}else {
			newCaseName.setText("");
			rename.setSelected(false);
		}
	}


    private void cargarUseCaseList() {
		for (UseCaseData useCase : projectData.getUseCases()) {
    		useCaseListModel.addElement(useCase.getNombre());
		}
    }


    private boolean pageExist(String name) {
    	for (int i = 0; i < pageListModel.size(); i++) {
    		String pageName = (String) pageListModel.get( i );
    		if( name.equals( pageName ) ) {
    			return true;
    		}
    	}
    	return false;
    }


    private void configureAppereance() {
    	try {
    		UIManager.setLookAndFeel( LookAndFeelUtil.getLookAndFeel() );
    	} catch (Exception ex) {
    		throw new RuntimeException( "Error stting XP look and feel", ex );
    	}
    	SwingUtilities.updateComponentTreeUI( this );
    }













    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        useCaseName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        add = new javax.swing.JButton();
        pageName = new javax.swing.JTextField();
        remove = new javax.swing.JButton();
        clear = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        pageList = new javax.swing.JList();
        down = new javax.swing.JButton();
        up = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        consola = new javax.swing.JTextArea();
        create = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        delete = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        renameNow = new javax.swing.JButton();
        newCaseName = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        clonNow = new javax.swing.JButton();
        clonCaseName = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        useCaseList = new javax.swing.JList();
        jSeparator3 = new javax.swing.JSeparator();
        refresh = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        clonar = new javax.swing.JToggleButton();
        rename = new javax.swing.JToggleButton();
        jSeparator5 = new javax.swing.JSeparator();
        editUseCase = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Lnw CaseMaker v1.1");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("New UseCase"));

        jLabel1.setText("UseCase Name:");

        useCaseName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                onUseCaseName_LostFocus(evt);
            }
        });
        useCaseName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                onUseCaseName_KeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 2, 11));
        jLabel2.setText("( Introduzca el nombre del caso de uso de la forma: \"myUseCaseName\" )");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Pages"));

        jLabel3.setText("Page:");

        add.setText("Add");
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAdd(evt);
            }
        });

        pageName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                onPageName_KeyPressed(evt);
            }
        });

        remove.setText("Remove");
        remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemove(evt);
            }
        });

        clear.setText("Clear");
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onClear(evt);
            }
        });

        pageList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(pageList);

        down.setText("Down");
        down.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onDown(evt);
            }
        });

        up.setText("Up");
        up.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onUp(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Arial", 2, 11));
        jLabel9.setText("( La Primera página se registró como página de Inicio) ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, 0, 0, Short.MAX_VALUE)
                            .addComponent(pageName, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(add, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                            .addComponent(clear, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                            .addComponent(remove, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                            .addComponent(up, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(down, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))))
                    .addComponent(jLabel9))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(pageName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(clear, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(remove, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(up)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(down)
                        .addGap(33, 33, 33))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Resultado"));

        consola.setColumns(20);
        consola.setRows(5);
        jScrollPane1.setViewportView(consola);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
        );

        create.setText("Create");
        create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCreate(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(useCaseName, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(create, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(useCaseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(create, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Uses Cases"));

        jLabel4.setText("Selecciones un UseCase:");

        delete.setText("Delete");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onDelete(evt);
            }
        });

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Rename Use Case selected by:"));

        renameNow.setText("Rename NOW!");
        renameNow.setEnabled(false);
        renameNow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRenameNow(evt);
            }
        });

        newCaseName.setEnabled(false);
        newCaseName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                onNewNameCase_KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newCaseName, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(renameNow, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newCaseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(renameNow))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Clon Use Case selected by:"));

        clonNow.setText("Clon NOW!");
        clonNow.setEnabled(false);
        clonNow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onClonNow(evt);
            }
        });

        clonCaseName.setEnabled(false);
        clonCaseName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                onClonCaseName_KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(clonCaseName, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clonNow, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clonCaseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clonNow))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel7.setFont(new java.awt.Font("Arial", 2, 11));
        jLabel7.setText("*BORRAR MANUALMENTE aquellos archivos que no se borraron con la aplicación.");

        jLabel6.setFont(new java.awt.Font("Arial", 2, 11));

        jLabel5.setFont(new java.awt.Font("Arial", 2, 11));
        jLabel5.setText("( Introduzca el nombre del caso de uso de la forma: \"myUseCaseName\" )");

        useCaseList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        useCaseList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                onUseCaseList_change(evt);
            }
        });
        jScrollPane3.setViewportView(useCaseList);

        refresh.setText("Refresh");
        refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRefresh(evt);
            }
        });

        clonar.setText("Clonar");
        clonar.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                onClonarChange(evt);
            }
        });

        rename.setText("Rename");
        rename.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                onRenameChange(evt);
            }
        });

        editUseCase.setText("Edit");
        editUseCase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onEdit(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(editUseCase, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                                        .addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                                        .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                                        .addComponent(jSeparator4, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                                        .addComponent(delete, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                                        .addComponent(clonar, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                                        .addComponent(refresh, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                                        .addComponent(rename, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)))
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(29, 29, 29)
                        .addComponent(jLabel6)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clonar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rename, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editUseCase, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(51, 51, 51))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>


	    // Variables declaration - do not modify
	    private javax.swing.JButton add;
	    private javax.swing.JButton clear;
	    private javax.swing.JTextField clonCaseName;
	    private javax.swing.JButton clonNow;
	    private javax.swing.JToggleButton clonar;
	    private javax.swing.JTextArea consola;
	    private javax.swing.JButton create;
	    private javax.swing.JButton delete;
	    private javax.swing.JButton down;
	    private javax.swing.JButton editUseCase;
	    private javax.swing.JLabel jLabel1;
	    private javax.swing.JLabel jLabel2;
	    private javax.swing.JLabel jLabel3;
	    private javax.swing.JLabel jLabel4;
	    private javax.swing.JLabel jLabel5;
	    private javax.swing.JLabel jLabel6;
	    private javax.swing.JLabel jLabel7;
	    private javax.swing.JLabel jLabel9;
	    private javax.swing.JPanel jPanel1;
	    private javax.swing.JPanel jPanel2;
	    private javax.swing.JPanel jPanel3;
	    private javax.swing.JPanel jPanel4;
	    private javax.swing.JPanel jPanel6;
	    private javax.swing.JPanel jPanel7;
	    private javax.swing.JScrollPane jScrollPane1;
	    private javax.swing.JScrollPane jScrollPane2;
	    private javax.swing.JScrollPane jScrollPane3;
	    private javax.swing.JSeparator jSeparator1;
	    private javax.swing.JSeparator jSeparator2;
	    private javax.swing.JSeparator jSeparator3;
	    private javax.swing.JSeparator jSeparator4;
	    private javax.swing.JSeparator jSeparator5;
	    private javax.swing.JTextField newCaseName;
	    private javax.swing.JList pageList;
	    private javax.swing.JTextField pageName;
	    private javax.swing.JButton refresh;
	    private javax.swing.JButton remove;
	    private javax.swing.JToggleButton rename;
	    private javax.swing.JButton renameNow;
	    private javax.swing.JButton up;
	    private javax.swing.JList useCaseList;
	    private javax.swing.JTextField useCaseName;
	    // End of variables declaration




}
