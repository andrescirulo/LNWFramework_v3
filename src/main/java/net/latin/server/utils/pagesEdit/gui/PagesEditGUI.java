package net.latin.server.utils.pagesEdit.gui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.io.FileUtils;

import net.latin.server.utils.caseMaker.domain.fileMaker.XslUtils;
import net.latin.server.utils.caseMaker.ui.LookAndFeelUtil;
import net.latin.server.utils.pageGeneraror.core.PageGenerator;
import net.latin.server.utils.pagesEdit.domain.PagesEditListener;
import net.latin.server.utils.projectData.Nodo;
import net.latin.server.utils.projectData.ProjectData;
import net.latin.server.utils.projectData.UseCaseData;
import net.latin.server.utils.projectData.logic.ProjectStructureReader;

/**
 *
 * @author Maxi Ariosti
 */

@SuppressWarnings("serial")
public class PagesEditGUI extends javax.swing.JDialog{

	public String mockPagePath;
	public String imageFolderPath;
	public String initialPath;

	private List<String> newPages;
	private String projectPath;
	private PagesEditListener listener;
	private DefaultListModel pageListModel;
	private ProjectData projectData;
	private UseCaseData useCase;
//	private PageMakerScreen pageMakerScreen;

	private boolean updateGroup = false;

	public void init(String initialPath,String mockPagePath,String imageFolderPath) {
		this.initialPath = initialPath;
		this.imageFolderPath = imageFolderPath;
		this.mockPagePath = mockPagePath;

//		PageMakerScreen.init(initialPath, mockPagePath, imageFolderPath, new PageMakerConfig());
//		pageMakerScreen = new PageMakerScreen(initialPath, false);
//		pageMakerScreen.setListener(this);

	}


    /** Creates new form PagesEdit
     * @param projectData2
     * @param projectPath path absoluto del proyecto
     */
    public PagesEditGUI(PagesEditListener listener, String projectPath, ProjectData projectData) {

    	setModal(true);
    	this.projectPath = projectPath;
    	this.projectData = projectData;

		configureAppereance();
		initComponents();

		this.newPages = new ArrayList<String>();
		this.listener = listener;

		//window list
		pageListModel = new DefaultListModel();
		this.pageList.setModel(pageListModel);

//		pageList.addMouseListener(new MouseListener(){
//
//			@Override
//			public void mouseClicked(MouseEvent e) {
//			}
//			public void mouseEntered(MouseEvent e) {}
//			public void mouseExited(MouseEvent e) {}
//			public void mousePressed(MouseEvent e) {}
//			public void mouseReleased(MouseEvent e) {}
//
//		});


    }

    private void configureAppereance() {
    	try {
    		UIManager.setLookAndFeel( LookAndFeelUtil.getLookAndFeel() );
    	} catch (Exception ex) {
    		throw new RuntimeException( "Error stting XP look and feel", ex );
    	}
    	SwingUtilities.updateComponentTreeUI( this );

    	/*
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getWidth() / 2, dim.height / 2 - this.getHeight() / 2);
        */
    }



	public void onDialogClosed() {
		this.onClosedEdit();
	}

    public void cargarPageList(String useCaseName) {
    	projectData = ProjectStructureReader.getInstance().readProjectStructure(projectPath+"/config/GeneralConfig.xml");

    	this.setTitle("Lnw PageEdit - UseCase: " + useCaseName + "-");

		useCase = this.getUseCase(projectData.getUseCases(), useCaseName);
		for (Nodo nodo : useCase.getPages()) {
			pageListModel.addElement(nodo.getName());
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

	/**
	 * Devulve un objeto UseCaseData si es que el nombre del Caso se encuentra en la lista de Casos de Uso
	 * @param useCases Lista de Casos de Usos del tipo UseCaseData
	 * @param caseNameDelete nombre del Caso de Uso Ej: perfilAlta
	 * @return un UseCaseData si exite, null caso contrario
	 */
	private UseCaseData getUseCase(List<UseCaseData> useCases, String caseNameDelete) {
		UseCaseData caseFound = null;

		for (Iterator iterator = useCases.iterator(); iterator.hasNext();) {
			UseCaseData useCase = (UseCaseData) iterator.next();

			if (caseNameDelete.equals(useCase.getNombre())) {
				caseFound = useCase;
			}
		}
		return caseFound;
	}


    public void setVisible(boolean b) {
    	super.setVisible(b);
    	if(!b) listener.onDialogClosed();
    }

	public void clear() {
		consola.setText("");
		pageName.setText("");
    	pageListModel.clear();
    	newPages.clear();


	}

	private void onRemove(ActionEvent evt) {
		if( pageListModel.getSize() == 0 ) {
			return;
		}

		String selectedValue = (String)pageList.getSelectedValue();
		final String texto = "Estás seguro de eleminar la página: "+ selectedValue + " ?";
    	final int result = JOptionPane.showConfirmDialog(this, texto,projectData.getName(), JOptionPane.YES_NO_OPTION);

    	if (result == 0) {

			int n = pageList.getSelectedIndex();
			if( n < 0 ) {
				return;
			}

			pageListModel.remove( n );

    	}
		//FIXME VER TEMA DE ELIMINAR UNA PAGINA.JAVA
    	String pagePath = useCase.getAbsoluteClientPath() + "\\pages\\" + selectedValue + ".java";
    	File pageFile=new File(pagePath);
    	pageFile.delete();
	}


	private void onAdd(ActionEvent evt) {
		String name = pageName.getText();
		String nameOK = name;

		//validar
		if( name.equals( "" ) ) {
			JOptionPane.showMessageDialog(null, "Falta ingresar el nombre de la Page", "Error" ,JOptionPane.ERROR_MESSAGE);
			return;
		}

		if( pageExist( name.substring(0,1).toUpperCase() + name.substring(1) )) {
			JOptionPane.showMessageDialog(null, "Ya agregaste una pagina con ese nombre", "Error" ,JOptionPane.ERROR_MESSAGE);
			return;
		}

		if( !Character.isUpperCase( name.charAt( 0 ) ) ) {
			//JOptionPane.showMessageDialog(null, "Escriba el nombre de la page con la primera letra mayúscula", "Error" ,JOptionPane.ERROR_MESSAGE);
			nameOK = name.substring(0,1).toUpperCase() + name.substring(1);
			pageName.setText(nameOK);

		}

		String pagePath = useCase.getAbsoluteClientPath() + "\\pages\\" + nameOK + ".java";


		File page = new File(pagePath);
		try {
			page.createNewFile();
			this.escribirConsola(pagePath);
			PageGenerator.generateEmptyPage(pagePath);

			this.escribirConsola("> Se agrego una página nueva: " + nameOK);

			newPages.add(nameOK);
			pageListModel.addElement( nameOK );

		} catch (IOException e) {
			this.escribirConsola("> Error en la creación de la nueva página: " + nameOK);
			e.printStackTrace();
		}



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


//    private void onEdit(java.awt.event.ActionEvent evt) {
//
//    	String pageName = (String)pageList.getSelectedValue();
//		pageMakerScreen.createDialogs(this.initialPath);
//		pageMakerScreen.setPageMakerConfig();
//
//
//    	if (pageName != null) {
//    		pagePath = useCase.getAbsoluteClientPath() + "\\pages\\" + pageName + ".java";
//
//    		//VER SI ES UNA PAG NUEVA O VIEJA
//    		this.pageBackup(pagePath);
//			pageMakerScreen.setPageEdit(pagePath);
//
//			this.escribirConsola("> Se va a editar la página : " + pageName );
//			pageMakerScreen.setVisible(true);
//		}
//
//    }


    private void onUpdeteGroup(java.awt.event.ActionEvent evt) {

    	BufferedReader input = null;
    	String text;
    	String line;
    	StringBuffer output = new StringBuffer();
    	String firstPage = null;

    	String filePath = useCase.getGroup().getAbsolutePath();
    	String importText = "import " + this.projectData.getName().toLowerCase() +".client.useCases."+ this.useCase.getNombre()+".pages.";

    	boolean copiar = true;


		if (pageListModel.getSize() == 0) {
			JOptionPane.showMessageDialog(null, "Debe registrar al menos una página.", "Error" ,JOptionPane.ERROR_MESSAGE);
			return;
		}

		//obtengo el primer nombre de la pagina del listBox para luego definirla como pagina de inicio de use Case
		firstPage = (String) pageListModel.getElementAt(0);


		try {
			input = new BufferedReader( new FileReader(filePath) );
		} catch (FileNotFoundException e) {
			System.err.println("> ERROR en lectura del archivo: " + filePath );
			return;
		}

		try {
			while ((line = input.readLine())!= null){

				if ( line.contains("registerRpcServers")) {
					copiar = true;
				}

				if ( line.contains(this.useCase.getGroup().getName())) {

					//AGREGO IMPORTS NUEVOS
					for (Iterator i = newPages.iterator(); i.hasNext();) {
						String page = (String) i.next();

						text = importText + XslUtils.capitalize(page) + ";" +'\n' ;
						output.append(text);
					}

					output.append('\n');
					output.append(line);
					output.append('\n');
					output.append('\n');

					// AGREGO LAS CONTANTES DE LAS PAGINAS
				   	for (int i = 0; i < pageListModel.size(); i++) {
			    		String page = (String) pageListModel.get( i );

			    		text = "\t\t"+"public static final String " + XslUtils.toConstantFormat(page) + " = \"" + XslUtils.capitalize(page) + "\";";
						output.append(text);
						output.append('\n');
			    	}

				   	//REGISTRO LA PRIMERA PAGINA
				   	output.append('\n');
					text = "\t\t"+"protected String registerFirstPage() {";
					output.append(text);
					output.append('\n');

					output.append("\t\t\t"+"return " + XslUtils.toConstantFormat(firstPage) + ";");
					output.append('\n');
					output.append("\t\t}");
					output.append('\n');

					//REGISTRO LAS PAGINAS
				 	output.append('\n');
					text = "\t\t"+"protected void registerPages() {";
					output.append(text);
					output.append('\n');
					output.append('\n');

				   	for (int i = 0; i < pageListModel.size(); i++) {
			    		String page = (String) pageListModel.get( i );

						text = "\t\t\t"+"this.addPage(" + XslUtils.toConstantFormat(page) + ", new " + XslUtils.capitalize(page) + "());" +'\n' ;
						output.append(text);
						output.append('\n');
			    	}

				   	output.append("\t\t}");
				   	output.append('\n');
				   	output.append('\n');

				   	copiar = false;
				}

				if (copiar) {
					output.append(line);
					output.append('\n');
				}
			}

			input.close();
		}catch (IOException e) {
			e.printStackTrace();
			return;
		}


		this.deleteFile(filePath);
		if (!this.createAndWriteFile (filePath, output)) {
			System.out.println("> ERROR en recrear el archivo: " + filePath );
			return;
		}

		newPages.clear();
		updateGroup = true;
		this.escribirConsola("> Se actualizá archivo Group");

    }

    private void onCerrar(java.awt.event.ActionEvent evt) {
    	this.setVisible(false);
    	this.clear();
    }


	private void onPageName_KeyPressed(java.awt.event.KeyEvent e) {
    	if(e.getKeyCode() == KeyEvent.VK_ENTER)	onAdd(null);
    }



    private boolean pageBackup(String pagePath) {
		BufferedReader input = null;
		String line;
		StringBuffer output = new StringBuffer();


		try {
			input = new BufferedReader( new FileReader(pagePath) );
		} catch (FileNotFoundException e) {

			System.err.println("> ERROR: No se posee el archivo: " + pagePath );
			return false;
		}

		try {
			while ((line = input.readLine())!= null){

				output.append(line);
				output.append('\n');

			}
			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (this.createAndWriteFile (pagePath.replace(".java",".java.bak"), output)) {
			System.out.println("BackupPage Listo");
			return false;
		}

		return true;
	}

    private void onClosedEdit() {

    	final String texto = "Desea borrar el archivo de backup de la página editada?\nEn caso de no eliminarlo ahora, recuerde borrarlo manualmente luego.";

    	final int result = JOptionPane.showConfirmDialog(this, texto,"Lnw PageMaker", JOptionPane.YES_NO_OPTION);

    	if (result == 0) {
    		pagePath = pagePath.replace(".java",".java.bak");

    		File file = new File(pagePath);
    		try {
				FileUtils.forceDelete(file);
				this.escribirConsola("> Se borró archivo Backup...");
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}

    }
	private boolean createAndWriteFile(String filePath, StringBuffer output) {
		// create and write a new file
		try
		{
			File file = new File(filePath);
			file.createNewFile();

			BufferedWriter outfile = new BufferedWriter(new FileWriter(filePath));
			outfile.write(output.toString());
			outfile.close();

			return true;
		}
		catch (IOException e)    {
			System.err.println("> ERROR: No creó el archivo " + filePath );
			e.printStackTrace();  }

		return false;
	}

	private boolean deleteFile(String filePath)		{
		File file = new File(filePath);
		try {
			FileUtils.forceDelete(file);
			System.out.println("> Se borro el archivo " + filePath );
		} catch (IOException e) {
			System.err.println("Error con " + filePath);
			e.printStackTrace();
			return false;
		}


		return true;
	}

	private void escribirConsola(String text) {

		consola.setText(consola.getText()+ text + '\n');
	}









    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        add = new javax.swing.JButton();
        pageName = new javax.swing.JTextField();
        remove = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        pageList = new javax.swing.JList();
        down = new javax.swing.JButton();
        up = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
//        edit = new javax.swing.JButton();
        updeteGroup = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        consola = new javax.swing.JTextArea();
        cerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Lnw PageEdit");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                onCerrar(null);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Pages"));

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

        /*edit.setText("Edit");
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onEdit(evt);
            }
        });*/

        updeteGroup.setText("Actualizar Group");
        updeteGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onUpdeteGroup(evt);
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
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane2, 0, 0, Short.MAX_VALUE)
                                .addComponent(pageName, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(add, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
//                                    .addComponent(edit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(remove, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addComponent(up, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                                .addComponent(down, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                            .addContainerGap())
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(updeteGroup, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                            .addGap(104, 104, 104)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(add, 0, 0, Short.MAX_VALUE)
                    .addComponent(pageName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(up, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(down, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(remove, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        /*.addComponent(edit, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)*/)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updeteGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Nose Algo"));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 304, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 350, Short.MAX_VALUE)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Consola"));

        consola.setColumns(20);
        consola.setRows(5);
        jScrollPane1.setViewportView(consola);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
        );

        cerrar.setText("Cerrar");
        cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCerrar(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addContainerGap()))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cerrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(13, 13, 13))
        );

        pack();
    }// </editor-fold>



    // Variables declaration - do not modify
    private javax.swing.JButton add;
    private javax.swing.JButton cerrar;
    private javax.swing.JTextArea consola;
    private javax.swing.JButton down;
//    private javax.swing.JButton edit;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JList pageList;
    private javax.swing.JTextField pageName;
    private javax.swing.JButton remove;
    private javax.swing.JButton up;
    private javax.swing.JButton updeteGroup;
    // End of variables declaration
	private String pagePath;





}
