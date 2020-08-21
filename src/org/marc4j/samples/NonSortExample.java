// $Id: NonSortExample.java,v 1.1 2013/09/06 10:50:03 argentino Exp $
/**
 * Copyright (C) 2002-2006 Bas Peters
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
 */
package org.marc4j.samples;

import java.io.InputStream;

import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;

/**
 * Demonstrates getting the title without non-sorting characters.
 * 
 * @author Bas Peters
 * @version $Revision: 1.1 $
 */
public class NonSortExample {

    public static void main(String args[]) throws Exception {

        InputStream input = DataFieldExample.class
                .getResourceAsStream("resources/chabon.mrc");

        MarcReader reader = new MarcStreamReader(input);
        while (reader.hasNext()) {
            Record record = reader.next();

            // get data field 245
            DataField dataField = (DataField) record.getVariableField("245");

            // get indicator as int value
            char ind2 = dataField.getIndicator2();

            // get the title proper
            Subfield subfield = dataField.getSubfield('a');
            String title = subfield.getData();
            System.out.println("Title proper: " + title);

            // remove the non sorting characters
            int nonSort = Character.digit(ind2, 10);
            title = title.substring(nonSort);
            System.out.println("Title non-sort (" + nonSort + "): " + title
                    + '\n');
        }

    }

}
