package org.csstudio.channel.opiwidgets;

import org.csstudio.channel.widgets.ChannelTreeByPropertyWidget;
import org.csstudio.opibuilder.editparts.AbstractBaseEditPart;
import org.eclipse.swt.SWT;

public class ChannelTreeByPropertyFigure extends AbstractChannelWidgetFigure<ChannelTreeByPropertyWidget> {
	
	public ChannelTreeByPropertyFigure(AbstractBaseEditPart editPart) {
		super(editPart);
		widget = new ChannelTreeByPropertyWidget(composite, SWT.NONE);
		selectionProvider = widget.getTreeSelectionProvider();
	}
}
