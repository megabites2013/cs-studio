
/* 
 * Copyright (c) 2008 Stiftung Deutsches Elektronen-Synchrotron, 
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
 *
 */

package org.csstudio.alarm.jms2ora.util;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 *  @author Markus Moeller
 *
 */
public class MessageContent implements Serializable
{
    /** Serial version id */
    private static final long serialVersionUID = -5582704742047919825L;

    /** Hash table that contains the values of the known message properties */
    private Hashtable<Long, String> msgIdContent = null;
    
    /** Hash table that contains the values of the known message properties */
    private Hashtable<String, String> msgNameContent = null;

    /** Hash table that contains the values of the unknown message properties */
    private Vector<String> unknownContent = null;
    
    /** ID of the property 'UNKNOWN' in the database table */
    private long unknownId;
    
    /** Flag, that indicates whether or not the message should be discarded */
    private boolean discard = false;
    
    public MessageContent()
    {
        msgIdContent = new Hashtable<Long, String>();
        msgNameContent = new Hashtable<String, String>();
        unknownContent = new Vector<String>();
        unknownId = -1;
    }
    
    public String getPropertyValue(Long key)
    {
        return msgIdContent.get(key);
    }
    
    public void put(Long key, String name, String value)
    {
        msgIdContent.put(key, value);
        msgNameContent.put(name, value);
    }

    public String getPropertyValue(String name)
    {
        return msgNameContent.get(name);
    }

    public Enumeration<Long> keys()
    {
        return msgIdContent.keys();
    }
        
    public boolean hasContent()
    {
        return(!msgIdContent.isEmpty());
    }
    
    /**
     * 
     *  @return True, if the object contains unknown message properties, false otherwise
     */
    public boolean unknownPropertiesAvailable()
    {
        return (!unknownContent.isEmpty());
    }
    
    public int countUnknownProperties()
    {
        return unknownContent.size();
    }
    
    public String getUnknownProperty(int index)
    {
        return unknownContent.get(index);
    }
    
    public void addUnknownProperty(String value)
    {
        unknownContent.add(value);
    }
    
    public void setUnknownTableId(long id)
    {
        this.unknownId = id;
    }
    
    public long getUnknownTableId()
    {
        return this.unknownId;
    }
    
    public void setDiscard(boolean discard)
    {
        this.discard = discard;
    }
    
    public boolean discard()
    {
        return discard;
    }
    
    public String toString()
    {
        String temp = null;
        String result = "{MessageContent";
        
        result = result + ":[Known properties]";
        
        Enumeration<String> list = msgNameContent.keys();
        while(list.hasMoreElements())
        {
            temp = list.nextElement();
            result = result + "(" + temp + "=" + msgNameContent.get(temp) + ")";
        }
        
        result = result + ":[Unknown properties]";

        list = unknownContent.elements();
        while(list.hasMoreElements())
        {
            temp = list.nextElement();
            result = result + "(" + temp + ")";
        }
        
        result = result + ":[Object attributes]";
        
        result = result + "(unknownId=" + unknownId + ")";
        result = result + "(discard=" + discard + ")";

        result = result + "}";
        
        return result;
    }
    
    public String toPrintableString()
    {
        String temp = null;
        String nl = System.getProperty("line.separator");
        String result = "MessageContent" + nl;
        
        result = result + " Known properties" + nl;
        
        Enumeration<String> list = msgNameContent.keys();
        while(list.hasMoreElements())
        {
            temp = list.nextElement();
            result = result + "  " + temp + " = " + msgNameContent.get(temp) + nl;
        }
        
        result = result + nl + " Unknown properties" + nl;

        list = unknownContent.elements();
        while(list.hasMoreElements())
        {
            temp = list.nextElement();
            result = result + "  " + temp + nl;
        }
        
        result = result + nl + " Object attributes" + nl;
        
        result = result + "  unknownId = " + unknownId + nl;
        result = result + "  discard = " + discard + nl;

        return result;
    }
}
