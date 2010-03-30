/*
 * Copyright (c) 2007 Stiftung Deutsches Elektronen-Synchrotron,
 * Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
 *
 * THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS.
 * WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE AND
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE DEFECTIVE
 * IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING, REPAIR OR
 * CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE.
 * NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 * DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS,
 * OR MODIFICATIONS.
 * THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION, MODIFICATION,
 * USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE DISTRIBUTION OF THIS
 * PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU MAY FIND A COPY
 * AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
 */
package org.csstudio.sds.components.model;

import org.csstudio.sds.model.AbstractWidgetModel;
import org.csstudio.sds.model.WidgetPropertyCategory;
import org.csstudio.sds.util.ColorAndFontUtil;

/**
 * A thumb wheel widget model.
 * 
 * @author Alen Vrecko, Jozef Stefan Institute
 * @author Joerg Rathlev, Universitaet Hamburg
 */
public class ThumbWheelModel extends AbstractWidgetModel {

	public static final String PROP_FONT = "font"; //$NON-NLS-1$

	public static final String PROP_MIN = "min"; //$NON-NLS-1$

	public static final String PROP_MAX = "max"; //$NON-NLS-1$

	public static final String PROP_INTERNAL_FRAME_THICKNESS = "internalFrameSize"; //$NON-NLS-1$

	public static final String PROP_INTERNAL_FRAME_COLOR = "internalFrameColor"; //$NON-NLS-1$

	public static final String PROP_WHOLE_DIGITS_PART = "wholeDigits"; //$NON-NLS-1$

	public static final String PROP_DECIMAL_DIGITS_PART = "decimalDigits"; //$NON-NLS-1$

	public static final String ID = "org.csstudio.sds.components.ThumbWheel"; //$NON-NLS-1$

	public static final String PROP_VALUE = "value"; //$NON-NLS-1$

	private static final int DEFAULT_HEIGHT = 50;

	/**
	 * The default value of the width property.
	 */
	private static final int DEFAULT_WIDTH = 20;

	/**
	 * Standard constructor.
	 */
	public ThumbWheelModel() {
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTypeID() {
		return ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configureProperties() {
		addDoubleProperty(PROP_VALUE, "Value", WidgetPropertyCategory.BEHAVIOR, 0);
		addIntegerProperty(PROP_WHOLE_DIGITS_PART, "Whole digits", WidgetPropertyCategory.DISPLAY, 5);
		addDoubleProperty(PROP_MIN, "Min", WidgetPropertyCategory.BEHAVIOR, Double.NaN);
		addDoubleProperty(PROP_MAX, "Max", WidgetPropertyCategory.BEHAVIOR, Double.NaN);

		// FIXME: 18.02.2010: swende: Ist f�r PROP_WHOLE_DIGITS_PART bereits angemeldet!? Von J�rg oder CosyLab zu pr�fen!
		addIntegerProperty(PROP_WHOLE_DIGITS_PART, "Integer digits", WidgetPropertyCategory.BEHAVIOR, 5);
		addIntegerProperty(PROP_DECIMAL_DIGITS_PART, "Decimal digits", WidgetPropertyCategory.BEHAVIOR, 5);

		addFontProperty(PROP_FONT, "Wheel Fonts", WidgetPropertyCategory.DISPLAY, ColorAndFontUtil.toFontString("Arial", 9));

		addColorProperty(PROP_INTERNAL_FRAME_COLOR, "Internal frame color", WidgetPropertyCategory.DISPLAY, "#000000");

		addIntegerProperty(PROP_INTERNAL_FRAME_THICKNESS, "Internal frame thickness", WidgetPropertyCategory.DISPLAY, 0);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getDefaultToolTip() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(createTooltipParameter(PROP_ALIASES) + "\n");
		buffer.append("Value:\t");
		buffer.append(createTooltipParameter(PROP_VALUE));
		return buffer.toString();
	}

	public int getWholePartDigits() {
		return getIntegerProperty(PROP_WHOLE_DIGITS_PART);
	}

	public void setWholePartDigits(int val) {
		setPropertyValue(PROP_WHOLE_DIGITS_PART, val);
	}

	public int getDecimalPartDigits() {
		return getIntegerProperty(PROP_DECIMAL_DIGITS_PART);
	}

	public void setDecimalPartDigits(int val) {
		setPropertyValue(PROP_DECIMAL_DIGITS_PART, val);
	}

	public double getValue() {
		return getDoubleProperty(PROP_VALUE);
	}

	public int getInternalFrameThickness() {
		return getIntegerProperty(PROP_INTERNAL_FRAME_THICKNESS);
	}

	public double getMin() {
		return getDoubleProperty(PROP_MIN);
	}

	public double getMax() {
		return getDoubleProperty(PROP_MAX);
	}

	public void setManualValue(double val) {
		setPropertyValue(PROP_VALUE, val);
	}

	public int getInternalBorderWidth() {
		return getIntegerProperty(PROP_INTERNAL_FRAME_THICKNESS);
	}
}
