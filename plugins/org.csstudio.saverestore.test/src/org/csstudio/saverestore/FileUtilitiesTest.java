package org.csstudio.saverestore;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.csstudio.saverestore.BeamlineSetContent;
import org.csstudio.saverestore.FileUtilities;
import org.csstudio.saverestore.SnapshotContent;
import org.csstudio.saverestore.data.BeamlineSet;
import org.csstudio.saverestore.data.BeamlineSetData;
import org.csstudio.saverestore.data.Branch;
import org.csstudio.saverestore.data.Snapshot;
import org.csstudio.saverestore.data.VSnapshot;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ListDouble;
import org.diirt.util.time.Timestamp;
import org.diirt.vtype.Alarm;
import org.diirt.vtype.AlarmSeverity;
import org.diirt.vtype.Display;
import org.diirt.vtype.Time;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VDoubleArray;
import org.diirt.vtype.VType;
import org.diirt.vtype.ValueFactory;
import org.junit.Test;

/**
 *
 * <code>FileUtilitiesTest</code> tests the methods from the class {@link FileUtilities}.
 *
 * @author <a href="mailto:jaka.bobnar@cosylab.com">Jaka Bobnar</a>
 *
 */
public class FileUtilitiesTest {

    /**
     * Test {@link FileUtilities#split(String)} method
     */
    @Test
    public void testSplit() {
        String str = "TEST1,1,1454595641.463352838,HIHI_ALARM,MAJOR,double,\"1.2323212342342342E16\",,\"---\",";
        String[] result = FileUtilities.split(str);
        assertArrayEquals(result, new String[] { "TEST1", "1", "1454595641.463352838", "HIHI_ALARM", "MAJOR", "double",
            "1.2323212342342342E16", "", "---", "" });

        str = "foo,bar,blabla,,test,";
        result = FileUtilities.split(str);
        assertArrayEquals(result, new String[] { "foo", "bar", "blabla", "", "test", "" });

        str = "\"foo,test\",\"bar something\",blabla,,test,";
        result = FileUtilities.split(str);
        assertArrayEquals(result, new String[] { "foo,test", "bar something", "blabla", "", "test", "" });
    }

    /**
     * Test {@link FileUtilities#generateBeamlineSetContent(BeamlineSetData)} and
     * {@link FileUtilities#readFromBeamlineSet(java.io.InputStream)}.
     *
     * @throws IOException
     */
    @Test
    public void testBeamlineSetData() throws IOException {
        BeamlineSet set = new BeamlineSet(new Branch(), Optional.empty(), new String[] { "first", "second", "third" },
            "someId");
        BeamlineSetData bsd = new BeamlineSetData(set, Arrays.asList("pv1", "pv2"), Arrays.asList("rb1", "rb2"),
            Arrays.asList("d1", "Math.pow(x,3)"), "some description");
        String content = FileUtilities.generateBeamlineSetContent(bsd);
        assertEquals(
            "# Description:\n# some description\n#\nPV,READBACK,DELTA\npv1,rb1,d1\npv2,rb2,\"Math.pow(x,3)\"\n",
            content);

        BeamlineSetContent bsc = FileUtilities
            .readFromBeamlineSet(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
        assertEquals("some description", bsc.getDescription());
        assertArrayEquals(new String[] { "pv1", "pv2" }, bsc.getNames().toArray(new String[0]));
        assertArrayEquals(new String[] { "rb1", "rb2" }, bsc.getReadbacks().toArray(new String[0]));
        assertArrayEquals(new String[] { "d1", "Math.pow(x,3)" }, bsc.getDeltas().toArray(new String[0]));
    }

    /**
     * Test {@link FileUtilities#generateSnapshotFileContent(VSnapshot)} and
     * {@link FileUtilities#readFromSnapshot(java.io.InputStream)}.
     *
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSnapshotData() throws IOException, ParseException {
        BeamlineSet set = new BeamlineSet(new Branch(), Optional.empty(), new String[] { "first", "second", "third" },
            "someId");
        Snapshot snapshot = new Snapshot(set, new Date(), "comment", "owner");
        Date d = new Date(1455296909369L);
        Date d2 = new Date(1455296909379L);
        Alarm alarmNone = ValueFactory.alarmNone();
        Alarm alarm = ValueFactory.newAlarm(AlarmSeverity.MINOR, "HIGH");
        Display display = ValueFactory.displayNone();
        Time time = ValueFactory.newTime(Timestamp.of(d));
        Time time2 = ValueFactory.newTime(Timestamp.of(d2));

        VDouble val1 = ValueFactory.newVDouble(5d, alarm, time, display);
        VDoubleArray val2 = ValueFactory.newVDoubleArray(new ArrayDouble(1, 2, 3), alarmNone, time2, display);
        VDouble rval1 = ValueFactory.newVDouble(6d, alarmNone, time, display);
        VDoubleArray rval2 = ValueFactory.newVDoubleArray(new ArrayDouble(1, 1, 1), alarmNone, time, display);

        VSnapshot vs = new VSnapshot(snapshot, Arrays.asList("pv1", "pv2"), Arrays.asList(true, false),
            Arrays.asList(val1, val2), Arrays.asList("rb1", "rb2"), Arrays.asList(rval1, rval2),
            Arrays.asList("50", "Math.min(x,3)"), time.getTimestamp());

        String content = FileUtilities.generateSnapshotFileContent(vs);
        String CONTENT = "# Date: 2016-02-12 18:08:29.369\n"
            + "PV,SELECTED,TIMESTAMP,STATUS,SEVERITY,VALUE_TYPE,VALUE,READBACK,READBACK_VALUE,DELTA\n"
            + "pv1,1,1455296909.369000000,HIGH,MINOR,double,\"5.0\",rb1,\"6.0\",50\n"
            + "pv2,0,1455296909.379000000,NONE,NONE,double_array,\"[1.0;2.0;3.0]\",rb2,\"[1.0;1.0;1.0]\",\"Math.min(x,3)\"\n";
        assertEquals(CONTENT, content);

        SnapshotContent sc = FileUtilities
            .readFromSnapshot(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
        assertEquals(time.getTimestamp().toDate(), sc.getDate());
        assertArrayEquals(new String[] { "pv1", "pv2" }, sc.getNames().toArray(new String[0]));
        assertArrayEquals(new String[] { "rb1", "rb2" }, sc.getReadbacks().toArray(new String[0]));
        assertArrayEquals(new String[] { "50", "Math.min(x,3)" }, sc.getDeltas().toArray(new String[0]));
        assertArrayEquals(new Boolean[] { true, false }, sc.getSelected().toArray(new Boolean[0]));

        // compare values
        List<VType> data = sc.getData();
        assertEquals(2, data.size());
        VDouble v1 = (VDouble) data.get(0);
        assertEquals(val1.getValue(), v1.getValue());
        assertEquals(val1.getTimestamp(), v1.getTimestamp());
        assertEquals(val1.getAlarmSeverity(), v1.getAlarmSeverity());
        assertEquals(val1.getAlarmName(), v1.getAlarmName());
        VDoubleArray v2 = (VDoubleArray) data.get(1);
        ListDouble ld1 = val2.getData();
        ListDouble ld2 = v2.getData();
        assertEquals(ld1.size(), ld2.size());
        for (int i = 0; i < ld1.size(); i++) {
            assertEquals(ld1.getDouble(i), ld2.getDouble(i), 0);
        }
        assertEquals(val2.getTimestamp(), v2.getTimestamp());
        assertEquals(val2.getAlarmSeverity(), v2.getAlarmSeverity());
        assertEquals(val2.getAlarmName(), v2.getAlarmName());

        // compare readbacks
        data = sc.getReadbackData();
        assertEquals(2, data.size());
        v1 = (VDouble) data.get(0);
        assertEquals(rval1.getValue(), v1.getValue());
        v2 = (VDoubleArray) data.get(1);
        ld1 = rval2.getData();
        ld2 = v2.getData();
        assertEquals(ld1.size(), ld2.size());
        for (int i = 0; i < ld1.size(); i++) {
            assertEquals(ld1.getDouble(i), ld2.getDouble(i), 0);
        }
    }
}
