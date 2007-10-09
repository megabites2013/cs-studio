package org.csstudio.sds.components.ui.internal.editparts;

import org.csstudio.sds.ui.editparts.AbstractWidgetEditPart;
import org.csstudio.sds.ui.editparts.IWidgetPropertyChangeHandler;
import org.eclipse.core.runtime.IPath;
import org.eclipse.draw2d.IFigure;

import org.csstudio.sds.components.model.ImageModel;
import org.csstudio.sds.components.ui.internal.figures.RefreshableImageFigure;

/**
 * EditPart controller for the image widget.
 * 
 * @author jbercic
 * 
 */
public final class ImageEditPart extends AbstractWidgetEditPart {

	/**
	 * Returns the casted model. This is just for convenience.
	 * 
	 * @return the casted {@link ImageModel}
	 */
	protected ImageModel getCastedModel() {
		return (ImageModel) getModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IFigure doCreateFigure() {
		ImageModel model = getCastedModel();
		// create AND initialize the view properly
		final RefreshableImageFigure figure = new RefreshableImageFigure();
		
		figure.setFilePath(model.getFilename());
		figure.setTopCrop(model.getTopCrop());
		figure.setBottomCrop(model.getBottomCrop());
		figure.setLeftCrop(model.getLeftCrop());
		figure.setRightCrop(model.getRightCrop());
		figure.setStretch(model.getStretch());
		
		return figure;
	}
	
	/**
	 * Register change handlers for the four crop properties.
	 */
	protected void registerCropPropertyHandlers() {
		// top
		IWidgetPropertyChangeHandler handle = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue, final Object newValue,
					final IFigure figure) {
				RefreshableImageFigure imageFigure = (RefreshableImageFigure) figure;
				imageFigure.setTopCrop((Integer)newValue);
				return true;
			}
		};
		setPropertyChangeHandler(ImageModel.PROP_TOPCROP, handle);
		
		// bottom
		handle = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue, final Object newValue,
					final IFigure figure) {
				RefreshableImageFigure imageFigure = (RefreshableImageFigure) figure;
				imageFigure.setBottomCrop((Integer)newValue);
				return true;
			}
		};
		setPropertyChangeHandler(ImageModel.PROP_BOTTOMCROP, handle);
		
		// left
		handle = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue, final Object newValue,
					final IFigure figure) {
				RefreshableImageFigure imageFigure = (RefreshableImageFigure) figure;
				imageFigure.setLeftCrop((Integer)newValue);
				return true;
			}
		};
		setPropertyChangeHandler(ImageModel.PROP_LEFTCROP, handle);
		
		// right
		handle = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue, final Object newValue,
					final IFigure figure) {
				RefreshableImageFigure imageFigure = (RefreshableImageFigure) figure;
				imageFigure.setRightCrop((Integer)newValue);
				return true;
			}
		};
		setPropertyChangeHandler(ImageModel.PROP_RIGHTCROP, handle);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void registerPropertyChangeHandlers() {
		// changes to the filename property
		IWidgetPropertyChangeHandler handle = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue, final Object newValue,
					final IFigure figure) {
				RefreshableImageFigure imageFigure = (RefreshableImageFigure) figure;
				imageFigure.setFilePath((IPath)newValue);
				return true;
			}
		};
		setPropertyChangeHandler(ImageModel.PROP_FILENAME, handle);
		
		// changes to the stretch property
		handle = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue, final Object newValue,
					final IFigure figure) {
				RefreshableImageFigure imageFigure = (RefreshableImageFigure) figure;
				imageFigure.setStretch((Boolean)newValue);
				return true;
			}
		};
		setPropertyChangeHandler(ImageModel.PROP_STRETCH, handle);
		
		// changes to the border width property
		handle = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue, final Object newValue,
					final IFigure figure) {
				RefreshableImageFigure imageFigure = (RefreshableImageFigure) figure;
				imageFigure.resizeImage();
				return true;
			}
		};
		setPropertyChangeHandler(ImageModel.PROP_BORDER_WIDTH, handle);
		setPropertyChangeHandler(ImageModel.PROP_BORDER_STYLE, handle);
		
		//size change handlers - so we can stretch accordingly
		handle = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue, final Object newValue,
					final IFigure figure) {
				RefreshableImageFigure imageFigure = (RefreshableImageFigure) figure;
				imageFigure.resizeImage();
				return true;
			}
		};
		setPropertyChangeHandler(ImageModel.PROP_HEIGHT, handle);
		setPropertyChangeHandler(ImageModel.PROP_WIDTH, handle);
		
		registerCropPropertyHandlers();
	}
}
