// $Id: RecordStack.java,v 1.1 2013/09/06 10:50:01 argentino Exp $
/**
 * Copyright (C) 2004 Bas Peters
 *
 * This file is part of MARC4J
 *
 * MARC4J is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 *
 * MARC4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with MARC4J; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package org.marc4j;

import java.util.ArrayList;
import java.util.List;

import org.marc4j.marc.Record;

/**
 * Provides <code>push</code> and <code>pop</code> operations for
 * <code>Record</code> objects created by <code>MarcXmlParser</code>.
 * 
 * @author Bas Peters
 * @version $Revision: 1.1 $
 */
public class RecordStack {

  private List list;

  private boolean eof = false;

  /**
   * Default constuctor.
   *  
   */
  public RecordStack() {
    list = new ArrayList();
  }

  /**
   * Pushes a <code>Record</code> object on the stack.
   * 
   * @param record
   *          the record object
   */
  public synchronized void push(Record record) {
    while (list.size() > 0) {
      try {
        wait();
      } catch (Exception e) {
      }
    }
    list.add(record);
    notifyAll();
  }

  /**
   * Removes the <code>Record</code> object from the stack and returns that
   * object.
   * 
   * @return Record - the record object
   */
  public synchronized Record pop() {
    while (list.size() <= 0 && (eof != true)) {
      try {
        wait();
      } catch (Exception e) {
      }
    }
    Record record = null;
    if (list.size() > 0)
      record = (Record) list.remove(0);
    notifyAll();
    return record;

  }

  /**
   * Returns true if there are more <code>Record</code> objects to expect,
   * false otherwise.
   * 
   * @return boolean
   */
  public synchronized boolean hasNext() {
    while (list.size() <= 0 && (eof != true)) {
      try {
        wait();
      } catch (Exception e) {
      }
    }

    if (!isEmpty() || !eof)
      return true;
    return false;
  }

  /**
   * Called when the end of the document is reached.
   *  
   */
  public synchronized void end() {
    eof = true;
    notifyAll();
  }

  /**
   * Returns true if the queue is empty, false otherwise.
   * 
   * @return boolean
   */
  private synchronized boolean isEmpty() {
    return (list.size() == 0 ? true : false);
  }

}