/**
 * 
 */
package org.epics.pvmanager.graphene;

import java.awt.image.BufferedImage;
import java.util.List;

import org.epics.graphene.Point2DDataset;
import org.epics.graphene.ScatterGraph2DRenderer;
import org.epics.graphene.ScatterGraph2DRendererUpdate;
import org.epics.pvmanager.QueueCollector;
import org.epics.pvmanager.ReadFunction;
import org.epics.vtype.VImage;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.ValueUtil;

/**
 * @author shroffk
 * 
 */
public class ScatterGraphFunction implements ReadFunction<Plot2DResult> {

    private ReadFunction<? extends VNumberArray> yArray;
    private ReadFunction<? extends VNumberArray> xArray;

    private ScatterGraph2DRenderer renderer = new ScatterGraph2DRenderer(300,
	    200);

    private VImage previousImage;

    private final QueueCollector<ScatterGraph2DRendererUpdate> rendererUpdateQueue = new QueueCollector<>(
	    100);

    public ScatterGraphFunction(ReadFunction<? extends VNumberArray> yArray,
	    ReadFunction<? extends VNumberArray> xArray) {
	this.yArray = yArray;
	this.xArray = xArray;
    }

    public QueueCollector<ScatterGraph2DRendererUpdate> getRendererUpdateQueue() {
	return rendererUpdateQueue;
    }

    @Override
    public Plot2DResult readValue() {
	VNumberArray newYData = yArray.readValue();
	VNumberArray newXData = xArray.readValue();

	Point2DDataset dataset = null;
	dataset = org.epics.graphene.Point2DDatasets.lineData(
		newXData.getData(), newYData.getData());

	List<ScatterGraph2DRendererUpdate> updates = rendererUpdateQueue
		.readValue();
	for (ScatterGraph2DRendererUpdate scatterGraph2DRendererUpdate : updates) {
	    renderer.update(scatterGraph2DRendererUpdate);
	}

	if (renderer.getImageHeight() == 0 && renderer.getImageWidth() == 0) {
	    return null;
	}

	BufferedImage image = new BufferedImage(renderer.getImageWidth(),
		renderer.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
	renderer.draw(image.createGraphics(), dataset);

	previousImage = ValueUtil.toVImage(image);
	return new Plot2DResult(previousImage, new PlotDataRange(
		renderer.getXPlotRange(), renderer.getXPlotRange(),
		renderer.getXAggregatedRange()), new PlotDataRange(
		renderer.getYPlotRange(), renderer.getYPlotRange(),
		renderer.getYAggregatedRange()));

    }
}
