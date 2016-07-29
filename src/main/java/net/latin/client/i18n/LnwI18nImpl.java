package net.latin.client.i18n;

import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * Implementación default de LnwI18n
 *
 * @author Matias Leone
 */
public class LnwI18nImpl extends LnwI18n {

	/**
	 * Si se quiere extender esta clase para crear una nueva configuracion, pero
	 * se quieren aprovechar alguno de los valores de esta clase base, no olvidar
	 * llamar a super.registerParameters() en la clase que extiende.
	 */
	protected void registerParameters() {

		//###############################CONSTANTES#####################################//

		/**
		 * GwtLabel
		 */
		GwtLabel_width = "200px";//200px
		GwtLabel_height = "auto";

		/**
		 * GwtListSuggest
		 */
		GwtListSuggest_searchButton_text = "Buscar";
		GwtListSuggest_noResult_text = "No se encontraron coincidencias";


		/**
		 * GwtTableSuggest
		 */
		GwtTableSuggest_searchButton_text = "Buscar";
		GwtTableSuggest_noResult_text = "No se encontraron coincidencias";
		GwtTableSuggest_editColumn_text = "Seleccionar";
		GwtTableSuggest_editButton_text = "Seleccionar";


		/**
		 * GwtChangePanel
		 */
		GwtChangePanel_changeText = "Cambiar";
		GwtChangePanel_searchText = "Buscar";


		/**
		 * GwtDoubleChangePanel
		 */
		GwtDoubleChangePanel_changeText = "Cambiar";
		GwtDoubleChangePanel_searchText = "Buscar";


		/**
		 * GwtForm
		 */
		GwtForm_mainPanel_width = "100%";
		GwtForm_title_separation = "10px";
		GwtForm_title_element_spacing_horizontal = 2;
		GwtForm_title_element_spacing_vertical = 5;
		GwtForm_max_label_width = 150;
		GwtForm_horizontal_separation = 5;
		GwtForm_requiredField_string = "(*)";
		GwtForm_requiredField_width = 17;
		GwtForm_requiredField_width_separation = 22;


		/**
		 * GwtObjectPicker
		 */
		GwtObjectPicker_picker_button_text = "Buscar";
		GwtObjectPicker_selectColumn_text = "Seleccionar";
		GwtObjectPicker_searchButton_text = "Buscar";
		GwtObjectPicker_clearButton_text = "Limpiar";
		GwtObjectPicker_closeButton_text = "Cerrar";
		GwtObjectPicker_scrollPanel_width = "460px";
		GwtObjectPicker_scrollPanel_height = "190px";
		GwtObjectPicker_pickerDetails_width = "150px";
		GwtObjectPicker_defaultPicker_title = "Seleccione un elemento";
		GwtObjectPicker_defaultSearchForm_title = "Opciones de búsqueda";
		GwtObjectPicker_dialog_width = "500";
		GwtObjectPicker_dialog_height = "300";

		/**
		 * GwtTable
		 */
		GwtTable_renglonVacio_text = "Sin elementos";
		GwtTable_default_column_width = "100px";
		GwtTable_default_text_aligment = HorizontalPanel.ALIGN_CENTER;
		GwtTable_creating_report_msg = "Generando reporte...";
		GwtTable_report_export_leyend = "Exportar a :";

		/**
		 * GwtIconColumn
		 */
		GwtIconColumn_toolTip_delete = "Eliminar";
		GwtIconColumn_toolTip_edit = "Editar";
		GwtIconColumn_toolTip_select = "Seleccionar";
		GwtIconColumn_toolTip_details = "Detalle";
		GwtIconColumn_toolTip_active = "Activo";
		GwtIconColumn_toolTip_inactive = "Inactivo";

		/**
		 * GwtTextBoxColumn
		 */
		GwtTextBoxColumn_textbox_visible_length = 10;
		GwtTextBoxColumn_textbox_max_length = 10;
		GwtTextBoxColumn_textbox_default_value = "0";

		/**
		 * GwtTextAreaColumn
		 */
		GwtTextAreaColumn_textArea_height = 100;
		GwtTextAreaColumn_textArea_width = 150;
		GwtTextAreaColumn_textArea_default_value = "";

		/**
		 * GwtPaginationTable
		 */
		GwtPaginationTable_max_selectionNumber = 10;
		GwtPaginationTable_search_msg = "Buscando resultados...";
		GwtPaginationTable_first_page = "Primero";
		GwtPaginationTable_last_page = "Último";
		GwtPaginationTable_previous_page ="Anterior";
		GwtPaginationTable_next_page = "Siguiente";

		GwtPaginationTable_no_items_found = "No se han encontrado registros.";
		GwtPaginationTable_one_item_found = "Se ha encontrado un solo registro.";
		GwtPaginationTable_all_items_found = "{0} registros encontrados, viendo todos los registros.";
		GwtPaginationTable_some_items_found = "{2} registros encontrados, viendo del {0} al {1}."; //"Resultados <B>{1}</B> - <B>{2}</B> de <B>{3}</B>.";
		GwtPaginationTable_separator = ",&nbsp;";



		/**
		 * GwtButtonPanel
		 */
		GwtButtonPanel_button_width = "200px";
		GwtButtonPanel_panel_width = "400px";


		/**
		 * GwtModalPopup
		 */
		GwtModalPopup_default_msg = "Cargando, espere un momento...";


		/**
		 * GwtDefaultErrorPage
		 */
		GwtDefaultErrorPage_accessDenied_msg = "Acceso denegado, compruebe sus permisos de usuario.";
		GwtDefaultErrorPage_error_msg = "Errores internos, por favor consulte al área de sistemas.";
		GwtDefaultErrorPage_formTitle_msg = "Ha ocurrido un problema";
		GwtDefaultErrorPage_descriptionLabel_text = "Descripción";
		GwtDefaultErrorPage_errorCodeLabel_text = "Código de error";

		/**
		 * GwtNewsPanel
		 */
		GwtNewsPanel_TitleKey = "title";
		GwtNewsPanel_ImageKey = "image";
		GwtNewsPanel_DescriptionKey = "description";
		GwtNewsPanel_DateKey = "date";
		GwtNewsPanel_ImageWidth = "55px";
		GwtNewsPanel_ImageHeight = "55px";
		GwtNewsPanel_DescriptionWidth = "260px";
		GwtNewsPanel_SeparatorWidth = "90%";

		/**
		 * GwtTabTable
		 */
		GwtTabTable_TitleTabKey = "title";

		/**
		 * GwtMenuBar
		 */
		GwtMenuBar_HideButton_ShowTooltip = "Mostrar menú";
		GwtMenuBar_HideButton_HideTooltip = "Esconder menú";
		GwtMenuBar_HelpButton_Tooltip = "Ver documentación de la sección actual";

		/**
		 * GwtImageUpload
		 */
		GwtImageUpload_RefreshButton_text = "Cargar/Refrescar";
		GwtImageUpload_InvalidFile_text = "El archivo no corresponde a una imagen valida.";

		/**
		 * GwtRichTextArea
		 */
		GwtRichTextArea_validacion_text ="Las siguientes palabras superan el area de edición\n" +
		"Mantenga el texto dentro del área de edición, ya que si esta\n" +
		"se supera es posible que el texto no sea impreso por completo.\n";;
		GwtRichTextArea_confirm_text = "\n\n  ¿Desea imprimir sin corregir el texto?";;
	}



}
