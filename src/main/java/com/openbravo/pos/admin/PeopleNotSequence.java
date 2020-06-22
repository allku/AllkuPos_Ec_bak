//    AllkuPOS Ec  - Touch Friendly Point Of Sale
//    https://www.allku.expert
//
//    This file is part of uniCenta oPOS
//
//    AllkuPOS Ec is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    AllkuPOS Ec is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with AllkuPOS Ec.  If not, see <http://www.gnu.org/licenses/>.
package com.openbravo.pos.admin;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializerRead;

/**
 *
 * @author jorgequiguango
 */
public class PeopleNotSequence {

    String peopleId;
    String peopleName;
    String visible;

    /**
     * Main method to return user without sequence
     */
    public PeopleNotSequence() {
    }

    /**
     *
     * @param peopleId
     * @param peopleName
     * @param visible
     */
    public PeopleNotSequence(String peopleId, String peopleName, String visible) {
        this.peopleId = peopleId;
        this.peopleName = peopleName;
        this.visible = visible;
    }

    public String getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(String peopleId) {
        this.peopleId = peopleId;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }

    /**
     *
     * @return serializer for this people
     */
    public static SerializerRead getSerializerRead() {
        return new SerializerRead() {

            @Override
            public Object readValues(DataRead dr) throws BasicException {

                String peopleId = dr.getString(1);
                String peopleName = dr.getString(2);
                String visible = dr.getString(3);

                return new PeopleNotSequence(peopleId, peopleName, visible);
            }
        };
    }
}
