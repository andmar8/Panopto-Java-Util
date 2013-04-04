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

package uk.ac.ncl.panopto.dataObjects;

public class Session
{
    protected String name;
    protected String folderExternalId;
    protected String startDateTime;
    protected String endDateTime;
    protected String location;
    protected String externalId;

    public Session(){}
    public Session(String name, String folderExternalId,
                    String startDateTime, String endDateTime,
                    String location, String externalId)
    {
        this.name = name;
        this.folderExternalId = folderExternalId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.location = location;
        this.externalId = externalId;
    }

    public String getEndDateTime()
    {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime)
    {
        this.endDateTime = endDateTime;
    }

    public String getExternalId()
    {
        return externalId;
    }

    public void setExternalId(String externalId)
    {
        this.externalId = externalId;
    }

    public String getFolderExternalId()
    {
        return folderExternalId;
    }

    public void setFolderExternalId(String folderExternalId)
    {
        this.folderExternalId = folderExternalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getStartDateTime()
    {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime)
    {
        this.startDateTime = startDateTime;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }
}
