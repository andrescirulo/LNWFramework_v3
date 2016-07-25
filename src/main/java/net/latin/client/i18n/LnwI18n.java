package net.latin.client.i18n;

import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

/**
 * <p>
 * Clase que registra todas las key que utilizan
 * los widgets para configuración de parámetros,
 * que no se pueden setear en CSS.
 * <p>
 * Todos los widgets sacan su configuración interna de
 * aquí (aparte de los CSS). Por default están todas vacías.
 * <p>
 * Los parámetros de configuración son constantes
 * públicas.
 * <p>
 * Cada implementación de LnwI18n puede redefinir estas
 * constantes.
 *
 * @author Matias Leone
 */
public abstract class LnwI18n {


	public LnwI18n() {
		registerParameters();
	}
	/**
	 * Método para registrar el valor de las constantes.
	 * Sobrescribir este método para cambiar los valores de todas las constantes.
	 */
	protected abstract void registerParameters();


	//###############################CONSTANTES#####################################//

	/**
	 * GwtLabel
	 */
	public String GwtLabel_width;
	public String GwtLabel_height;

	/**
	 * GwtListSuggest
	 */
	public String GwtListSuggest_searchButton_text;
	public String GwtListSuggest_noResult_text;

	/**
	 * GwtTableSuggest
	 */
	public String GwtTableSuggest_searchButton_text;
	public String GwtTableSuggest_noResult_text;
	public String GwtTableSuggest_editColumn_text;
	public String GwtTableSuggest_editButton_text;

	/**
	 * GwtChangePanel
	 */
	public String GwtChangePanel_changeText;
	public String GwtChangePanel_searchText;


	/**
	 * GwtDoubleChangePanel
	 */
	public String GwtDoubleChangePanel_changeText;
	public String GwtDoubleChangePanel_searchText;


	/**
	 * GwtForm
	 */
	public String GwtForm_mainPanel_width;
	public String GwtForm_title_separation;
	public int GwtForm_title_element_spacing_horizontal;
	public int GwtForm_title_element_spacing_vertical;
	public int GwtForm_max_label_width;
	public int GwtForm_horizontal_separation;
	public String GwtForm_requiredField_string;
	public int GwtForm_requiredField_width;
	public int GwtForm_requiredField_width_separation;

	/**
	 * GwtObjectPicker
	 */
	public String GwtObjectPicker_picker_button_text;
	public String GwtObjectPicker_selectColumn_text;
	public String GwtObjectPicker_searchButton_text;
	public String GwtObjectPicker_clearButton_text;
	public String GwtObjectPicker_closeButton_text;
	public String GwtObjectPicker_scrollPanel_height;
	public String GwtObjectPicker_scrollPanel_width;
	public String GwtObjectPicker_pickerDetails_width;
	public String GwtObjectPicker_defaultPicker_title;
	public String GwtObjectPicker_defaultSearchForm_title;
	public String GwtObjectPicker_dialog_width;
	public String GwtObjectPicker_dialog_height;


	/**
	 * GwtTable
	 */
	public String GwtTable_renglonVacio_text;
	public String GwtTable_default_column_width;
	public HorizontalAlignmentConstant GwtTable_default_text_aligment;
	public String GwtTable_creating_report_msg;
	public String GwtTable_report_export_leyend;

	/**
	 * GwtIconColumn
	 */
	public String GwtIconColumn_toolTip_delete;
	public String GwtIconColumn_toolTip_edit;
	public String GwtIconColumn_toolTip_select;
	public String GwtIconColumn_toolTip_details;
	public String GwtIconColumn_toolTip_active;
	public String GwtIconColumn_toolTip_inactive;

	/**
	 * GwtTextBoxColumn
	 */
	public int GwtTextBoxColumn_textbox_visible_length;
	public int GwtTextBoxColumn_textbox_max_length;
	public String GwtTextBoxColumn_textbox_default_value;

	/**
	 * GwtTextAreaColumn
	 */
	public int GwtTextAreaColumn_textArea_height;
	public int GwtTextAreaColumn_textArea_width;
	public String GwtTextAreaColumn_textArea_default_value;

	/**
	 * GwtPaginationTable
	 */
	public int GwtPaginationTable_max_selectionNumber;
	public String GwtPaginationTable_search_msg;
	public String GwtPaginationTable_first_page;
	public String GwtPaginationTable_last_page;
	public String GwtPaginationTable_previous_page;
	public String GwtPaginationTable_next_page;

	public String GwtPaginationTable_no_items_found;
	public String GwtPaginationTable_one_item_found;
	public String GwtPaginationTable_all_items_found;
	public String GwtPaginationTable_some_items_found;
	public String GwtPaginationTable_separator;

	/**
	 * GwtButtonPanel
	 */
	public String GwtButtonPanel_button_width;
	public String GwtButtonPanel_panel_width;

	/**
	 * GwtModalPopup
	 */
	public String GwtModalPopup_default_msg;

	/**
	 * GwtDefaultErrorPage
	 */
	public String GwtDefaultErrorPage_accessDenied_msg;
	public String GwtDefaultErrorPage_error_msg;
	public String GwtDefaultErrorPage_formTitle_msg;
	public String GwtDefaultErrorPage_descriptionLabel_text;
	public String GwtDefaultErrorPage_errorCodeLabel_text;

	/**
	 * GwtNewsPanel
	 */
	public String GwtNewsPanel_TitleKey;
	public String GwtNewsPanel_ImageKey;
	public String GwtNewsPanel_DescriptionKey;
	public String GwtNewsPanel_DateKey;
	public String GwtNewsPanel_ImageWidth;
	public String GwtNewsPanel_ImageHeight;
	public String GwtNewsPanel_DescriptionWidth;
	public String GwtNewsPanel_SeparatorWidth;

	/**
	 * GwtTabTable
	 */
	public String GwtTabTable_TitleTabKey;

	/**
	 * GwtMenuBar
	 */
	public String GwtMenuBar_HideButton_ShowTooltip;
	public String GwtMenuBar_HideButton_HideTooltip;
	public String GwtMenuBar_HelpButton_Tooltip;


	/**
	 * GwtImageUpload
	 */
	public String GwtImageUpload_RefreshButton_text;
	public String GwtImageUpload_InvalidFile_text;


	/**
	 * GwtRichTextArea
	 */
	public String GwtRichTextArea_validacion_text;
	public String GwtRichTextArea_confirm_text;









}
