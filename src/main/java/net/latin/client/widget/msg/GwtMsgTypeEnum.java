package net.latin.client.widget.msg;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconSize;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialIcon;

public enum GwtMsgTypeEnum {
	OK(0,IconType.CHECK,Color.LIGHT_GREEN_DARKEN_1,Color.LIGHT_GREEN_LIGHTEN_4),
	ERROR(1,IconType.ERROR,Color.RED_DARKEN_1,Color.RED_LIGHTEN_4),
	ALERT(2,IconType.WARNING,Color.YELLOW_DARKEN_1,Color.YELLOW_LIGHTEN_4),
	LOADING(3,IconType.SEARCH,Color.LIGHT_BLUE_DARKEN_1,Color.LIGHT_BLUE_LIGHTEN_4);
	
	private int id;
	private IconType iconType;
	private Color bgColor;
	private Color iconColor;

	private GwtMsgTypeEnum(int id,IconType iconType,Color iconColor,Color bgColor) {
		this.id = id;
		this.iconType = iconType;
		this.iconColor = iconColor;
		this.bgColor = bgColor;
	}
	
	public MaterialIcon getIcon(){
		MaterialIcon icon=new MaterialIcon(iconType);
		icon.setIconSize(IconSize.SMALL);
		icon.setIconColor(iconColor);
		icon.setPaddingLeft(5);
		icon.setPaddingRight(5);
		return icon;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public IconType getIconType() {
		return iconType;
	}

	public void setIconType(IconType iconType) {
		this.iconType = iconType;
	}

	public Color getBgColor() {
		return bgColor;
	}
	
}
