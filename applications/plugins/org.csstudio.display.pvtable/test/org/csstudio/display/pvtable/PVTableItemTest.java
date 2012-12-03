package org.csstudio.display.pvtable;

import static org.epics.pvmanager.ExpressionLanguage.channel;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.csstudio.display.pvtable.model.PVTableItem;
import org.csstudio.display.pvtable.model.PVTableItemListener;
import org.csstudio.display.pvtable.model.VTypeHelper;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVWriter;
import org.junit.Before;
import org.junit.Test;

public class PVTableItemTest implements PVTableItemListener
{
	@Before
	public void setup()
	{
		TestSettings.setup();
	}

	
	@Override
	public void tableItemChanged(final PVTableItem item)
	{
		System.out.println(item);
		synchronized (item)
		{
			item.notifyAll();
		}
	}


	@Test(timeout=8000)
	public void testPVTableItem() throws Exception
	{
		final PVWriter<Object> pv = PVManager.write(channel(TestSettings.NAME)).async();
		pv.write(3.14);
		
		final PVTableItem item = new PVTableItem(TestSettings.NAME, PVTableItem.DEFAULT_TOLERANCE, this);
		item.setTolerance(0.1);
		

		// Get initial value
		synchronized (item)
		{
			while (VTypeHelper.toDouble(item.getValue()) != 3.14)
				item.wait(100);
		}
		assertThat(VTypeHelper.toDouble(item.getValue()), equalTo(3.14));

		// There is no saved value, so also no change
		assertThat(item.hasChanged(), equalTo(false));

		// Current matches saved value
		item.save();
		assertThat(item.hasChanged(), equalTo(false));
		
		// Receive update, but within tolerance
		pv.write(3.15);
		synchronized (item)
		{
			while (VTypeHelper.toDouble(item.getValue()) != 3.15)
				item.wait(100);
		}
		assertThat(VTypeHelper.toDouble(item.getValue()), equalTo(3.15));
		assertThat(item.hasChanged(), equalTo(false));
		
		// Receive update beyond tolerance
		pv.write(6.28);
		synchronized (item)
		{
			while (VTypeHelper.toDouble(item.getValue()) != 6.28)
				item.wait(100);
		}
		assertThat(VTypeHelper.toDouble(item.getValue()), equalTo(6.28));		
		
		// Current no longer matches saved value
		System.out.println("Saved: " + VTypeHelper.toString(item.getSavedValue()));
		assertThat(item.hasChanged(), equalTo(true));
		
		// Value changes back on its own
		pv.write(3.14);
		synchronized (item)
		{
			while (VTypeHelper.toDouble(item.getValue()) != 3.14)
				item.wait(100);
		}
		assertThat(VTypeHelper.toDouble(item.getValue()), equalTo(3.14));
		assertThat(item.hasChanged(), equalTo(false));

		item.dispose();
	}
}
