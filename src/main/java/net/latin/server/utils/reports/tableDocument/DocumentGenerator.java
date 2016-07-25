package net.latin.server.utils.reports.tableDocument;

import java.util.HashMap;
import java.util.Map;

import net.latin.client.rpc.commonUseCase.LnwTableDocumentData;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;

public class DocumentGenerator {

	private static final int PAGE_HEIGHT = 842;
	private static final int PAGE_WIDTH = 595;
	private static final int TOP_MARGIN = 10;
	private static final int BOTTOM_MARGIN = 10;
	private static final int RIGHT_MARGIN = 10;
	private static final int LEFT_MARGIN = 10;
	
	private static final int TITLE_HEIGHT = 80;
	private static final int COLUMN_HEADER_WIDTH = 575;
	private static final int COLUMN_HEIGHT = 30;
	private static final int DETAIL_HEIGHT = 20;
	
	public static int COLUMN_WIDTH = 150;
	
	public static JasperPrint makeDocument( LnwTableDocumentData data ) throws JRException {
		//Seteo la cantidad de columnas
		setColumnWidth( data.getColumns().length );
		
		Map parameters = new HashMap();
		//Agrego el nombre del reporte a los parametros
		parameters.put( "ReportTitle", data.getTitle() );
		
		//Creo el diseño del reporte
		JasperDesign jasperDesign = createJasperDesign( data.getTitle() );
		
		//Agrego el parametro titulo al diseño del reporte
		addParameterToDesign( jasperDesign, "ReportTitle" );
		
		//Creo y agrego un band title al diseño del reporte
		JRDesignBand title = new JRDesignBand();
		title.setHeight( TITLE_HEIGHT );
		jasperDesign.setTitle( title );

		//Agrego un textField que contendra la expression y el style en el title
		addTextFieldToTitle( title, "ReportTitle" );
		
		//Creo y agrego un band columnHeader al diseño del reporte
		JRDesignBand columnHeader = new JRDesignBand();
		columnHeader.setHeight( COLUMN_HEIGHT );
		jasperDesign.setColumnHeader( columnHeader );
		
		//Creo y agrego un band details al diseño del reporte
		JRDesignBand detail = new JRDesignBand();
		detail.setHeight( DETAIL_HEIGHT );
//		jasperDesign.setDetail( detail );
		((JRDesignSection)jasperDesign.getDetailSection()).addBand(detail);
		
		//Agrego las columnas al diseño del reporte
		String[] columnsNames = data.getColumns();
		for( int i = 0; i < columnsNames.length; i++ ) {
			
			//Agrego el campo "nombre de la columna" al diseño del reporte
			addFieldToDesign( jasperDesign, columnsNames[ i ] );
							
			//Agrego un staticText con el nombre de la columna en el details
			addColumnNameToDetail( columnHeader, columnsNames[ i ], i );
			
			//Agrego un textField que contendra la expression y el style en el details
			addDataColumnToDetail( detail, columnsNames[ i ], i );
		}
		
		//Genero el reporte
		JasperReport jasperReport = JasperCompileManager.compileReport( jasperDesign );
		
		JasperPrint jasperPrint = JasperFillManager.fillReport( jasperReport, parameters, 
			new JRTableModelDataSource(
					new CustomTableModel( data.getColumns(), data.getRowsData() ) ) );
			
		return jasperPrint;		
	}
	
	
	private static JasperDesign createJasperDesign( String title ) {
		JasperDesign jasperDesign = new JasperDesign();
		jasperDesign.setName( title );
		jasperDesign.setPageHeight( PAGE_HEIGHT );
		jasperDesign.setPageWidth( PAGE_WIDTH );
		jasperDesign.setTopMargin( TOP_MARGIN );
		jasperDesign.setBottomMargin( BOTTOM_MARGIN );
		jasperDesign.setRightMargin( RIGHT_MARGIN );
		jasperDesign.setLeftMargin( LEFT_MARGIN );
		jasperDesign.setColumnCount( 1 );
		jasperDesign.setColumnSpacing( 0 );
		jasperDesign.setColumnWidth( COLUMN_HEADER_WIDTH );
		return jasperDesign;
	}

	private static void addParameterToDesign( JasperDesign jasperDesign, String nombre ) throws JRException {
		JRDesignParameter parameter = new JRDesignParameter();
		parameter.setName( nombre );
		parameter.setValueClass( String.class );
		jasperDesign.addParameter( parameter );
	}
	
	private static void addTextFieldToTitle( JRDesignBand title, String name ) {
		JRDesignTextField textField = new JRDesignTextField();
		textField.setX( 0 );
		textField.setY( 0 );
		textField.setWidth( COLUMN_HEADER_WIDTH );
		textField.setHeight( TITLE_HEIGHT );
		textField.setHorizontalAlignment( HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment( VerticalAlignEnum.MIDDLE );
		textField.setExpression( getExpressionTitle( name ) );
		setFontTitle( textField ); 
		title.addElement( textField );
	}
	
	private static JRExpression getExpressionTitle( String name ) {
		//Creo una expression que contendra al titulo
		JRDesignExpression expression = new JRDesignExpression();
		expression.addParameterChunk( name );
		expression.setValueClass( String.class );
		return expression;
	}
	
	private static void addFieldToDesign( JasperDesign jasperDesign, String nombre ) throws JRException {
		JRDesignField field = new JRDesignField();
		field.setName( nombre );
		field.setValueClass( String.class );
		jasperDesign.addField( field );		
	}
	
	private static void addColumnNameToDetail( JRDesignBand details, String columnName, int positionY ) {
		JRDesignStaticText text = new JRDesignStaticText();
		text.setX( COLUMN_WIDTH * positionY );
		text.setY( 0 );
		text.setWidth( COLUMN_WIDTH ); 
		text.setHeight( COLUMN_HEIGHT );
		text.setHorizontalAlignment( HorizontalAlignEnum.CENTER);
		text.setVerticalAlignment( VerticalAlignEnum.MIDDLE );
		text.setText( columnName );
		text.getLineBox().getPen().setLineWidth(2);
//		text.setBorder( (byte)2 );
		setFontColumnName( text );
		details.addElement( text );
	}
	
	private static void addDataColumnToDetail( JRDesignBand details, String columnName, int positionY ) {
		JRDesignTextField textField = new JRDesignTextField();
		textField.setX( COLUMN_WIDTH * positionY );
		textField.setY( 0 );
		textField.setWidth( COLUMN_WIDTH ); 
		textField.setHeight( DETAIL_HEIGHT );
		textField.setHorizontalAlignment( HorizontalAlignEnum.CENTER);
		textField.setVerticalAlignment( VerticalAlignEnum.MIDDLE );
		textField.setExpression( getExpressionDetail( columnName ) );
//		textField.setBorder( (byte)1 );
		textField.getLineBox().getPen().setLineWidth(1);
		setFontDataColumn( textField );
		details.addElement( textField );
	}

	private static JRExpression getExpressionDetail( String columnName ) {
		//Creo una expression que contendra al nombre de la columna  
		JRDesignExpression expression = new JRDesignExpression();
		expression.addFieldChunk( columnName ); 
		expression.setValueClass( String.class );
		return expression;
	}
	
	private static void setFontTitle( JRDesignTextField text ) {
		text.setFontName( "Arial" );
		text.setFontSize( 28 );
		text.setBold( true );
	}

	private static void setFontColumnName( JRDesignStaticText text ) {
		text.setFontName( "Arial" );
		text.setFontSize( 12 );
		text.setBold( true );
	}
	
	private static void setFontDataColumn( JRDesignTextField text ) {
		text.setFontName( "Arial" );
		text.setFontSize( 10 );
	}
	
	private static void setColumnWidth( int columnCount ) {
		COLUMN_WIDTH = COLUMN_HEADER_WIDTH / columnCount;		
	}
	
}
