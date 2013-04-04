    /*
     * This file is part of Panopto-Java-BlockBooker.
     * 
     * Panopto-Java-BlockBooker is free software: you can redistribute it and/or modify
     * it under the terms of the GNU General Public License as published by
     * the Free Software Foundation, either version 3 of the License, or
     * (at your option) any later version.
     * 
     * Panopto-Java-BlockBooker is distributed in the hope that it will be useful,
     * but WITHOUT ANY WARRANTY; without even the implied warranty of
     * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     * GNU General Public License for more details.
     * 
     * You should have received a copy of the GNU General Public License
     * along with Panopto-Java-BlockBooker.  If not, see <http://www.gnu.org/licenses/>.
     * 
     * Copyright: Andrew Martin, Newcastle University
     * 
     */
package uk.ac.ncl.panopto.interfaces;

import java.util.List;

public interface ObjectWithExternalId
{
    public void getPanoptoObjectsByExternalId(String externalId);
    public Object getPanoptoObjectById(String id);
    public Object getPanoptoObjectByName(String name);
    public void updateExternalIdById(String id, String externalId);
    public void updateExternalIdByName(String name, String externalId);
    public void printObject(Object o);
}